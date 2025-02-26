package com.sau.bitirme.controller;

import com.sau.bitirme.entity.Committee;
import com.sau.bitirme.entity.Student;
import com.sau.bitirme.repository.CommitteeRepo;
import com.sau.bitirme.repository.StudentRepo;
import com.sau.bitirme.service.ApplicationPeriodService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private CommitteeRepo committeeRepo;
    @Autowired
    private ApplicationPeriodService applicationPeriodService;

    @GetMapping("/dashboard")
    public String dashboard(final HttpSession session, final Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || role == null) {
            return "redirect:/login"; // Kullanıcı giriş yapmadıysa login sayfasına yönlendir.
        }

        String fullName = "Bilinmeyen Kullanıcı";

        if ("student".equals(role)) {
            Student student = studentRepo.findByStudentNumber(username);
            if (student != null) {
                fullName = student.getName() + " " + student.getSurname();
            }
            model.addAttribute("role", "student");
            model.addAttribute("showApply", true);
        }

        if ("committee".equals(role)) {
            Committee committee = committeeRepo.findByEmail(username);
            if (committee != null) {
                fullName = committee.getName() + " " + committee.getSurname();
            }
            model.addAttribute("role", "committee");
            model.addAttribute("showAllTabs", true);
        }

        boolean isPreviewMode = applicationPeriodService.isPreviewOpen();
        boolean isApplicationPeriod = applicationPeriodService.isApplicationOpen();

        model.addAttribute("fullName", fullName);
        model.addAttribute("isPreviewMode", isPreviewMode);
        model.addAttribute("isApplicationPeriod", isApplicationPeriod);

        return "dashboard";
    }
}
