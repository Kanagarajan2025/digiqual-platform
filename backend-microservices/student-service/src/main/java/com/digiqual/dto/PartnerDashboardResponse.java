package com.digiqual.dto;

import java.util.ArrayList;
import java.util.List;

public class PartnerDashboardResponse {

    private List<AdminDashboardResponse.StatCard> stats = new ArrayList<>();
    private List<DashboardUiItem> quickActions = new ArrayList<>();
    private List<DashboardUiItem> highlights = new ArrayList<>();
    private List<BatchPipelineItem> batchPipeline = new ArrayList<>();
    private List<EnrollmentItem> recentEnrollments = new ArrayList<>();
    private List<String> checklist = new ArrayList<>();

    public List<AdminDashboardResponse.StatCard> getStats() {
        return stats;
    }

    public void setStats(List<AdminDashboardResponse.StatCard> stats) {
        this.stats = stats;
    }

    public List<DashboardUiItem> getQuickActions() {
        return quickActions;
    }

    public void setQuickActions(List<DashboardUiItem> quickActions) {
        this.quickActions = quickActions;
    }

    public List<DashboardUiItem> getHighlights() {
        return highlights;
    }

    public void setHighlights(List<DashboardUiItem> highlights) {
        this.highlights = highlights;
    }

    public List<BatchPipelineItem> getBatchPipeline() {
        return batchPipeline;
    }

    public void setBatchPipeline(List<BatchPipelineItem> batchPipeline) {
        this.batchPipeline = batchPipeline;
    }

    public List<EnrollmentItem> getRecentEnrollments() {
        return recentEnrollments;
    }

    public void setRecentEnrollments(List<EnrollmentItem> recentEnrollments) {
        this.recentEnrollments = recentEnrollments;
    }

    public List<String> getChecklist() {
        return checklist;
    }

    public void setChecklist(List<String> checklist) {
        this.checklist = checklist;
    }

    public static class BatchPipelineItem {
        private Long id;
        private String batch;
        private Integer students;
        private String status;
        private String lastUpdate;
        private String reviewNote;

        public BatchPipelineItem() {
        }

        public BatchPipelineItem(Long id, String batch, Integer students, String status, String lastUpdate, String reviewNote) {
            this.id = id;
            this.batch = batch;
            this.students = students;
            this.status = status;
            this.lastUpdate = lastUpdate;
            this.reviewNote = reviewNote;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getBatch() {
            return batch;
        }

        public void setBatch(String batch) {
            this.batch = batch;
        }

        public Integer getStudents() {
            return students;
        }

        public void setStudents(Integer students) {
            this.students = students;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(String lastUpdate) {
            this.lastUpdate = lastUpdate;
        }

        public String getReviewNote() {
            return reviewNote;
        }

        public void setReviewNote(String reviewNote) {
            this.reviewNote = reviewNote;
        }
    }

    public static class EnrollmentItem {
        private Long id;
        private String name;
        private String email;
        private String course;
        private String state;
        private Long batchId;

        public EnrollmentItem() {
        }

        public EnrollmentItem(Long id, String name, String email, String course, String state, Long batchId) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.course = course;
            this.state = state;
            this.batchId = batchId;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Long getBatchId() {
            return batchId;
        }

        public void setBatchId(Long batchId) {
            this.batchId = batchId;
        }
    }
}
