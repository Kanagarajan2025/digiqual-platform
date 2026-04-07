package com.digiqual.dto;

public class DashboardUiItem {

    private String title;
    private String description;
    private String iconKey;

    public DashboardUiItem() {
    }

    public DashboardUiItem(String title, String description, String iconKey) {
        this.title = title;
        this.description = description;
        this.iconKey = iconKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconKey() {
        return iconKey;
    }

    public void setIconKey(String iconKey) {
        this.iconKey = iconKey;
    }
}