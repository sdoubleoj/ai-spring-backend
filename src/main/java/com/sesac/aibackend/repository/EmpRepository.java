package com.sesac.aibackend.repository;

import com.sesac.aibackend.domain.Emp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmpRepository extends JpaRepository<Emp, Long> {
    // 부서 소속 직원 조회
    List<Emp> findByDeptIdOrderByHireAtDesc(Long deptId);

    // 부서 정보까지 함께 조회
    @Query(
            """
            select e from Emp e
            join fetch e.dept
            where e.dept.id = :deptId
            order by e.hireAt desc
            """
    )
    List<Emp> findByDeptIdWithDept(Long deptId);

    // 부서별 소속 직원 수
    long countByDeptId(Long deptId);

    boolean existsByEmpname(String empname);
}
