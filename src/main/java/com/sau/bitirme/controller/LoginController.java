package com.sau.bitirme.controller;

import com.sau.bitirme.entity.Committee;
import com.sau.bitirme.entity.Student;
import com.sau.bitirme.repository.StudentRepo;
import com.sau.bitirme.repository.CommitteeRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private CommitteeRepo committeeRepo;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        // Öğrenci olarak giriş yapmayı kontrol et
        Student student = studentRepo.findByStudentNumberAndPassword(username, password);
        if (student != null) {
            // Öğrenci ise oturuma kullanıcıyı ve rolünü kaydet
            session.setAttribute("studentId", student.getStudentId());
            session.setAttribute("username", username);
            session.setAttribute("role", "student");
            return "redirect:/dashboard";
        }

        // Komite üyesi olarak giriş yapmayı kontrol et
        Committee committee = committeeRepo.findByEmailAndPassword(username, password);
        if (committee != null) {
            // Komite üyesi ise oturuma kullanıcıyı ve rolünü kaydet
            session.setAttribute("username", username);
            session.setAttribute("role", "committee");
            return "redirect:/dashboard";
        }

        model.addAttribute("errorMessage", "Geçersiz kullanıcı adı veya şifre");
        return "login";
    }
}
