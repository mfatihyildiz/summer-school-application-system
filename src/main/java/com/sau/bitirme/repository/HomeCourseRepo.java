package com.sau.bitirme.repository;

import com.sau.bitirme.entity.HomeCourse;
import com.sau.bitirme.enums.EducationYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeCourseRepo extends JpaRepository<HomeCourse, Long> {
    List<HomeCourse> findByEducationYearLessThanEqual(EducationYear educationYear);
    // custom queries
}
