package com.sau.bitirme.repository;

import com.sau.bitirme.entity.Committee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteeRepo extends JpaRepository<Committee, Long> {

    Committee findByEmail(String email);

    Committee findByEmailAndPassword(String Email, String password);
}
