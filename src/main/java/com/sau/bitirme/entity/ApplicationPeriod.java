package com.sau.bitirme.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ApplicationPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime previewStartDate;
    private LocalDateTime previewEndDate;
    private LocalDateTime applicationStartDate;
    private LocalDateTime applicationEndDate;

    // Getter-Setter Metotları
    public LocalDateTime getPreviewStartDate() {
        return previewStartDate;
    }

    public void setPreviewStartDate(LocalDateTime previewStartDate) {
        this.previewStartDate = previewStartDate;
    }

    public LocalDateTime getPreviewEndDate() {
        return previewEndDate;
    }

    public void setPreviewEndDate(LocalDateTime previewEndDate) {
        this.previewEndDate = previewEndDate;
    }

    public LocalDateTime getApplicationStartDate() {
        return applicationStartDate;
    }

    public void setApplicationStartDate(LocalDateTime applicationStartDate) {
        this.applicationStartDate = applicationStartDate;
    }

    public LocalDateTime getApplicationEndDate() {
        return applicationEndDate;
    }

    public void setApplicationEndDate(LocalDateTime applicationEndDate) {
        this.applicationEndDate = applicationEndDate;
    }
}
