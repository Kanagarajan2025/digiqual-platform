package com.digiqual.service;

import com.digiqual.dto.AdminDashboardResponse;
import com.digiqual.dto.DashboardUiItem;
import com.digiqual.dto.StudentDashboardResponse;
import com.digiqual.dto.StudentProfileResponse;
import com.digiqual.entity.LearningModuleProgress;
import com.digiqual.entity.StudentEnrollment;
import com.digiqual.entity.StudentTimelineEvent;
import com.digiqual.entity.User;
import com.digiqual.repository.LearningModuleProgressRepository;
import com.digiqual.repository.StudentEnrollmentRepository;
import com.digiqual.repository.StudentTimelineEventRepository;
import com.digiqual.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final UserRepository userRepository;
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final LearningModuleProgressRepository learningModuleProgressRepository;
    private final StudentTimelineEventRepository studentTimelineEventRepository;

    public StudentService(UserRepository userRepository,
                          StudentEnrollmentRepository studentEnrollmentRepository,
                          LearningModuleProgressRepository learningModuleProgressRepository,
                          StudentTimelineEventRepository studentTimelineEventRepository) {
        this.userRepository = userRepository;
        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.learningModuleProgressRepository = learningModuleProgressRepository;
        this.studentTimelineEventRepository = studentTimelineEventRepository;
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
}
