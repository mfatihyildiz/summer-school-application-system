package com.sau.bitirme.entity;

import com.sau.bitirme.enums.Semester;
import com.sau.bitirme.enums.EducationYear;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;
    @Column(unique = true, nullable = false)
    private String studentNumber;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(unique = true,nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Semester semester;
    @Enumerated(EnumType.STRING)
    private EducationYear educationYear;
    private double gpa;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<ExternalCourse> externalCourses;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Application> applications;

    public String getStudentNumber() {
        return studentNumber;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public EducationYear getEducationYear() {
        return educationYear;
    }

    public void setEducationYear(EducationYear educationYear) {
        this.educationYear = educationYear;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }
}
