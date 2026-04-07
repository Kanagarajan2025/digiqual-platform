package com.digiqual.dto;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardResponse {

    private List<StatCard> stats = new ArrayList<>();
    private List<DashboardUiItem> quickActions = new ArrayList<>();
    private List<DashboardUiItem> highlights = new ArrayList<>();
    private List<QueuePartnerItem> pendingPartners = new ArrayList<>();
    private List<PendingStudentItem> pendingStudents = new ArrayList<>();
    private List<BatchReviewItem> submittedBatches = new ArrayList<>();
    private List<CapacityControlItem> capacityControls = new ArrayList<>();
    private List<CertificateQueueItem> certificateQueue = new ArrayList<>();

    public List<StatCard> getStats() {
        return stats;
    }

    public void setStats(List<StatCard> stats) {
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

    public List<QueuePartnerItem> getPendingPartners() {
        return pendingPartners;
    }

    public void setPendingPartners(List<QueuePartnerItem> pendingPartners) {
        this.pendingPartners = pendingPartners;
    }

    public List<PendingStudentItem> getPendingStudents() {
        return pendingStudents;
    }

    public void setPendingStudents(List<PendingStudentItem> pendingStudents) {
        this.pendingStudents = pendingStudents;
    }

    public List<BatchReviewItem> getSubmittedBatches() {
        return submittedBatches;
    }

    public void setSubmittedBatches(List<BatchReviewItem> submittedBatches) {
        this.submittedBatches = submittedBatches;
    }

    public List<CapacityControlItem> getCapacityControls() {
        return capacityControls;
    }

    public void setCapacityControls(List<CapacityControlItem> capacityControls) {
        this.capacityControls = capacityControls;
    }

    public List<CertificateQueueItem> getCertificateQueue() {
        return certificateQueue;
    }

    public void setCertificateQueue(List<CertificateQueueItem> certificateQueue) {
        this.certificateQueue = certificateQueue;
    }

    public static class StatCard {
        private String label;
        private String value;
        private String meta;

        public StatCard() {
        }

        public StatCard(String label, String value, String meta) {
            this.label = label;
            this.value = value;
            this.meta = meta;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getMeta() {
            return meta;
        }

        public void setMeta(String meta) {
            this.meta = meta;
        }
    }

    public static class QueuePartnerItem {
        private Long id;
        private String institute;
        private String country;
        private String submitted;
        private String status;

        public QueuePartnerItem() {
        }

        public QueuePartnerItem(Long id, String institute, String country, String submitted, String status) {
            this.id = id;
            this.institute = institute;
            this.country = country;
            this.submitted = submitted;
            this.status = status;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getInstitute() {
            return institute;
        }

        public void setInstitute(String institute) {
            this.institute = institute;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getSubmitted() {
            return submitted;
        }

        public void setSubmitted(String submitted) {
            this.submitted = submitted;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class CapacityControlItem {
        private String institute;
        private Integer seats;

        public CapacityControlItem() {
        }

        public CapacityControlItem(String institute, Integer seats) {
            this.institute = institute;
            this.seats = seats;
        }

        public String getInstitute() {
            return institute;
        }

        public void setInstitute(String institute) {
            this.institute = institute;
        }

        public Integer getSeats() {
            return seats;
        }

        public void setSeats(Integer seats) {
            this.seats = seats;
        }
    }

    public static class BatchReviewItem {
        private Long id;
        private String batchCode;
        private String institute;
        private Integer studentCount;
        private String status;
        private String updatedAt;
        private String reviewNote;

        public BatchReviewItem() {
        }

        public BatchReviewItem(Long id, String batchCode, String institute, Integer studentCount, String status, String updatedAt, String reviewNote) {
            this.id = id;
            this.batchCode = batchCode;
            this.institute = institute;
            this.studentCount = studentCount;
            this.status = status;
            this.updatedAt = updatedAt;
            this.reviewNote = reviewNote;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getBatchCode() {
            return batchCode;
        }

        public void setBatchCode(String batchCode) {
            this.batchCode = batchCode;
        }

        public String getInstitute() {
            return institute;
        }

        public void setInstitute(String institute) {
            this.institute = institute;
        }

        public Integer getStudentCount() {
            return studentCount;
        }

        public void setStudentCount(Integer studentCount) {
            this.studentCount = studentCount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getReviewNote() {
            return reviewNote;
        }

        public void setReviewNote(String reviewNote) {
            this.reviewNote = reviewNote;
        }
    }

    public static class PendingStudentItem {
        private Long id;
        private String name;
        private String course;
        private String partner;
        private String state;

        public PendingStudentItem() {
        }

        public PendingStudentItem(Long id, String name, String course, String partner, String state) {
            this.id = id;
            this.name = name;
            this.course = course;
            this.partner = partner;
            this.state = state;
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

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getPartner() {
            return partner;
        }

        public void setPartner(String partner) {
            this.partner = partner;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    public static class CertificateQueueItem {
        private String certificateId;
        private String student;
        private String course;
        private String stage;

        public CertificateQueueItem() {
        }

        public CertificateQueueItem(String certificateId, String student, String course, String stage) {
            this.certificateId = certificateId;
            this.student = student;
            this.course = course;
            this.stage = stage;
        }

        public String getCertificateId() {
            return certificateId;
        }

        public void setCertificateId(String certificateId) {
            this.certificateId = certificateId;
        }

        public String getStudent() {
            return student;
        }

        public void setStudent(String student) {
            this.student = student;
        }

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getStage() {
            return stage;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }
    }
}
