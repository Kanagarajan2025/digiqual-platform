package com.digiqual.dto;

import java.time.LocalDateTime;

public class StudentProfileResponse {

    private String fullName;
    private String email;
    private String studentId;
    private String status;
    private String courseName;
    private String certificateId;
    private String certificateStatus;
    private LocalDateTime approvedAt;
    private LocalDateTime lastLogin;

    public StudentProfileResponse() {
    }

    public StudentProfileResponse(String fullName, String email, String studentId, String status, String courseName,
                                  String certificateId, String certificateStatus, LocalDateTime approvedAt,
                                  LocalDateTime lastLogin) {
        this.fullName = fullName;
        this.email = email;
        this.studentId = studentId;
        this.status = status;
        this.courseName = courseName;
        this.certificateId = certificateId;
        this.certificateStatus = certificateStatus;
        this.approvedAt = approvedAt;
        this.lastLogin = lastLogin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getCertificateStatus() {
        return certificateStatus;
    }

    public void setCertificateStatus(String certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}