package com.sau.bitirme.repository;

import com.sau.bitirme.entity.HomeCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeCourseRepo extends JpaRepository<HomeCourse, Long> {
}
