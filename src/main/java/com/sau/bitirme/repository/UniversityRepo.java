package com.sau.bitirme.repository;

import com.sau.bitirme.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UniversityRepo extends JpaRepository<University, Long> {

    @Query("SELECT DISTINCT u.universityName FROM University u")
    List<String> findDistinctUniversityNames();

    @Query("SELECT DISTINCT u.facultyName FROM University u WHERE u.universityName = :universityName")
    List<String> findDistinctFacultyNamesByUniversityName(@Param("universityName") String universityName);

    @Query("SELECT DISTINCT u.departmentName FROM University u WHERE u.universityName = :universityName AND u.facultyName = :facultyName")
    List<String> findDistinctDepartmentsByFacultyAndUniversity(@Param("universityName") String universityName, @Param("facultyName") String facultyName);

    Optional<University> findByUniversityNameAndFacultyNameAndDepartmentName(String universityName, String facultyName, String departmentName);
    Optional<University> findByUniversityName(String universityName);


}
