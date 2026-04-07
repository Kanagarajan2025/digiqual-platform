package com.digiqual.service;

import com.digiqual.dto.AdminDashboardResponse;
import com.digiqual.dto.CreateBatchRequest;
import com.digiqual.dto.CreateEnrollmentRequest;
import com.digiqual.dto.DashboardUiItem;
import com.digiqual.dto.PartnerDashboardResponse;
import com.digiqual.dto.UpdateEnrollmentRequest;
import com.digiqual.entity.CertificateRecord;
import com.digiqual.entity.PartnerBatch;
import com.digiqual.entity.PartnerInstitute;
import com.digiqual.entity.StudentEnrollment;
import com.digiqual.entity.User;
import com.digiqual.repository.CertificateRecordRepository;
import com.digiqual.repository.PartnerBatchRepository;
import com.digiqual.repository.PartnerInstituteRepository;
import com.digiqual.repository.StudentEnrollmentRepository;
import com.digiqual.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final PartnerInstituteRepository partnerInstituteRepository;
    private final PartnerBatchRepository partnerBatchRepository;
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final CertificateRecordRepository certificateRecordRepository;

    public DashboardService(UserRepository userRepository,
                            PartnerInstituteRepository partnerInstituteRepository,
                            PartnerBatchRepository partnerBatchRepository,
                            StudentEnrollmentRepository studentEnrollmentRepository,
                            CertificateRecordRepository certificateRecordRepository) {
        this.userRepository = userRepository;
        this.partnerInstituteRepository = partnerInstituteRepository;
        this.partnerBatchRepository = partnerBatchRepository;
        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.certificateRecordRepository = certificateRecordRepository;
    }

    public AdminDashboardResponse getAdminDashboard(String email) {
        User user = getUserByEmail(email);
        requireRole(user, User.UserRole.ADMIN, "Admin access required");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ROOT);
        long pendingApprovals = partnerInstituteRepository.countByStatus("PENDING")
                + studentEnrollmentRepository.countByStateContainingIgnoreCase("review")
                + partnerBatchRepository.countByStatus("SUBMITTED");
        long activePartners = partnerInstituteRepository.countByStatus("ACTIVE");
        long issuedThisMonth = certificateRecordRepository.countByIssuedAtAfter(
                LocalDate.now().withDayOfMonth(1).atStartOfDay());

        AdminDashboardResponse response = new AdminDashboardResponse();
        response.setStats(List.of(
                new AdminDashboardResponse.StatCard("Pending approvals", String.valueOf(pendingApprovals), "Needs review"),
                new AdminDashboardResponse.StatCard("Active partners", String.valueOf(activePartners), "Across regions"),
                new AdminDashboardResponse.StatCard("Certificates issued", String.valueOf(issuedThisMonth), "This month")
        ));

        response.setQuickActions(List.of(
                new DashboardUiItem("Review partner applications", "Approve, suspend, or reactivate partner institutes and control access to the platform.", "BUILDING2"),
                new DashboardUiItem("Validate student submissions", "Confirm learner records and close approval queues from one screen.", "FILECHECK2"),
                new DashboardUiItem("Issue certificates", "Advance certificates through QA, signature, and issue stages.", "BADGECHECK"),
                new DashboardUiItem("Adjust batch limits", "Monitor institute-level intake and keep delivery capacity aligned.", "SETTINGS2")
        ));

        response.setHighlights(buildAdminHighlights(pendingApprovals, pendingApprovals > 0 ? "Backlog requires action" : "Backlog is clear"));

        response.setPendingPartners(
                partnerInstituteRepository.findByStatusOrderByCreatedAtDesc("PENDING").stream()
                        .map(partner -> new AdminDashboardResponse.QueuePartnerItem(
                                partner.getId(),
                                partner.getName(),
                                partner.getCountry(),
                                partner.getCreatedAt().format(dateFormatter),
                                partner.getStatus()))
                        .collect(Collectors.toList())
        );

        response.setPendingStudents(
                studentEnrollmentRepository.findTop8ByStateContainingIgnoreCaseOrderByUpdatedAtDesc("review").stream()
                        .map(enrollment -> new AdminDashboardResponse.PendingStudentItem(
                                enrollment.getId(),
                                enrollment.getFullName(),
                                enrollment.getCourseName(),
                                enrollment.getPartnerInstitute().getName(),
                                enrollment.getState()))
                        .collect(Collectors.toList())
        );

        response.setSubmittedBatches(
                partnerBatchRepository.findTop8ByStatusOrderByLastUpdatedDesc("SUBMITTED").stream()
                        .map(batch -> new AdminDashboardResponse.BatchReviewItem(
                                batch.getId(),
                                batch.getBatchCode(),
                                batch.getPartnerInstitute().getName(),
                                batch.getStudentCount(),
                                batch.getStatus(),
                                batch.getLastUpdated().format(dateFormatter),
                                batch.getReviewNote()))
                        .collect(Collectors.toList())
        );

        response.setCapacityControls(
                partnerInstituteRepository.findAll().stream()
                        .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                        .map(partner -> new AdminDashboardResponse.CapacityControlItem(partner.getName(), partner.getMonthlySeatLimit()))
                        .collect(Collectors.toList())
        );

        response.setCertificateQueue(
                certificateRecordRepository.findTop8ByOrderByIdDesc().stream()
                        .map(c -> new AdminDashboardResponse.CertificateQueueItem(
                                c.getCertificateCode(),
                                c.getStudentName(),
                                c.getCourseName(),
                                c.getStage()))
                        .collect(Collectors.toList())
        );

        return response;
    }

    public PartnerDashboardResponse getPartnerDashboard(String email) {
        User user = getUserByEmail(email);
        requireRole(user, User.UserRole.PARTNER, "Partner access required");

        PartnerInstitute institute = partnerInstituteRepository.findByOwnerUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Partner institute not found"));

        List<PartnerBatch> batches = partnerBatchRepository.findTop6ByPartnerInstituteOrderByLastUpdatedDesc(institute);
        List<StudentEnrollment> enrollments = studentEnrollmentRepository.findTop8ByPartnerInstituteOrderByUpdatedAtDesc(institute);

        long openBatches = batches.stream().filter(batch -> !"APPROVED".equalsIgnoreCase(batch.getStatus())).count();
        long inReview = studentEnrollmentRepository.countByPartnerInstituteAndStateContainingIgnoreCase(institute, "review");

        PartnerDashboardResponse response = new PartnerDashboardResponse();
        response.setStats(List.of(
                new AdminDashboardResponse.StatCard("Open batches", String.format(Locale.ROOT, "%02d", openBatches), "Draft and active"),
                new AdminDashboardResponse.StatCard("Students in review", String.valueOf(inReview), "Awaiting admin"),
                new AdminDashboardResponse.StatCard("Available seats", String.valueOf(Math.max(0, institute.getMonthlySeatLimit() - enrollments.size())), "Current quota")
        ));

        response.setQuickActions(List.of(
                new DashboardUiItem("Create a new batch", "Start a new cohort and build it to the required student count before submission.", "FOLDERKANBAN"),
                new DashboardUiItem("Enrol students", "Add student details, check records, and prepare a clean submission for approval.", "GRADUATIONCAP"),
                new DashboardUiItem("Submit for admin review", "Send completed batches once minimum capacity is reached and ready for validation.", "SEND"),
                new DashboardUiItem("Request course materials", "Access DIGIQUAL modules and make sure learners have the correct study content.", "BOOKOPENCHECK")
        ));

        response.setBatchPipeline(batches.stream()
                .map(batch -> new PartnerDashboardResponse.BatchPipelineItem(
                        batch.getId(),
                        batch.getBatchCode(),
                        batch.getStudentCount(),
                        batch.getStatus(),
                        relativeTime(batch.getLastUpdated()),
                        batch.getReviewNote()))
                .collect(Collectors.toList()));

        response.setRecentEnrollments(enrollments.stream()
                .map(enrollment -> new PartnerDashboardResponse.EnrollmentItem(
                        enrollment.getId(),
                        enrollment.getFullName(),
                        enrollment.getEmail(),
                        enrollment.getCourseName(),
                        enrollment.getState(),
                        enrollment.getPartnerBatch() == null ? null : enrollment.getPartnerBatch().getId()))
                .collect(Collectors.toList()));

        response.setChecklist(buildPartnerChecklist(batches, enrollments));
                response.setHighlights(buildPartnerHighlights(batches, enrollments));

        return response;
    }

    public void approvePartner(String email, Long partnerId) {
        updatePartnerStatus(email, partnerId, "ACTIVE");
    }

    public void suspendPartner(String email, Long partnerId) {
        updatePartnerStatus(email, partnerId, "SUSPENDED");
    }

    public void updateCertificateStage(String email, String certificateCode, String stage) {
        User user = getUserByEmail(email);
        requireRole(user, User.UserRole.ADMIN, "Admin access required");

        CertificateRecord certificate = certificateRecordRepository.findByCertificateCode(certificateCode)
                .orElseThrow(() -> new IllegalArgumentException("Certificate not found"));
        certificate.setStage(stage.trim());
        certificateRecordRepository.save(certificate);
    }

        public void approveBatch(String email, Long batchId) {
                updateBatchStatus(email, batchId, "APPROVED", null);
        }

        public void returnBatch(String email, Long batchId, String reason) {
                if (reason == null || reason.isBlank()) {
                        throw new IllegalArgumentException("Return reason is required");
                }

                updateBatchStatus(email, batchId, "RETURNED", reason.trim());
        }

        public void approveStudent(String email, Long enrollmentId) {
                updateStudentReviewState(email, enrollmentId, "Approved", "Completed", "Ready to issue");
        }

        public void rejectStudent(String email, Long enrollmentId) {
                updateStudentReviewState(email, enrollmentId, "Rejected", "Action required", "On hold");
        }

    public void createBatch(String email, CreateBatchRequest request) {
        User user = getUserByEmail(email);
        requireRole(user, User.UserRole.PARTNER, "Partner access required");
        PartnerInstitute institute = getPartnerInstituteForUser(user);

        String batchCode = request.getBatchCode().trim();
        if (partnerBatchRepository.existsByBatchCode(batchCode)) {
            throw new IllegalArgumentException("Batch code already exists");
        }

        PartnerBatch batch = new PartnerBatch();
        batch.setBatchCode(batchCode);
        batch.setStudentCount(request.getStudentCount());
        batch.setStatus("DRAFT");
        batch.setLastUpdated(LocalDateTime.now());
        batch.setPartnerInstitute(institute);
        partnerBatchRepository.save(batch);
    }

    public void submitBatch(String email, Long batchId) {
        User user = getUserByEmail(email);
        requireRole(user, User.UserRole.PARTNER, "Partner access required");
        PartnerInstitute institute = getPartnerInstituteForUser(user);

        PartnerBatch batch = partnerBatchRepository.findByIdAndPartnerInstitute(batchId, institute)
                .orElseThrow(() -> new IllegalArgumentException("Batch not found"));

        if ("APPROVED".equalsIgnoreCase(batch.getStatus())) {
            throw new IllegalArgumentException("Approved batch cannot be resubmitted");
        }

        batch.setStatus("SUBMITTED");
                batch.setReviewNote(null);
        batch.setLastUpdated(LocalDateTime.now());
        partnerBatchRepository.save(batch);
    }

    public void createEnrollment(String email, CreateEnrollmentRequest request) {
        User user = getUserByEmail(email);
        requireRole(user, User.UserRole.PARTNER, "Partner access required");
        PartnerInstitute institute = getPartnerInstituteForUser(user);

        PartnerBatch batch = partnerBatchRepository.findByIdAndPartnerInstitute(request.getBatchId(), institute)
                .orElseThrow(() -> new IllegalArgumentException("Batch not found"));

        StudentEnrollment enrollment = new StudentEnrollment();
        enrollment.setFullName(request.getFullName().trim());
        enrollment.setEmail(request.getEmail().trim().toLowerCase(Locale.ROOT));
        enrollment.setCourseName(request.getCourseName().trim());
        enrollment.setState("In review");
        enrollment.setCompletionStatus(resolveCompletionStatus(request.getCompletionStatus()));
        enrollment.setCertificateStatus("Pending");
        enrollment.setCertificateId("DQL-CERT-" + (10000 + (int) (Math.random() * 89999)));
        enrollment.setPartnerInstitute(institute);
        enrollment.setPartnerBatch(batch);
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollment.setUpdatedAt(LocalDateTime.now());
        studentEnrollmentRepository.save(enrollment);

        batch.setStudentCount(batch.getStudentCount() + 1);
        batch.setLastUpdated(LocalDateTime.now());
        partnerBatchRepository.save(batch);
    }

        public void deleteBatch(String email, Long batchId) {
                User user = getUserByEmail(email);
                requireRole(user, User.UserRole.PARTNER, "Partner access required");
                PartnerInstitute institute = getPartnerInstituteForUser(user);

                PartnerBatch batch = partnerBatchRepository.findByIdAndPartnerInstitute(batchId, institute)
                                .orElseThrow(() -> new IllegalArgumentException("Batch not found"));

                if (studentEnrollmentRepository.countByPartnerBatchId(batchId) > 0) {
                        throw new IllegalArgumentException("Batch with learners cannot be deleted");
                }

                partnerBatchRepository.delete(batch);
        }

        public void updateEnrollment(String email, Long enrollmentId, UpdateEnrollmentRequest request) {
                User user = getUserByEmail(email);
                requireRole(user, User.UserRole.PARTNER, "Partner access required");
                PartnerInstitute institute = getPartnerInstituteForUser(user);

                StudentEnrollment enrollment = studentEnrollmentRepository.findByIdAndPartnerInstitute(enrollmentId, institute)
                                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));

                enrollment.setFullName(request.getFullName().trim());
                enrollment.setEmail(request.getEmail().trim().toLowerCase(Locale.ROOT));
                enrollment.setCourseName(request.getCourseName().trim());
                enrollment.setCompletionStatus(resolveCompletionStatus(request.getCompletionStatus()));
                enrollment.setUpdatedAt(LocalDateTime.now());
                studentEnrollmentRepository.save(enrollment);
        }

        public void deleteEnrollment(String email, Long enrollmentId) {
                User user = getUserByEmail(email);
                requireRole(user, User.UserRole.PARTNER, "Partner access required");
                PartnerInstitute institute = getPartnerInstituteForUser(user);

                StudentEnrollment enrollment = studentEnrollmentRepository.findByIdAndPartnerInstitute(enrollmentId, institute)
                                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));

                PartnerBatch batch = enrollment.getPartnerBatch();
                studentEnrollmentRepository.delete(enrollment);

                if (batch != null) {
                        batch.setStudentCount(Math.max(0, batch.getStudentCount() - 1));
                        batch.setLastUpdated(LocalDateTime.now());
                        partnerBatchRepository.save(batch);
                }
        }

    private void updatePartnerStatus(String email, Long partnerId, String status) {
        User user = getUserByEmail(email);
        requireRole(user, User.UserRole.ADMIN, "Admin access required");

        PartnerInstitute institute = partnerInstituteRepository.findById(partnerId)
                .orElseThrow(() -> new IllegalArgumentException("Partner institute not found"));
        institute.setStatus(status);
        partnerInstituteRepository.save(institute);
    }

        private void updateBatchStatus(String email, Long batchId, String status, String reviewNote) {
                User user = getUserByEmail(email);
                requireRole(user, User.UserRole.ADMIN, "Admin access required");

                PartnerBatch batch = partnerBatchRepository.findById(batchId)
                                .orElseThrow(() -> new IllegalArgumentException("Batch not found"));
                batch.setStatus(status);
                batch.setReviewNote(reviewNote);
                batch.setLastUpdated(LocalDateTime.now());
                partnerBatchRepository.save(batch);
        }

        private void updateStudentReviewState(String email,
                                                                                  Long enrollmentId,
                                                                                  String state,
                                                                                  String completionStatus,
                                                                                  String certificateStatus) {
                User user = getUserByEmail(email);
                requireRole(user, User.UserRole.ADMIN, "Admin access required");

                StudentEnrollment enrollment = studentEnrollmentRepository.findById(enrollmentId)
                                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));
                enrollment.setState(state);
                enrollment.setCompletionStatus(completionStatus);
                enrollment.setCertificateStatus(certificateStatus);
                enrollment.setUpdatedAt(LocalDateTime.now());
                studentEnrollmentRepository.save(enrollment);
        }

        private String resolveCompletionStatus(String value) {
                if (value == null || value.isBlank()) {
                        return "Active";
                }
                return value.trim();
        }

    private PartnerInstitute getPartnerInstituteForUser(User user) {
        return partnerInstituteRepository.findByOwnerUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Partner institute not found"));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private void requireRole(User user, User.UserRole role, String message) {
        if (user.getRole() != role) {
            throw new IllegalArgumentException(message);
        }
    }

    private String relativeTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "Unknown";
        }
        LocalDateTime now = LocalDateTime.now();
        long minutes = Duration.between(dateTime, now).toMinutes();
        if (minutes < 60) {
            return Math.max(1, minutes) + " min ago";
        }
        long hours = Duration.between(dateTime, now).toHours();
        if (hours < 24) {
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        }
        return "Yesterday";
    }

        private List<String> buildPartnerChecklist(List<PartnerBatch> batches, List<StudentEnrollment> enrollments) {
                List<String> checklist = new ArrayList<>();

                boolean hasReturnedBatch = batches.stream()
                                .anyMatch(batch -> "RETURNED".equalsIgnoreCase(batch.getStatus()));
                if (hasReturnedBatch) {
                        checklist.add("Resolve admin return notes before re-submitting affected batches.");
                }

                boolean hasSmallSubmittedBatch = batches.stream()
                                .anyMatch(batch -> "SUBMITTED".equalsIgnoreCase(batch.getStatus()) && batch.getStudentCount() < 10);
                if (hasSmallSubmittedBatch) {
                        checklist.add("One or more submitted batches are below recommended size (10 learners).");
                }

                long reviewCount = enrollments.stream()
                                .filter(enrollment -> enrollment.getState() != null && enrollment.getState().toLowerCase(Locale.ROOT).contains("review"))
                                .count();
                if (reviewCount > 0) {
                        checklist.add("" + reviewCount + " learner records are pending admin review.");
                }

                long missingEmailCount = enrollments.stream()
                                .filter(enrollment -> enrollment.getEmail() == null || enrollment.getEmail().isBlank())
                                .count();
                if (missingEmailCount > 0) {
                        checklist.add("" + missingEmailCount + " learner records are missing email details.");
                }

                if (checklist.isEmpty()) {
                        checklist.add("No operational blockers detected. Batches are ready for progression.");
                }

                return checklist;
        }

        private List<DashboardUiItem> buildAdminHighlights(long pendingApprovals, String complianceMessage) {
                List<DashboardUiItem> items = new ArrayList<>();
                items.add(new DashboardUiItem("Pending approvals", pendingApprovals + " items awaiting action", "CLOCK3"));
                items.add(new DashboardUiItem("Certificate queue", "Keep QA and signature stages moving to avoid delays", "BADGECHECK"));
                items.add(new DashboardUiItem("Compliance", complianceMessage, "SHIELDCHECK"));
                return items;
        }

        private List<DashboardUiItem> buildPartnerHighlights(List<PartnerBatch> batches, List<StudentEnrollment> enrollments) {
                long submittedBatches = batches.stream()
                                .filter(batch -> "SUBMITTED".equalsIgnoreCase(batch.getStatus()))
                                .count();
                long returnedBatches = batches.stream()
                                .filter(batch -> "RETURNED".equalsIgnoreCase(batch.getStatus()))
                                .count();
                long inReview = enrollments.stream()
                                .filter(enrollment -> enrollment.getState() != null && enrollment.getState().toLowerCase(Locale.ROOT).contains("review"))
                                .count();

                List<DashboardUiItem> items = new ArrayList<>();
                items.add(new DashboardUiItem("Submitted batches", submittedBatches + " currently with admin team", "SEND"));
                items.add(new DashboardUiItem("Returned batches", returnedBatches + " require updates before re-submission", "ALERTTRIANGLE"));
                items.add(new DashboardUiItem("Learners in review", inReview + " learner records pending decision", "USERS"));
                return items;
        }
}
