package com.sau.bitirme.controller;

import com.sau.bitirme.entity.*;
import com.sau.bitirme.enums.ApplicationStatus;
import com.sau.bitirme.enums.EducationYear;
import com.sau.bitirme.repository.*;
import com.sau.bitirme.service.ApplicationPeriodService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationRepo applicationRepo;
    @Autowired
    private HomeCourseRepo homeCourseRepo;
    @Autowired
    private ExternalCourseRepo externalCourseRepo;
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private UniversityRepo universityRepo;
    @Autowired
    private ApplicationPeriodService applicationPeriodService;

    @GetMapping("/new")
    public String showNewApplicationPage(final Model model, final HttpSession session) {
        String studentNumber = (String) session.getAttribute("username");

        if (studentNumber == null) {
            return "redirect:/login"; // Kullanıcı giriş yapmamışsa login sayfasına yönlendir.
        }

        Student student = studentRepo.findByStudentNumber(studentNumber);
        if (student == null) {
            model.addAttribute("errorMessage", "Öğrenci bilgileri bulunamadı.");
            return "dashboard";
        }

        boolean isPreviewMode = applicationPeriodService.isPreviewOpen();
        boolean isApplicationPeriod = applicationPeriodService.isApplicationOpen();

        model.addAttribute("isPreviewMode", isPreviewMode);
        model.addAttribute("isApplicationPeriod", isApplicationPeriod);

        if (isPreviewMode) {
            model.addAttribute("previewMode", true);
        }

        if (!isPreviewMode && !isApplicationPeriod) {
            model.addAttribute("showErrorPopup", true);
            model.addAttribute("errorMessage", "Başvurular şu anda kapalıdır. Lütfen belirlenen tarihlerde tekrar deneyin.");
            return "redirect:/dashboard";
        }

        //Öğrencinin bilgilerini alalım
        EducationYear studentYear = student.getEducationYear();
        double gpa = student.getGpa();

        final EducationYear maxAllowedYear;
        if (gpa >= 3.00) {
            maxAllowedYear = EducationYear.values()[Math.min(studentYear.ordinal() + 1, EducationYear.values().length - 1)];
        } else {
            maxAllowedYear = studentYear;
        }

        List<HomeCourse> eligibleCourses = homeCourseRepo.findAll().stream()
                .filter(course -> course.getEducationYear().ordinal() <= maxAllowedYear.ordinal())
                .toList();

        if (eligibleCourses.isEmpty()) {
            model.addAttribute("errorMessage", "Seçebileceğiniz uygun ders bulunmamaktadır.");
        } else {
            model.addAttribute("homeCourses", eligibleCourses);
        }

        return "new-application";
    }

    @PostMapping("/step2")
    public String proceedToNextStep(final @RequestParam Long selectedCourseId, final HttpSession session) {
        session.setAttribute("selectedHomeCourseId", selectedCourseId);
        return "redirect:/applications/new/2";
    }

    @GetMapping("/new/2")
    public String showNewApplication2Page(final HttpSession session, final Model model) {
        Long homeCourseId = (Long) session.getAttribute("selectedHomeCourseId");

        if (homeCourseId == null) {
            throw new RuntimeException("HomeCourse ID bulunamadı. Lütfen tekrar başlayın.");
        }

        List<String> universityNames = universityRepo.findDistinctUniversityNames();
        model.addAttribute("universityNames", universityNames);
        model.addAttribute("homeCourseId", homeCourseId); // Model'e ekleme
        return "new-application2";
    }

    @GetMapping("/external-course-id")
    @ResponseBody
    public Long getExternalCourseId(final @RequestParam String universityName, final @RequestParam String facultyName,
                                    final @RequestParam String departmentName, final @RequestParam String courseName) {
        // 1. Adım: UniversityId'yi al
        Long universityId = universityRepo
                .findByUniversityNameAndFacultyNameAndDepartmentName(universityName, facultyName, departmentName)
                .map(University::getUniversityId)
                .orElseThrow(() -> new RuntimeException("University ID bulunamadı."));

        // 2. Adım: ExternalCourseId'yi al
        return externalCourseRepo
                .findByUniversityIdAndCourseName(universityId, courseName)
                .map(ExternalCourse::getExternalCourseId)
                .orElseThrow(() -> new RuntimeException("ExternalCourse ID bulunamadı."));
    }

    @PostMapping("/set-external-course")
    public String setExternalCourseId(final @RequestParam Long externalCourseId, final HttpSession session) {
        session.setAttribute("selectedExternalCourseId", externalCourseId);
        return "redirect:/applications/confirmation";
    }

    @GetMapping("/confirmation")
    public String showConfirmationPage(final HttpSession session, final Model model) {
        // Session'dan ID'leri al
        Long homeCourseId = (Long) session.getAttribute("selectedHomeCourseId");
        Long externalCourseId = (Long) session.getAttribute("selectedExternalCourseId");
        Long studentId = (Long) session.getAttribute("studentId");

        if (homeCourseId == null || externalCourseId == null || studentId == null) {
            model.addAttribute("errorMessage", "Gerekli veriler eksik. Lütfen işlemi tekrar başlatın.");
            return "confirmation";  // Hata mesajıyla birlikte sayfayı göster
        }

        // HomeCourse ve ExternalCourse verilerini çek
        Optional<HomeCourse> homeCourseOpt = homeCourseRepo.findById(homeCourseId);
        Optional<ExternalCourse> externalCourseOpt = externalCourseRepo.findById(externalCourseId);

        if (homeCourseOpt.isEmpty() || externalCourseOpt.isEmpty()) {
            model.addAttribute("errorMessage", "Seçilen dersler sistemde bulunamadı!");
            return "confirmation";
        }

        HomeCourse homeCourse = homeCourseOpt.get();
        ExternalCourse externalCourse = externalCourseOpt.get();

        // Model'e verileri ekle
        model.addAttribute("homeCourse", homeCourse);
        model.addAttribute("externalCourse", externalCourse);
        model.addAttribute("studentId", studentId);

        return "confirmation";
    }

    @PostMapping("/submit")
    public String submitApplication(final @RequestParam Long homeCourseId, final @RequestParam Long externalCourseId,
                                    final @RequestParam Long studentId, final Model model) {
        // Veritabanından HomeCourse, ExternalCourse ve Student nesneleri alınıyor
        HomeCourse homeCourse = homeCourseRepo.findById(homeCourseId)
                .orElseThrow(() -> new RuntimeException("HomeCourse bulunamadı."));
        ExternalCourse externalCourse = externalCourseRepo.findById(externalCourseId)
                .orElseThrow(() -> new RuntimeException("ExternalCourse bulunamadı."));
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student bulunamadı."));

        // AKTS kontrolü
        if (externalCourse.getEcts() < homeCourse.getEcts()) {
            model.addAttribute("errorMessage", "Dışarıdan alınacak dersin AKTS değeri, seçilen HomeCourse'un AKTS değerine eşit veya daha büyük olmalıdır.");
            return "confirmation"; // Kullanıcıyı tekrar onay sayfasına yönlendirir
        }

        //Dil kontrolü
        if (!externalCourse.getLanguage().equals(homeCourse.getLanguage())) {
            model.addAttribute("errorMessage", "Seçilen derslerin eğitim dili aynı olmalıdır!");
            return "confirmation"; //Kullanıcıyı tekrar onay sayfasına yönlendirir
        }

        // Yeni bir Application oluşturur
        Application application = new Application();
        application.setHomeCourse(homeCourse);
        application.setExternalCourse(externalCourse);
        application.setStudent(student);
        application.setStatus(ApplicationStatus.PENDING);
        application.setSubmissionDate(LocalDate.now());
        // Application'ı kaydeder
        applicationRepo.save(application);

        model.addAttribute("message", "Başvuru başarıyla oluşturuldu!");
        return "redirect:/dashboard";
    }

    @GetMapping("/my-applications")
    public String getMyApplications(final HttpSession session, final Model model) {
        String studentNumber = (String) session.getAttribute("username");

        if (studentNumber == null) {
            return "redirect:/login"; // Eğer oturumda öğrenci yoksa giriş sayfasına yönlendir
        }

        Student student = studentRepo.findByStudentNumber(studentNumber);
        if (student == null) {
            return "redirect:/dashboard"; // Eğer öğrenci bulunamazsa, dashboard'a yönlendir
        }

        List<Application> applicationsList = applicationRepo.findByStudent_StudentId(student.getStudentId());
        model.addAttribute("applications", applicationsList);
        model.addAttribute("fullName", student.getName() + " " + student.getSurname());

        return "applicationPages/my-applications"; // Öğrenci başvurularını ayrı bir sayfada göster
    }

    @GetMapping("/pending")
    public String getPendingApplications(final Model model) {
        // PENDING statüsündeki başvuruları çek
        List<Application> pendingApplications = applicationRepo.findByStatus(ApplicationStatus.PENDING);

        // Model'e ekleyerek sayfaya gönder
        model.addAttribute("pendingApplications", pendingApplications);

        return "applicationPages/pending-applications"; // Bekleyen başvurular sayfasına yönlendirme
    }

    @GetMapping("/approved")
    public String approvedApplications(final Model model) {
        List<Application> approvedApplications = applicationRepo.findByStatus(ApplicationStatus.APPROVED);
        model.addAttribute("approvedApplications", approvedApplications);
        return "applicationPages/approved-applications";
    }

    @PostMapping("/approve/{id}")
    public String approveApplication(final @PathVariable Long id) {
        Application application = applicationRepo.findById(id).orElseThrow(() -> new RuntimeException("Başvuru bulunamadı!"));

        application.setStatus(ApplicationStatus.APPROVED);
        applicationRepo.save(application);

        return "redirect:/applications/pending"; // Sayfayı yenile
    }

    @GetMapping("/rejected")
    public String rejectedApplications(final Model model) {
        List<Application> rejectedApplications = applicationRepo.findByStatus(ApplicationStatus.REJECTED);
        model.addAttribute("rejectedApplications", rejectedApplications);
        return "applicationPages/rejected-applications";
    }

    @PostMapping("/reject/{id}")
    public String rejectApplication(final @PathVariable Long id) {
        Application application = applicationRepo.findById(id).orElseThrow(() -> new RuntimeException("Başvuru bulunamadı!"));

        application.setStatus(ApplicationStatus.REJECTED);
        applicationRepo.save(application);

        return "redirect:/applications/pending"; // Sayfayı yenile
    }

    @PostMapping("/reject-approved/{id}")
    public String rejectApprovedApplication(final @PathVariable Long id) {
        Application application = applicationRepo.findById(id).orElseThrow(() -> new RuntimeException("Başvuru bulunamadı!"));

        if (application.getStatus() == ApplicationStatus.APPROVED) {
            application.setStatus(ApplicationStatus.REJECTED);
            applicationRepo.save(application);
        }

        return "redirect:/applications/approved"; // Onaylı başvurular sayfasına geri dön
    }

    @PostMapping("/approve-rejected/{id}")
    public String approveRejectedApplication(final @PathVariable Long id) {
        Application application = applicationRepo.findById(id).orElseThrow(() -> new RuntimeException("Başvuru bulunamadı!"));

        // Eğer başvuru zaten REJECTED değilse işlem yapma
        if (application.getStatus() != ApplicationStatus.REJECTED) {
            throw new RuntimeException("Bu başvuru zaten onaylı veya beklemede!");
        }

        application.setStatus(ApplicationStatus.APPROVED);
        applicationRepo.save(application);

        return "redirect:/applications/rejected";
    }

    @PostMapping("/cancel/{id}")
    public String cancelApplication(final @PathVariable Long id, final HttpSession session) {
        Application application = applicationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Başvuru bulunamadı!"));

        // Oturumdan giriş yapan öğrenci kimliğini al
        String studentNumber = (String) session.getAttribute("username");
        Student student = studentRepo.findByStudentNumber(studentNumber);

        // Başvurunun gerçekten öğrenciye ait olup olmadığını kontrol et
        if (student == null || !application.getStudent().getStudentId().equals(student.getStudentId())) {
            throw new RuntimeException("Bu başvuruyu iptal etme yetkiniz yok!");
        }

        // Başvuru zaten onaylandı veya reddedildiyse iptal edilemez
        if (application.getStatus() == ApplicationStatus.APPROVED || application.getStatus() == ApplicationStatus.REJECTED) {
            throw new RuntimeException("Onaylanmış veya reddedilmiş başvurular iptal edilemez!");
        }

        application.setStatus(ApplicationStatus.CANCELLED);
        applicationRepo.save(application);

        return "redirect:/applications/my-applications";
    }

    @GetMapping("/settings")
    public String settings(final Model model) {
        model.addAttribute("page", "settings");
        return "settings";
    }
}
