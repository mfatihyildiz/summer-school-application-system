package com.sau.bitirme.entity;

import com.sau.bitirme.enums.Language;
import jakarta.persistence.*;

@Entity
public class ExternalCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long externalCourseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student;

    private String courseName;
    private int ects;
    @Enumerated(EnumType.STRING)
    private Language language;
    private int theoreticalHours;
    private int practicalHours;
    private String description;

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false, referencedColumnName = "university_id")
    private University university;

    public Long getExternalCourseId() {
        return externalCourseId;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public int getTheoreticalHours() {
        return theoreticalHours;
    }

    public void setTheoreticalHours(int theoreticalHours) {
        this.theoreticalHours = theoreticalHours;
    }

    public int getPracticalHours() {
        return practicalHours;
    }

    public void setPracticalHours(int practicalHours) {
        this.practicalHours = practicalHours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

// Getters and Setters
}
