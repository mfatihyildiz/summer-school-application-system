package com.sau.bitirme.entity;

import com.sau.bitirme.enums.EducationYear;
import com.sau.bitirme.enums.Language;
import com.sau.bitirme.enums.Semester;
import jakarta.persistence.*;

@Entity
public class HomeCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long homeCourseId;
    private String courseName;
    private int ects;
    @Enumerated(EnumType.STRING)
    private Language language;
    private int theoreticalHours;
    private int practicalHours;
    @Enumerated(EnumType.STRING)
    private Semester semester;
    @Enumerated(EnumType.STRING)
    private EducationYear educationYear;
    private String description;

    public Long getHomeCourseId() {
        return homeCourseId;
    }

    public void setHomeCourseId(Long homeCourseId) {
        this.homeCourseId = homeCourseId;
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

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EducationYear getEducationYear() {
        return educationYear;
    }

    public void setEducationYear(EducationYear educationYear) {
        this.educationYear = educationYear;
    }
}

