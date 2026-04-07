package com.digiqual.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateBatchRequest {

    @NotBlank(message = "Batch code is required")
    private String batchCode;

    @Min(value = 1, message = "Student count must be at least 1")
    private Integer studentCount;

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Integer getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }
}
