package com.digiqual.config;

import com.digiqual.entity.*;
import com.digiqual.repository.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
public class DataInitializer {

    private final String runSeed = Long.toString(System.currentTimeMillis(), 36).toUpperCase(Locale.ROOT);

    @Bean
    @ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
    public CommandLineRunner initializeData(UserRepository userRepository,
                                            PasswordEncoder passwordEncoder,
                                            PartnerInstituteRepository partnerInstituteRepository,
                                            PartnerBatchRepository partnerBatchRepository,
                                            StudentEnrollmentRepository studentEnrollmentRepository,
                                            CertificateRecordRepository certificateRecordRepository,
                                            LearningModuleProgressRepository learningModuleProgressRepository,
                                            StudentTimelineEventRepository studentTimelineEventRepository) {
        return args -> {
            User adminUser = getOrCreateUser(userRepository, passwordEncoder,
                    "admin@digiqual.com", "Admin@123", "Super Admin", User.UserRole.ADMIN);
            User partnerUser = getOrCreateUser(userRepository, passwordEncoder,
                    "partner@digiqual.com", "Partner@123", "Partner Institute", User.UserRole.PARTNER);
            User studentUser = getOrCreateUser(userRepository, passwordEncoder,
                    "student@digiqual.com", "Student@123", "Student User", User.UserRole.STUDENT);

            PartnerInstitute partnerInstitute = seedPartnerInstitutes(partnerInstituteRepository, partnerUser);
            List<PartnerBatch> batches = seedBatches(partnerBatchRepository, partnerInstitute);
            StudentEnrollment studentEnrollment = seedEnrollments(studentEnrollmentRepository, partnerInstitute, batches, studentUser);
            seedCertificates(certificateRecordRepository, studentEnrollment);
            seedModulesAndTimeline(learningModuleProgressRepository, studentTimelineEventRepository, studentEnrollment);

            System.out.println("[OK] DIGIQUAL seed data ready for full dashboard flow");
        };
    }

