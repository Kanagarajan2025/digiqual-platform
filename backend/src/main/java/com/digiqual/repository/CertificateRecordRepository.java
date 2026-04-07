package com.digiqual.repository;

import com.digiqual.entity.CertificateRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CertificateRecordRepository extends JpaRepository<CertificateRecord, Long> {

    long countByIssuedAtAfter(LocalDateTime start);

    List<CertificateRecord> findTop8ByOrderByIdDesc();

    Optional<CertificateRecord> findByCertificateCode(String certificateCode);
}
