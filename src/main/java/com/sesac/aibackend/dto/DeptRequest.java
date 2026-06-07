package com.sesac.aibackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeptRequest(
        @NotBlank
        @Size(max = 100)
        String deptname,

        Long parentId
) {
    // Dept는 parent를 DB에서 조회해야 해서 DTO에서 엔티티를 완성할 수 없으므로,
    // toEntity()를 사용하지 않음.
}
