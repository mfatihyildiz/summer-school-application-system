package com.sau.bitirme.repository;

import com.sau.bitirme.entity.ApplicationPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationPeriodRepo extends JpaRepository<ApplicationPeriod, Long> {
    Optional<ApplicationPeriod> findTopByOrderByIdDesc(); // **En büyük ID’ye sahip olan kaydı getir**
}
