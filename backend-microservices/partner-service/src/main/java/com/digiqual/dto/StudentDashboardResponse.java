package com.digiqual.dto;

import java.util.ArrayList;
import java.util.List;

public class StudentDashboardResponse {

    private StudentProfileResponse profile;
    private List<AdminDashboardResponse.StatCard> stats = new ArrayList<>();
    private List<DashboardUiItem> quickActions = new ArrayList<>();
    private List<DashboardUiItem> highlights = new ArrayList<>();
    private List<LearningModuleItem> learningModules = new ArrayList<>();
    private List<String> timeline = new ArrayList<>();

    public StudentProfileResponse getProfile() {
        return profile;
    }

    public void setProfile(StudentProfileResponse profile) {
        this.profile = profile;
    }

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

    public List<LearningModuleItem> getLearningModules() {
        return learningModules;
    }

    public void setLearningModules(List<LearningModuleItem> learningModules) {
        this.learningModules = learningModules;
    }

    public List<String> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<String> timeline) {
        this.timeline = timeline;
    }

    public static class LearningModuleItem {
        private String name;
        private String progress;
        private String state;

        public LearningModuleItem() {
        }

        public LearningModuleItem(String name, String progress, String state) {
            this.name = name;
            this.progress = progress;
            this.state = state;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProgress() {
            return progress;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
