package com.sesac.aibackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EmpRequest(
        @NotBlank
        @Size(max = 50)
        String empname,

        @NotBlank
        @Size(max = 20)
        String level,

        @NotNull
        LocalDateTime hireAt,

        @NotNull
        Long deptId
) {
}
