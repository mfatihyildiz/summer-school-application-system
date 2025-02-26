package com.sau.bitirme.controller;

import com.sau.bitirme.entity.Student;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.sau.bitirme.repository.StudentRepo;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepo studentRepo;

    @GetMapping("/get-student-id")
    public ResponseEntity<Long> getStudentId(HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(studentId);
    }





    @GetMapping("/students/add")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "add-student";
    }

    @PostMapping("/students/add")
    public String addStudent(Student student) {
        studentRepo.save(student);
        return "redirect:/students/add";
    }

    @GetMapping("/students/search")
    public String showSearchForm() {
        return "search-student";
    }

    @PostMapping("/students/search")
    public String searchStudent(@RequestParam("studentNumber") String studentNumber, Model model) {
        Student student = studentRepo.findByStudentNumber(studentNumber);
        if (student != null) {
            model.addAttribute("student", student);
        } else {
            model.addAttribute("error", "Öğrenci bulunamadı!");
        }
        return "search-student";
    }
}
