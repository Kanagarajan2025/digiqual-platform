package com.digiqual.repository;

import com.digiqual.entity.PartnerInstitute;
import com.digiqual.entity.StudentEnrollment;
import com.digiqual.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollment, Long> {

    Optional<StudentEnrollment> findByUserAccount(User userAccount);

    Optional<StudentEnrollment> findByIdAndPartnerInstitute(Long id, PartnerInstitute partnerInstitute);

    List<StudentEnrollment> findTop8ByPartnerInstituteOrderByUpdatedAtDesc(PartnerInstitute partnerInstitute);

    List<StudentEnrollment> findTop8ByStateContainingIgnoreCaseOrderByUpdatedAtDesc(String state);

    long countByPartnerBatchId(Long partnerBatchId);

    long countByPartnerInstituteAndStateContainingIgnoreCase(PartnerInstitute partnerInstitute, String state);

    long countByStateContainingIgnoreCase(String state);
}
