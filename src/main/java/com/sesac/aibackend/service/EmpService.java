package com.sesac.aibackend.service;

import com.sesac.aibackend.domain.Dept;
import com.sesac.aibackend.domain.Emp;
import com.sesac.aibackend.error.NotFoundException;
import com.sesac.aibackend.repository.DeptRepository;
import com.sesac.aibackend.repository.EmpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpService {

    private final EmpRepository empRepository;
    private final DeptRepository deptRepository;

    // --조회--
    // 전체 직원 조회
    @Transactional(readOnly = true)
    public List<Emp> findAll() {
        return empRepository.findAll();
    }

    // Id로 직원 조회
    @Transactional(readOnly = true)
    public Optional<Emp> findById(Long id) {
        return empRepository.findById(id);
    }

    // Id로 직원 존재 여부 조회
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return empRepository.existsById(id);
    }

    // 이름으로 직원 존재 여부 조회
    @Transactional(readOnly = true)
    public boolean existsByEmpname(String empname) {
        return empRepository.existsByEmpname(empname);
    }

    // --변경--
    // 새 직원 생성
    @Transactional
    public Emp create(String empname, String level, LocalDateTime hireAt, Long deptId) {
        Dept dept = deptRepository.findById(deptId)
                .orElseThrow(() -> NotFoundException.of("dept", deptId));

        Emp saved = empRepository.save(
                Emp.builder()
                        .empname(empname)
                        .level(level)
                        .hireAt(hireAt)
                        .dept(dept)
                        .build()
        );
        updateDeptEmpCount(deptId, +1);
        return saved;
    }

    // 직원 수 변경
    @Transactional
    private void updateDeptEmpCount(Long deptId, int delta) {
        Dept dept = deptRepository.findById(deptId)
                .orElseThrow(() -> NotFoundException.of("dept", deptId));

        if (delta > 0) dept.increaseEmpCount();
        else dept.decreaseEmpCount();

        deptRepository.save(dept);
    }

}
