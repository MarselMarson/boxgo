package com.szd.boxgo.repo;

import com.szd.boxgo.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepo extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByCodeAndEmail(String code, String email);
}
