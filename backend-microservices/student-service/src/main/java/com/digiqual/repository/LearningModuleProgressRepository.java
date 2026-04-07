package com.digiqual.repository;

import com.digiqual.entity.LearningModuleProgress;
import com.digiqual.entity.StudentEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LearningModuleProgressRepository extends JpaRepository<LearningModuleProgress, Long> {

    List<LearningModuleProgress> findByStudentEnrollmentOrderByDisplayOrderAsc(StudentEnrollment studentEnrollment);
}
