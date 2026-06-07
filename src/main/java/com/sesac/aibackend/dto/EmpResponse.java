package com.sesac.aibackend.dto;

import com.sesac.aibackend.domain.Emp;

import java.time.LocalDateTime;

public record EmpResponse(
        Long id,
        String empname,
        String level,
        LocalDateTime hireAt,
        Long deptId,
        String deptname

) {
    // 일반 조회 시 (deptId만 포함)
    public static EmpResponse from(Emp emp) {
        return new EmpResponse(
                emp.getId(),
                emp.getEmpname(),
                emp.getLevel(),
                emp.getHireAt(),
                emp.getDept().getId(),
                null
        );
    }

    // 부서명까지 필요할 경우
    // EmpRepository.findByDeptIdWithDept() 등으로 dept를 미리 로딩
    public static EmpResponse fromWithDept(Emp emp) {
        return new EmpResponse(
                emp.getId(),
                emp.getEmpname(),
                emp.getLevel(),
                emp.getHireAt(),
                emp.getDept().getId(),
                emp.getDept().getDeptname()
        );
    }

}
