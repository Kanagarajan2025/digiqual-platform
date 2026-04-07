package com.digiqual.repository;

import com.digiqual.entity.StudentEnrollment;
import com.digiqual.entity.StudentTimelineEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentTimelineEventRepository extends JpaRepository<StudentTimelineEvent, Long> {

    List<StudentTimelineEvent> findByStudentEnrollmentOrderByDisplayOrderAsc(StudentEnrollment studentEnrollment);
}