    private User getOrCreateUser(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 String email,
                                 String password,
                                 String fullName,
                                 User.UserRole role) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setFullName(fullName);
            user.setRole(role);
            user.setActive(true);
            user.setEmailVerified(true);
            return userRepository.save(user);
        });
    }

    private PartnerInstitute seedPartnerInstitutes(PartnerInstituteRepository partnerInstituteRepository, User partnerUser) {
        PartnerInstitute primary = partnerInstituteRepository.findByOwnerUser(partnerUser).orElse(null);
        if (primary == null) {
            primary = new PartnerInstitute();
            primary.setName("Northbridge College " + runSeed.substring(0, 4));
            primary.setCountry("UK");
            primary.setStatus("ACTIVE");
            primary.setMonthlySeatLimit(randomBetween(100, 180));
            primary.setOwnerUser(partnerUser);
            primary.setCreatedAt(LocalDateTime.now().minusDays(14));
            primary = partnerInstituteRepository.save(primary);
        }

        String pendingOneName = "Elmsford Training " + runSeed.substring(0, 3);
        if (!partnerInstituteRepository.existsByName(pendingOneName)) {
            PartnerInstitute pendingOne = new PartnerInstitute();
            pendingOne.setName(pendingOneName);
            pendingOne.setCountry("UK");
            pendingOne.setStatus("PENDING");
            pendingOne.setMonthlySeatLimit(randomBetween(60, 120));
            pendingOne.setCreatedAt(LocalDateTime.now().minusDays(3));
            partnerInstituteRepository.save(pendingOne);
        }

        String pendingTwoName = "Westbay Skills " + runSeed.substring(1, 4);
        if (!partnerInstituteRepository.existsByName(pendingTwoName)) {
            PartnerInstitute pendingTwo = new PartnerInstitute();
            pendingTwo.setName(pendingTwoName);
            pendingTwo.setCountry("Ireland");
            pendingTwo.setStatus("PENDING");
            pendingTwo.setMonthlySeatLimit(randomBetween(50, 110));
            pendingTwo.setCreatedAt(LocalDateTime.now().minusDays(2));
            partnerInstituteRepository.save(pendingTwo);
        }

        return primary;
    }

    private List<PartnerBatch> seedBatches(PartnerBatchRepository partnerBatchRepository, PartnerInstitute partnerInstitute) {
        List<PartnerBatch> existing = partnerBatchRepository.findByPartnerInstituteOrderByLastUpdatedDesc(partnerInstitute);
        if (!existing.isEmpty()) {
            return existing;
        }

        PartnerBatch batch1 = new PartnerBatch();
        batch1.setBatchCode("HSC-L3-" + runSeed.substring(0, 3) + "-" + randomBetween(10, 99));
        batch1.setStatus("DRAFT");
        batch1.setStudentCount(randomBetween(14, 24));
        batch1.setLastUpdated(LocalDateTime.now().minusMinutes(5));
        batch1.setPartnerInstitute(partnerInstitute);

        PartnerBatch batch2 = new PartnerBatch();
        batch2.setBatchCode("BUS-L2-" + runSeed.substring(1, 4) + "-" + randomBetween(10, 99));
        batch2.setStatus("SUBMITTED");
        batch2.setStudentCount(randomBetween(18, 30));
        batch2.setLastUpdated(LocalDateTime.now().minusHours(1));
        batch2.setPartnerInstitute(partnerInstitute);

        PartnerBatch batch3 = new PartnerBatch();
        batch3.setBatchCode("ITS-L3-" + runSeed.substring(2, 5) + "-" + randomBetween(10, 99));
        batch3.setStatus("APPROVED");
        batch3.setStudentCount(randomBetween(16, 28));
        batch3.setLastUpdated(LocalDateTime.now().minusDays(1));
        batch3.setPartnerInstitute(partnerInstitute);

        return partnerBatchRepository.saveAll(List.of(batch1, batch2, batch3));
    }

    private StudentEnrollment seedEnrollments(StudentEnrollmentRepository studentEnrollmentRepository,
                                              PartnerInstitute partnerInstitute,
                                              List<PartnerBatch> batches,
                                              User studentUser) {
        StudentEnrollment linkedStudent = studentEnrollmentRepository.findByUserAccount(studentUser).orElse(null);
        if (linkedStudent != null) {
            return linkedStudent;
        }

        PartnerBatch mainBatch = batches.get(0);

        StudentEnrollment student = new StudentEnrollment();
        student.setFullName("Student User");
        student.setEmail("student@digiqual.com");
        student.setCourseName(pickCourse());
        student.setState("In review");
        student.setCompletionStatus("Active");
        student.setCertificateStatus("Pending");
        student.setCertificateId(nextCertificateCode());
        student.setDateOfBirth(randomDateOfBirth());
        student.setPartnerInstitute(partnerInstitute);
        student.setPartnerBatch(mainBatch);
        student.setUserAccount(studentUser);
        student.setCreatedAt(LocalDateTime.now().minusDays(10));
        student.setUpdatedAt(LocalDateTime.now().minusHours(3));
        StudentEnrollment savedStudent = studentEnrollmentRepository.save(student);

        StudentEnrollment e2 = new StudentEnrollment();
        e2.setFullName("Leah Bennett " + runSeed.substring(0, 2));
        e2.setEmail("leah." + runSeed.toLowerCase(Locale.ROOT) + "@example.com");
        e2.setCourseName(pickCourse());
        e2.setState(pick("In review", "Awaiting docs"));
        e2.setCompletionStatus("Active");
        e2.setCertificateStatus("Pending");
        e2.setCertificateId(nextCertificateCode());
        e2.setDateOfBirth(randomDateOfBirth());
        e2.setPartnerInstitute(partnerInstitute);
        e2.setPartnerBatch(batches.get(1));
        e2.setCreatedAt(LocalDateTime.now().minusDays(8));
        e2.setUpdatedAt(LocalDateTime.now().minusHours(2));

        StudentEnrollment e3 = new StudentEnrollment();
        e3.setFullName("Nirav Shah " + runSeed.substring(1, 3));
        e3.setEmail("nirav." + runSeed.toLowerCase(Locale.ROOT) + "@example.com");
        e3.setCourseName(pickCourse());
        e3.setState("Awaiting docs");
        e3.setCompletionStatus("Active");
        e3.setCertificateStatus("Pending");
        e3.setCertificateId(nextCertificateCode());
        e3.setDateOfBirth(randomDateOfBirth());
        e3.setPartnerInstitute(partnerInstitute);
        e3.setPartnerBatch(batches.get(1));
        e3.setCreatedAt(LocalDateTime.now().minusDays(7));
        e3.setUpdatedAt(LocalDateTime.now().minusHours(6));

        StudentEnrollment e4 = new StudentEnrollment();
        e4.setFullName("Marta Silva " + runSeed.substring(2, 4));
        e4.setEmail("marta." + runSeed.toLowerCase(Locale.ROOT) + "@example.com");
        e4.setCourseName(pickCourse());
        e4.setState("Approved");
        e4.setCompletionStatus("Completed");
        e4.setCertificateStatus("Issued");
        e4.setCertificateId(nextCertificateCode());
        e4.setDateOfBirth(randomDateOfBirth());
        e4.setPartnerInstitute(partnerInstitute);
        e4.setPartnerBatch(batches.get(2));
        e4.setCreatedAt(LocalDateTime.now().minusDays(15));
        e4.setUpdatedAt(LocalDateTime.now().minusDays(1));

        studentEnrollmentRepository.saveAll(List.of(e2, e3, e4));
        return savedStudent;
    }

    private void seedCertificates(CertificateRecordRepository certificateRecordRepository, StudentEnrollment enrollment) {
        if (!certificateRecordRepository.findTop8ByOrderByIdDesc().isEmpty()) {
            return;
        }

        CertificateRecord c1 = new CertificateRecord();
        c1.setCertificateCode(nextCertificateCode());
        c1.setStudentName("Leah Bennett");
        c1.setCourseName(pickCourse());
        c1.setStage("Ready to issue");
        c1.setIssuedAt(LocalDateTime.now().minusDays(2));
        c1.setStudentEnrollment(enrollment);

        CertificateRecord c2 = new CertificateRecord();
        c2.setCertificateCode(nextCertificateCode());
        c2.setStudentName("Nirav Shah");
        c2.setCourseName(pickCourse());
        c2.setStage("Signature pending");
        c2.setIssuedAt(LocalDateTime.now().minusDays(1));
        c2.setStudentEnrollment(enrollment);

        CertificateRecord c3 = new CertificateRecord();
        c3.setCertificateCode(nextCertificateCode());
        c3.setStudentName("Student User");
        c3.setCourseName(pickCourse());
        c3.setStage(pick("QA review", "Ready to issue", "Signature pending"));
        c3.setIssuedAt(LocalDateTime.now());
        c3.setStudentEnrollment(enrollment);

        certificateRecordRepository.saveAll(List.of(c1, c2, c3));
    }

    private void seedModulesAndTimeline(LearningModuleProgressRepository learningModuleProgressRepository,
                                        StudentTimelineEventRepository studentTimelineEventRepository,
                                        StudentEnrollment enrollment) {
        if (learningModuleProgressRepository.findByStudentEnrollmentOrderByDisplayOrderAsc(enrollment).isEmpty()) {
            LearningModuleProgress m1 = new LearningModuleProgress();
            m1.setStudentEnrollment(enrollment);
            m1.setModuleName("Orientation & Platform Basics");
            m1.setProgressPercent(100);
            m1.setState("Completed");
            m1.setDisplayOrder(1);

            LearningModuleProgress m2 = new LearningModuleProgress();
            m2.setStudentEnrollment(enrollment);
            m2.setModuleName("Core Unit 1 Assessment Prep");
            m2.setProgressPercent(70);
            m2.setState("In progress");
            m2.setDisplayOrder(2);

            LearningModuleProgress m3 = new LearningModuleProgress();
            m3.setStudentEnrollment(enrollment);
            m3.setModuleName("Core Unit 2 Submission Pack");
            m3.setProgressPercent(30);
            m3.setState("In progress");
            m3.setDisplayOrder(3);

            learningModuleProgressRepository.saveAll(List.of(m1, m2, m3));
        }

        if (studentTimelineEventRepository.findByStudentEnrollmentOrderByDisplayOrderAsc(enrollment).isEmpty()) {
            StudentTimelineEvent t1 = new StudentTimelineEvent();
            t1.setStudentEnrollment(enrollment);
            t1.setDescription("Enrolled and verified by partner institute.");
            t1.setDisplayOrder(1);

            StudentTimelineEvent t2 = new StudentTimelineEvent();
            t2.setStudentEnrollment(enrollment);
            t2.setDescription("Record approved by DIGIQUAL admin team.");
            t2.setDisplayOrder(2);

            StudentTimelineEvent t3 = new StudentTimelineEvent();
            t3.setStudentEnrollment(enrollment);
            t3.setDescription("Course modules unlocked in student portal.");
            t3.setDisplayOrder(3);

            StudentTimelineEvent t4 = new StudentTimelineEvent();
            t4.setStudentEnrollment(enrollment);
            t4.setDescription("Certificate released after completion status is confirmed.");
            t4.setDisplayOrder(4);

            studentTimelineEventRepository.saveAll(List.of(t1, t2, t3, t4));
        }
    }

    private int randomBetween(int minInclusive, int maxInclusive) {
        return ThreadLocalRandom.current().nextInt(minInclusive, maxInclusive + 1);
    }

    private String nextCertificateCode() {
        return "DQL-CERT-" + randomBetween(10000, 99999);
    }

    private String pickCourse() {
        return pick(
                "Level 3 Award in Professional Skills",
                "L3 Health & Social Care",
                "L2 Business Admin",
                "L3 IT Support"
        );
    }

    private String pick(String... values) {
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }

    private LocalDate randomDateOfBirth() {
        int year = randomBetween(1996, 2004);
        int month = randomBetween(1, 12);
        int day = randomBetween(1, 28);
        return LocalDate.of(year, month, day);
    }
}
