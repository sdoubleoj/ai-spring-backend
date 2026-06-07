package com.sesac.aibackend.dto;

import com.sesac.aibackend.domain.Dept;

public record DeptResponse(
        Long id,
        String deptname,
        Long parentId,
        String parentDeptname,
        int empCount
) {
    // parentId만 필요할 때,
    // Lazy 프록시에서 getId()만 읽으므로 fetch join 불필요
    public static DeptResponse from(Dept dept) {
        return new DeptResponse(
                dept.getId(),
                dept.getDeptname(),
                dept.getParent() != null ? dept.getParent().getId() : null,
                null,
                dept.getEmpCount()
        );
    }

    // 상위 부서명까지 필요할 경우
    // DeptService.findByIdWithParent()로 조회한 Dept를 넘김
    public static DeptResponse fromWithParent(Dept dept) {
        return new DeptResponse(
                dept.getId(),
                dept.getDeptname(),
                dept.getParent() != null ? dept.getParent().getId() : null,
                dept.getParent() != null ? dept.getParent().getDeptname() : null,
                dept.getEmpCount()
        );
    }
}
