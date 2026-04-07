package com.digiqual.repository;

import com.digiqual.entity.PartnerBatch;
import com.digiqual.entity.PartnerInstitute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartnerBatchRepository extends JpaRepository<PartnerBatch, Long> {

    long countByStatus(String status);

    List<PartnerBatch> findByPartnerInstituteOrderByLastUpdatedDesc(PartnerInstitute partnerInstitute);

    List<PartnerBatch> findTop8ByStatusOrderByLastUpdatedDesc(String status);

    List<PartnerBatch> findTop6ByPartnerInstituteOrderByLastUpdatedDesc(PartnerInstitute partnerInstitute);

    Optional<PartnerBatch> findByIdAndPartnerInstitute(Long id, PartnerInstitute partnerInstitute);

    boolean existsByBatchCode(String batchCode);
}
