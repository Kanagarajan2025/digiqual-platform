package com.digiqual.repository;

import com.digiqual.entity.PartnerInstitute;
import com.digiqual.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartnerInstituteRepository extends JpaRepository<PartnerInstitute, Long> {

    long countByStatus(String status);

    List<PartnerInstitute> findByStatusOrderByCreatedAtDesc(String status);

    Optional<PartnerInstitute> findByOwnerUser(User ownerUser);

    boolean existsByName(String name);
}
