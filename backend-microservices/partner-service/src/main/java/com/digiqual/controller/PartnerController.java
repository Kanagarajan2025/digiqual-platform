package com.digiqual.controller;

import com.digiqual.dto.CreateBatchRequest;
import com.digiqual.dto.CreateEnrollmentRequest;
import com.digiqual.dto.PartnerDashboardResponse;
import com.digiqual.dto.UpdateEnrollmentRequest;
import com.digiqual.service.DashboardService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/partner")
public class PartnerController {

    private final DashboardService dashboardService;

    public PartnerController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<PartnerDashboardResponse> getDashboard(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            return ResponseEntity.ok(dashboardService.getPartnerDashboard(authentication.getName()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping("/batches")
    public ResponseEntity<Void> createBatch(@Valid @RequestBody CreateBatchRequest request, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            dashboardService.createBatch(authentication.getName(), request);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/batches/{batchId}/submit")
    public ResponseEntity<Void> submitBatch(@PathVariable Long batchId, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            dashboardService.submitBatch(authentication.getName(), batchId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/batches/{batchId}")
    public ResponseEntity<Void> deleteBatch(@PathVariable Long batchId, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            dashboardService.deleteBatch(authentication.getName(), batchId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/enrollments")
    public ResponseEntity<Void> createEnrollment(@Valid @RequestBody CreateEnrollmentRequest request,
                                                 Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            dashboardService.createEnrollment(authentication.getName(), request);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/enrollments/{enrollmentId}")
    public ResponseEntity<Void> updateEnrollment(@PathVariable Long enrollmentId,
                                                 @Valid @RequestBody UpdateEnrollmentRequest request,
                                                 Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            dashboardService.updateEnrollment(authentication.getName(), enrollmentId, request);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/enrollments/{enrollmentId}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long enrollmentId, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            dashboardService.deleteEnrollment(authentication.getName(), enrollmentId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
