package com.digiqual.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateCertificateStageRequest {

    @NotBlank(message = "Stage is required")
    private String stage;

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
}
