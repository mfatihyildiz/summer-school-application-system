package com.sau.bitirme.service;

import com.sau.bitirme.entity.ApplicationPeriod;
import com.sau.bitirme.repository.ApplicationPeriodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ApplicationPeriodService {

    @Autowired
    private ApplicationPeriodRepo applicationPeriodRepo;

    public boolean isPreviewOpen() {
        Optional<ApplicationPeriod> applicationPeriodOpt = applicationPeriodRepo.findTopByOrderByIdDesc();

        if (applicationPeriodOpt.isEmpty()) {
            return false; // Eğer tarih yoksa false döndür
        }

        ApplicationPeriod applicationPeriod = applicationPeriodOpt.get();
        LocalDateTime now = LocalDateTime.now();

        return now.isAfter(applicationPeriod.getPreviewStartDate()) && now.isBefore(applicationPeriod.getPreviewEndDate());
    }

    public boolean isApplicationOpen() {
        Optional<ApplicationPeriod> applicationPeriodOpt = applicationPeriodRepo.findTopByOrderByIdDesc();

        if (applicationPeriodOpt.isEmpty()) {
            return false; // Eğer tarih yoksa false döndür
        }

        ApplicationPeriod applicationPeriod = applicationPeriodOpt.get();
        LocalDateTime now = LocalDateTime.now();

        return now.isAfter(applicationPeriod.getApplicationStartDate()) && now.isBefore(applicationPeriod.getApplicationEndDate());
    }
}
