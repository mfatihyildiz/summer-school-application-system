package com.sau.bitirme.controller;

import com.sau.bitirme.entity.ExternalCourse;
import com.sau.bitirme.entity.Student;
import com.sau.bitirme.entity.University;
import com.sau.bitirme.repository.ExternalCourseRepo;
import com.sau.bitirme.repository.StudentRepo;
import com.sau.bitirme.repository.UniversityRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/external")
public class ExternalCourseController {

    @Autowired
    private ExternalCourseRepo externalCourseRepo;
    @Autowired
    private UniversityRepo universityRepo;

    @GetMapping("/external-course-id")
    public ResponseEntity<Long> getExternalCourseId(
            @RequestParam String universityName,
            @RequestParam String facultyName,
            @RequestParam String departmentName,
            @RequestParam String courseName
    ) {
        Optional<Long> externalCourseId = externalCourseRepo.findExternalCourseId(universityName, facultyName, departmentName, courseName);

        if (externalCourseId.isPresent()) {
            return ResponseEntity.ok(externalCourseId.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @Autowired
    private StudentRepo studentRepo;

    // Ders Adlarını Getiren Endpoint
    @GetMapping("/courses")
    public ResponseEntity<List<String>> getCoursesByUniversityDetails(
            @RequestParam String universityName,
            @RequestParam String facultyName,
            @RequestParam String departmentName) {

        // University tablosunda sorgu yaparak universityId'yi bul
        Optional<University> optionalUniversity = universityRepo.findByUniversityNameAndFacultyNameAndDepartmentName(
                universityName, facultyName, departmentName
        );

        University university = optionalUniversity.orElseThrow(() ->
                new RuntimeException("Belirtilen üniversite, fakülte ve bölüm bilgileri bulunamadı."));

        if (university == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(List.of("Üniversite bilgilerine uygun kayıt bulunamadı."));
        }

        // ExternalCourse tablosunda universityId ile sorgu yaparak courseName'leri al
        List<String> courseNames = externalCourseRepo.findCourseNamesByUniversityId(university.getUniversityId());

        if (courseNames.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(List.of("Seçilen bilgilere uygun ders bulunamadı."));
        }

        return ResponseEntity.ok(courseNames);
    }

    @PostMapping("/add-course")
    public ResponseEntity<String> addCourse(@RequestBody ExternalCourse externalCourse, HttpSession session) {
        // Oturumdan studentId alın
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            return ResponseEntity.badRequest().body("Oturumda bir öğrenci bulunamadı.");
        }

        // Öğrenciyi al ve kontrol et
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı!"));

        // Üniversiteyi tablodan bul
        University university = universityRepo
                .findByUniversityNameAndFacultyNameAndDepartmentName(
                        externalCourse.getUniversity().getUniversityName(),
                        externalCourse.getUniversity().getFacultyName(),
                        externalCourse.getUniversity().getDepartmentName()
                ).orElseThrow(() -> new RuntimeException("Üniversite bilgileri bulunamadı."));

        // ExternalCourse'a University ID'yi set et
        externalCourse.setUniversity(university);
        externalCourse.setStudent(student);

        // ExternalCourse'u kaydet
        externalCourseRepo.save(externalCourse);

        return ResponseEntity.ok("Ders başarıyla eklendi!");
    }







}
