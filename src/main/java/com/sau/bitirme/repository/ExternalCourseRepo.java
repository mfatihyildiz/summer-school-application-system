package com.sau.bitirme.repository;

import com.sau.bitirme.entity.ExternalCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExternalCourseRepo extends JpaRepository<ExternalCourse, Long> {

    @Query("SELECT e.externalCourseId FROM ExternalCourse e " +
            "WHERE e.university.universityName = :universityName " +
            "AND e.university.facultyName = :facultyName " +
            "AND e.university.departmentName = :departmentName " +
            "AND e.courseName = :courseName")
    Optional<Long> findExternalCourseId(
            @Param("universityName") String universityName,
            @Param("facultyName") String facultyName,
            @Param("departmentName") String departmentName,
            @Param("courseName") String courseName
    );

    @Query("SELECT e.courseName FROM ExternalCourse e WHERE e.university.universityId = :universityId")
    List<String> findCourseNamesByUniversityId(@Param("universityId") Long universityId);

    @Query("SELECT e FROM ExternalCourse e WHERE e.university.universityId = :universityId AND e.courseName = :courseName")
    Optional<ExternalCourse> findByUniversityIdAndCourseName(@Param("universityId") Long universityId, @Param("courseName") String courseName);


    // custom queries
}
