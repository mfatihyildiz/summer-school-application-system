package com.sau.bitirme.controller;

import com.sau.bitirme.entity.University;
import com.sau.bitirme.repository.UniversityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/university")
public class UniversityController {

    @Autowired
    private UniversityRepo universityRepo;

    @GetMapping("/universities")
    public List<String> getUniversities() {
        return universityRepo.findDistinctUniversityNames();
    }

    @GetMapping("/faculties")
    public List<String> getFaculties(@RequestParam String universityName) {
        return universityRepo.findDistinctFacultyNamesByUniversityName(universityName);
    }

    @GetMapping("/departments")
    public List<String> getDepartments(@RequestParam String universityName, @RequestParam String facultyName) {
        return universityRepo.findDistinctDepartmentsByFacultyAndUniversity(universityName, facultyName);
    }

    @GetMapping("/find-id")
    public ResponseEntity<Long> getUniversityId(@RequestParam String universityName) {
        University university = universityRepo.findByUniversityName(universityName)
                .orElseThrow(() -> new RuntimeException("University not found"));
        return ResponseEntity.ok(university.getUniversityId());
    }
}
