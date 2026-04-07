package com.digiqual.controller;

import com.digiqual.dto.StudentDashboardResponse;
import com.digiqual.dto.PublicCertificateVerificationResponse;
import com.digiqual.dto.StudentProfileResponse;
import com.digiqual.service.StudentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/me")
    public ResponseEntity<StudentProfileResponse> getMyProfile(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            StudentProfileResponse response = studentService.getStudentProfileByEmail(authentication.getName());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<StudentDashboardResponse> getDashboard(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            return ResponseEntity.ok(studentService.getStudentDashboardByEmail(authentication.getName()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/public/verify")
    public ResponseEntity<PublicCertificateVerificationResponse> verifyCertificate(
            @RequestParam String studentId,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String dob) {
        PublicCertificateVerificationResponse response = studentService.verifyCertificatePublic(studentId, surname, dob);
        if (response.isValid()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/public/certificate/download")
    public ResponseEntity<byte[]> downloadCertificate(
            @RequestParam String studentId,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String dob) throws IOException {
        byte[] content;
        try {
            content = studentService.generateCertificatePdfPublic(studentId, surname, dob);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage().getBytes());
        }
        String fileName = studentId.toUpperCase() + "-certificate.pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(content);
    }
}
