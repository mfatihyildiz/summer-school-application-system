import com.sau.bitirme.BitirmeApplication;
import com.sau.bitirme.entity.*;
import com.sau.bitirme.enums.Language;
import com.sau.bitirme.enums.Semester;
import com.sau.bitirme.enums.EducationYear;
import com.sau.bitirme.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = BitirmeApplication.class)
public class StudentTest {

    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private CommitteeRepo committeeRepo;
    @Autowired
    private HomeCourseRepo homeCourseRepo;
    @Autowired
    private ExternalCourseRepo externalCourseRepo;
    @Autowired
    private UniversityRepo universityRepo;


    @Test
    public void addStudent() {
        Student student = new Student();
        student.setName("2");
        student.setSurname("Yıldız");
        student.setEmail("222@gmail.com");
        student.setPassword("222");
        student.setSemester(Semester.SPRING);
        student.setEducationYear(EducationYear.SECOND);
        student.setGpa(2.4);
        student.setStudentNumber("222");


        studentRepo.save(student);
    }

    @Test
    public void addCommittee() {
        Committee committee = new Committee();
        committee.setName("John");
        committee.setSurname("Doe");
        committee.setEmail("asd");
        committee.setPassword("asd");


        committeeRepo.save(committee);
    }

    @Test
    public void AddHomeCourse() {
        // Yeni bir HomeCourse nesnesi oluştur
        HomeCourse homeCourse = new HomeCourse();
        homeCourse.setCourseName("4-2");
        homeCourse.setEcts(5);
        homeCourse.setLanguage(Language.ENGLISH);
        homeCourse.setTheoreticalHours(3);
        homeCourse.setPracticalHours(1);
        homeCourse.setSemester(Semester.SPRING);
        homeCourse.setDescription("4-2 dersi açıklaması \n deneme deneme");
        homeCourse.setEducationYear(EducationYear.FOURTH);

        // Kaydı veritabanına ekle
        homeCourseRepo.save(homeCourse);
    }

    @Test
    public void AddUniversity(){
        University university = new University();
        university.setUniversityName("İTÜ");
        university.setFacultyName("Bilgisayar Fakültesi");
        university.setDepartmentName("cs");

        universityRepo.save(university);
    }

    @Test
    public void testAddExternalCourse() {
        // Öğrenci kaydet
        Student student = new Student();
        student.setStudentNumber("b123");
        student.setName("Alib");
        student.setSurname("Velib");
        student.setEmail("alib.veli@example.com");
        student.setPassword("pass");
        student.setSemester(Semester.FALL);
        student.setEducationYear(EducationYear.THIRD);
        student.setGpa(3.5);
        Student savedStudent = studentRepo.save(student);

        // ExternalCourse nesnesini oluştur
        ExternalCourse externalCourse = new ExternalCourse();
        //University.setUniversityName("Ankara Üniversitesi");
        //externalCourse.setFacultyName("Mühendislik Fakültesi");
        //externalCourse.setDepartmentName("Bilgisayar Mühendisliği");
        externalCourse.setCourseName("veri yapıları");
        externalCourse.setEcts(6);
        externalCourse.setLanguage(Language.ENGLISH);
        externalCourse.setTheoreticalHours(3);
        externalCourse.setPracticalHours(2);
        externalCourse.setDescription("veriler.");
        externalCourse.setStudent(savedStudent); // İlişkiyi belirt

        // Kaydı veritabanına ekle
        ExternalCourse savedCourse = externalCourseRepo.save(externalCourse);
    }
}
