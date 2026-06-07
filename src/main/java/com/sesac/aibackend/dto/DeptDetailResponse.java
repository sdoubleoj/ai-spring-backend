package com.sesac.aibackend.dto;

import com.sesac.aibackend.domain.Dept;
import com.sesac.aibackend.domain.Emp;

import java.util.List;

public record DeptDetailResponse(
        DeptResponse dept,
        List<EmpResponse> emps
) {
    public static DeptDetailResponse from(Dept dept, List<Emp> emps) {
        return new DeptDetailResponse(
                DeptResponse.from(dept),
                emps.stream().map(EmpResponse::from).toList()
        );
    }

    public static DeptDetailResponse fromWithParent(Dept dept, List<Emp> emps) {
        return new DeptDetailResponse(
                DeptResponse.fromWithParent(dept),
                emps.stream().map(EmpResponse::from).toList()
        );
    }
}
