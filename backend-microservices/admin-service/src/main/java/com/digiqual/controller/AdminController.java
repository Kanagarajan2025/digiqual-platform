package com.digiqual.controller;

import com.digiqual.dto.AdminDashboardResponse;
import com.digiqual.dto.ReturnBatchRequest;
import com.digiqual.dto.UpdateCertificatePaymentRequest;
import com.digiqual.dto.UpdateCertificateStageRequest;
import com.digiqual.service.DashboardService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/admin")
public class AdminController {

    private final DashboardService dashboardService;

    public AdminController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> getDashboard(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            return ResponseEntity.ok(dashboardService.getAdminDashboard(authentication.getName()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping("/partners/{partnerId}/approve")
    public ResponseEntity<Void> approvePartner(@PathVariable Long partnerId, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            dashboardService.approvePartner(authentication.getName(), partnerId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/partners/{partnerId}/suspend")
    public ResponseEntity<Void> suspendPartner(@PathVariable Long partnerId, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            dashboardService.suspendPartner(authentication.getName(), partnerId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/certificates/{certificateCode}/stage")
    public ResponseEntity<Void> updateCertificateStage(@PathVariable String certificateCode,
                                                       @Valid @RequestBody UpdateCertificateStageRequest request,
                                                       Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            dashboardService.updateCertificateStage(authentication.getName(), certificateCode, request.getStage());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/certificates/{certificateCode}/payment")
    public ResponseEntity<Void> updateCertificatePayment(@PathVariable String certificateCode,
                                                         @RequestBody UpdateCertificatePaymentRequest request,
                                                         Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            dashboardService.updateCertificatePayment(authentication.getName(), certificateCode, request.isPaid());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/students/{enrollmentId}/approve")
    public ResponseEntity<Void> approveStudent(@PathVariable Long enrollmentId, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            dashboardService.approveStudent(authentication.getName(), enrollmentId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/students/{enrollmentId}/reject")
    public ResponseEntity<Void> rejectStudent(@PathVariable Long enrollmentId, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            dashboardService.rejectStudent(authentication.getName(), enrollmentId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/batches/{batchId}/approve")
    public ResponseEntity<Void> approveBatch(@PathVariable Long batchId, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            dashboardService.approveBatch(authentication.getName(), batchId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/batches/{batchId}/return")
    public ResponseEntity<Void> returnBatch(@PathVariable Long batchId,
                                            @Valid @RequestBody ReturnBatchRequest request,
                                            Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            dashboardService.returnBatch(authentication.getName(), batchId, request.getReason());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
