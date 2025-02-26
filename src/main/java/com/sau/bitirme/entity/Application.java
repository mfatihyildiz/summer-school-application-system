package com.sau.bitirme.entity;

import com.sau.bitirme.enums.ApplicationStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne
    @JoinColumn(name = "studentId", nullable = false) // Veritabanında studentId sütunu ile ilişki
    private Student student;

    @ManyToOne
    @JoinColumn(name = "homeCourseId", nullable = false)
    private HomeCourse homeCourse;

    @ManyToOne
    @JoinColumn(name = "externalCourseId", nullable = false)
    private ExternalCourse externalCourse;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    private LocalDate submissionDate = LocalDate.now();

    // Getters and Setters
    public Long getApplicationId() {
        return applicationId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public HomeCourse getHomeCourse() {
        return homeCourse;
    }

    public void setHomeCourse(HomeCourse homeCourse) {
        this.homeCourse = homeCourse;
    }

    public ExternalCourse getExternalCourse() {
        return externalCourse;
    }

    public void setExternalCourse(ExternalCourse externalCourse) {
        this.externalCourse = externalCourse;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }
}

