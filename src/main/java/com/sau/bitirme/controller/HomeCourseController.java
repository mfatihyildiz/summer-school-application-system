package com.sau.bitirme.controller;

import com.sau.bitirme.entity.HomeCourse;
import com.sau.bitirme.repository.HomeCourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/homeCourses")
public class HomeCourseController {
    @Autowired
    private HomeCourseRepo homeCourseRepo;

    @GetMapping
    public List<HomeCourse> getAllHomeCourses() {
        return homeCourseRepo.findAll();
    }

    @GetMapping("/{id}")
    public HomeCourse getHomeCourseById(final @PathVariable Long id) {
        return homeCourseRepo.findById(id).orElseThrow(() -> new RuntimeException("ExternalCourse not found with id: " + id));
    }

    @PostMapping
    public HomeCourse createHomeCourse(final @RequestBody HomeCourse homeCourse) {
        return homeCourseRepo.save(homeCourse);
    }

    @DeleteMapping("/{id}")
    public void deleteHomeCourse(final @PathVariable Long id) {
        homeCourseRepo.deleteById(id);
    }
}
