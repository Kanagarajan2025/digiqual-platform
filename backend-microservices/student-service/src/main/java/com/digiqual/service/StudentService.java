package com.digiqual.service;

import com.digiqual.dto.AdminDashboardResponse;
import com.digiqual.dto.DashboardUiItem;
import com.digiqual.dto.PublicCertificateVerificationResponse;
import com.digiqual.dto.StudentDashboardResponse;
import com.digiqual.dto.StudentProfileResponse;
import com.digiqual.entity.CertificateRecord;
import com.digiqual.entity.LearningModuleProgress;
import com.digiqual.entity.StudentEnrollment;
import com.digiqual.entity.StudentTimelineEvent;
import com.digiqual.entity.User;
import com.digiqual.repository.CertificateRecordRepository;
import com.digiqual.repository.LearningModuleProgressRepository;
import com.digiqual.repository.StudentEnrollmentRepository;
import com.digiqual.repository.StudentTimelineEventRepository;
import com.digiqual.repository.UserRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final UserRepository userRepository;
    private final StudentEnrollmentRepository studentEnrollmentRepository;
        private final CertificateRecordRepository certificateRecordRepository;
    private final LearningModuleProgressRepository learningModuleProgressRepository;
    private final StudentTimelineEventRepository studentTimelineEventRepository;

    public StudentService(UserRepository userRepository,
                          StudentEnrollmentRepository studentEnrollmentRepository,
                                                  CertificateRecordRepository certificateRecordRepository,
                          LearningModuleProgressRepository learningModuleProgressRepository,
                          StudentTimelineEventRepository studentTimelineEventRepository) {
        this.userRepository = userRepository;
        this.studentEnrollmentRepository = studentEnrollmentRepository;
                this.certificateRecordRepository = certificateRecordRepository;
        this.learningModuleProgressRepository = learningModuleProgressRepository;
        this.studentTimelineEventRepository = studentTimelineEventRepository;
    }

        public PublicCertificateVerificationResponse verifyCertificatePublic(String studentId, String surname, String dob) {
                String normalizedStudentId = studentId == null ? "" : studentId.trim().toUpperCase(Locale.ROOT);
                String normalizedSurname = surname == null ? "" : surname.trim().toLowerCase(Locale.ROOT);
                String normalizedDob = dob == null ? "" : dob.trim();

                if (normalizedStudentId.isBlank() || (normalizedSurname.isBlank() && normalizedDob.isBlank())) {
                        return invalidResult("Student ID and either surname or DOB are required");
                }

                Long userId = extractUserId(normalizedStudentId);
                if (userId == null) {
                        return invalidResult("Invalid student ID format");
                }

                StudentEnrollment enrollment = studentEnrollmentRepository.findByUserAccountId(userId).orElse(null);
                if (enrollment == null) {
                        return invalidResult("Student record not found");
                }

                String expectedStudentId = buildStudentId(enrollment.getUserAccount());
                if (!expectedStudentId.equalsIgnoreCase(normalizedStudentId)) {
                        return invalidResult("Student record not found");
                }

                boolean identifierMatches = false;

                if (!normalizedSurname.isBlank()) {
                        String name = enrollment.getFullName() == null ? "" : enrollment.getFullName().trim();
                        String[] nameParts = name.split("\\s+");
                        String lastName = nameParts.length == 0 ? "" : nameParts[nameParts.length - 1].toLowerCase(Locale.ROOT);
                        identifierMatches = lastName.equals(normalizedSurname);
                }

                if (!identifierMatches && !normalizedDob.isBlank()) {
                        LocalDate parsedDob = tryParseDob(normalizedDob);
                        identifierMatches = parsedDob != null && enrollment.getDateOfBirth() != null && enrollment.getDateOfBirth().equals(parsedDob);
                }

                if (!identifierMatches) {
                        return invalidResult("Provided surname or DOB does not match");
                }

                CertificateRecord certificate = enrollment.getCertificateId() == null
                                ? null
                                : certificateRecordRepository.findByCertificateCode(enrollment.getCertificateId()).orElse(null);

                PublicCertificateVerificationResponse response = new PublicCertificateVerificationResponse();
                response.setValid(true);
                response.setMessage("Certificate verified");
                response.setStudentId(expectedStudentId);
                response.setStudentName(enrollment.getFullName());
                response.setCourseName(enrollment.getCourseName());
                response.setCertificateId(enrollment.getCertificateId());
                response.setCertificateStatus(enrollment.getCertificateStatus());
                response.setDateOfBirth(enrollment.getDateOfBirth());
                response.setIssuedAt(certificate == null ? null : certificate.getIssuedAt());
                String identifierQuery = !normalizedSurname.isBlank()
                        ? "surname=" + normalizedSurname
                        : "dob=" + normalizedDob;
                response.setDownloadPath(enrollment.getCertificateId() == null
                        ? null
                        : "/student/public/certificate/download?studentId=" + expectedStudentId + "&" + identifierQuery);
                return response;
        }

            public byte[] generateCertificatePdfPublic(String studentId, String surname, String dob) throws IOException {
                PublicCertificateVerificationResponse verification = verifyCertificatePublic(studentId, surname, dob);
                if (!verification.isValid()) {
                        throw new IllegalArgumentException(verification.getMessage());
                }

                if (verification.getCertificateId() == null || verification.getCertificateId().isBlank()) {
                        throw new IllegalArgumentException("Certificate is not issued yet");
                }

                try (PDDocument document = new PDDocument(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                        PDPage page = new PDPage();
                        document.addPage(page);

                        try (PDPageContentStream stream = new PDPageContentStream(document, page)) {
                                stream.beginText();
                                stream.setFont(PDType1Font.HELVETICA_BOLD, 22);
                                stream.newLineAtOffset(72, 720);
                                stream.showText("DIGIQUAL Certificate");

                                stream.setFont(PDType1Font.HELVETICA, 12);
                                stream.newLineAtOffset(0, -38);
                                stream.showText("Student Name: " + safe(verification.getStudentName()));
                                stream.newLineAtOffset(0, -20);
                                stream.showText("Student ID: " + safe(verification.getStudentId()));
                                stream.newLineAtOffset(0, -20);
                                stream.showText("Course: " + safe(verification.getCourseName()));
                                stream.newLineAtOffset(0, -20);
                                stream.showText("Certificate ID: " + safe(verification.getCertificateId()));
                                stream.newLineAtOffset(0, -20);
                                stream.showText("Status: " + safe(verification.getCertificateStatus()));
                                stream.newLineAtOffset(0, -20);
                                stream.showText("Issued By: DIGIQUAL (UK Awarding Body)");
                                stream.endText();
                        }

                        document.save(output);
                        return output.toByteArray();
                }
        }

    public StudentProfileResponse getStudentProfileByEmail(String email) {
        StudentEnrollment enrollment = getStudentEnrollment(email);
        User user = enrollment.getUserAccount();
        String studentId = String.format(Locale.ROOT, "DGQ-%d-%03d", user.getCreatedAt().getYear(), user.getId());

        return new StudentProfileResponse(
                enrollment.getFullName(),
                enrollment.getEmail(),
                studentId,
                enrollment.getCompletionStatus(),
                enrollment.getCourseName(),
                enrollment.getCertificateId() == null ? "Pending" : enrollment.getCertificateId(),
                enrollment.getCertificateStatus(),
                enrollment.getCreatedAt(),
                user.getLastLogin()
        );
    }

    public StudentDashboardResponse getStudentDashboardByEmail(String email) {
        StudentEnrollment enrollment = getStudentEnrollment(email);
        StudentProfileResponse profile = getStudentProfileByEmail(email);

        StudentDashboardResponse response = new StudentDashboardResponse();
        response.setProfile(profile);
        response.setStats(List.of(
                new AdminDashboardResponse.StatCard("Student ID", profile.getStudentId(), "Unique serial"),
                new AdminDashboardResponse.StatCard("Course status", profile.getStatus(), "Admin approved"),
                new AdminDashboardResponse.StatCard("Certificate", profile.getCertificateStatus(), "Visible on completion")
        ));

        response.setQuickActions(List.of(
                new DashboardUiItem("View your profile", "Student account: " + profile.getEmail(), "FINGERPRINT"),
                new DashboardUiItem("Open course materials", "Access the current learning pack and stay aligned with your enrolled course.", "BOOKMARKED"),
                new DashboardUiItem("Track completion status", "Monitor whether your record is active, completed, or awaiting certificate issue.", "USERCHECK"),
                new DashboardUiItem("Download certificate", "Current certificate status: " + profile.getCertificateStatus(), "FILEDOWN")
        ));

        List<StudentDashboardResponse.LearningModuleItem> modules = learningModuleProgressRepository
                .findByStudentEnrollmentOrderByDisplayOrderAsc(enrollment)
                .stream()
                .map(this::toModuleItem)
                .collect(Collectors.toList());
        response.setLearningModules(modules);

        List<String> timeline = studentTimelineEventRepository
                .findByStudentEnrollmentOrderByDisplayOrderAsc(enrollment)
                .stream()
                .map(StudentTimelineEvent::getDescription)
                .collect(Collectors.toList());
        response.setTimeline(timeline);

        response.setHighlights(List.of(
                new DashboardUiItem("Current course", profile.getCourseName(), "GRADUATIONCAP"),
                new DashboardUiItem("Modules", modules.size() + " learning modules available", "BOOKOPENCHECK"),
                new DashboardUiItem("Timeline", timeline.size() + " certification milestones tracked", "CLOCK3")
        ));

        return response;
    }

    private StudentDashboardResponse.LearningModuleItem toModuleItem(LearningModuleProgress module) {
        return new StudentDashboardResponse.LearningModuleItem(
                module.getModuleName(),
                module.getProgressPercent() + "%",
                module.getState()
        );
    }

    private StudentEnrollment getStudentEnrollment(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        if (user.getRole() != User.UserRole.STUDENT) {
            throw new IllegalArgumentException("Profile is not a student account");
        }

        return studentEnrollmentRepository.findByUserAccount(user)
                .orElseThrow(() -> new IllegalArgumentException("Student enrollment record not found"));
    }

        private String buildStudentId(User user) {
                return String.format(Locale.ROOT, "DGQ-%d-%03d", user.getCreatedAt().getYear(), user.getId());
        }

        private Long extractUserId(String studentId) {
                String[] parts = studentId.split("-");
                if (parts.length != 3 || !"DGQ".equalsIgnoreCase(parts[0])) {
                        return null;
                }

                try {
                        return Long.parseLong(parts[2]);
                } catch (NumberFormatException ex) {
                        return null;
                }
        }

        private PublicCertificateVerificationResponse invalidResult(String message) {
                PublicCertificateVerificationResponse response = new PublicCertificateVerificationResponse();
                response.setValid(false);
                response.setMessage(message);
                return response;
        }

        private String safe(String value) {
                return value == null ? "" : value;
        }

        private LocalDate tryParseDob(String dob) {
                try {
                        return LocalDate.parse(dob);
                } catch (Exception ex) {
                        return null;
                }
        }
}
