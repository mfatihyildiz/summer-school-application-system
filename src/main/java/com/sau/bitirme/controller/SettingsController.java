package com.sau.bitirme.controller;

import com.sau.bitirme.entity.ApplicationPeriod;
import com.sau.bitirme.repository.ApplicationPeriodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class SettingsController {

    @Autowired
    private ApplicationPeriodRepo applicationPeriodRepo;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    // **Ayarlar sayfasını yükle**
    @GetMapping("/settings")
    public String showSettingsPage(Model model) {
        // **En son eklenen kaydı getir**
        ApplicationPeriod latestPeriod = applicationPeriodRepo.findTopByOrderByIdDesc().orElse(new ApplicationPeriod());
        model.addAttribute("currentPeriod", latestPeriod);
        return "settings";
    }

    // **Yeni tarihleri kaydetme işlemi**
    @PostMapping("/settings/update-period")
    public String updateApplicationPeriod(
            @RequestParam("previewStart") String previewStart,
            @RequestParam("previewEnd") String previewEnd,
            @RequestParam("applyStart") String applyStart,
            @RequestParam("applyEnd") String applyEnd,
            Model model) {

        try {
            // **Tarihleri LocalDateTime formatına çevir**
            LocalDateTime previewStartDate = LocalDateTime.parse(previewStart, formatter);
            LocalDateTime previewEndDate = LocalDateTime.parse(previewEnd, formatter);
            LocalDateTime applicationStartDate = LocalDateTime.parse(applyStart, formatter);
            LocalDateTime applicationEndDate = LocalDateTime.parse(applyEnd, formatter);

            // **Yeni bir ApplicationPeriod kaydı oluştur**
            ApplicationPeriod newPeriod = new ApplicationPeriod();
            newPeriod.setPreviewStartDate(previewStartDate);
            newPeriod.setPreviewEndDate(previewEndDate);
            newPeriod.setApplicationStartDate(applicationStartDate);
            newPeriod.setApplicationEndDate(applicationEndDate);

            // **LOG: Database'e kayıt işlemi öncesi**
            System.out.println("💾 Database'e kayıt yapılıyor...");

            // **Kaydı veritabanına ekleyelim**
            applicationPeriodRepo.save(newPeriod);

            // **LOG: Kayıt başarılı mesajı**
            System.out.println("✅ Yeni başvuru dönemi başarıyla kaydedildi!");

            model.addAttribute("successMessage", "Başvuru tarihleri başarıyla eklendi!");

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Tarih formatı hatalı! Lütfen doğru formatta girin.");
            System.out.println("HATA! Tarih eklenemedi.");
        }

        return "settings";
    }
}
