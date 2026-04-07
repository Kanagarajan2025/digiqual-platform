package com.digiqual.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ReturnBatchRequest {

    @NotBlank(message = "Reason is required")
    @Size(max = 500, message = "Reason must be 500 characters or fewer")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}