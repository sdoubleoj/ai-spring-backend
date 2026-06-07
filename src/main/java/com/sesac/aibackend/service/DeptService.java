package com.sesac.aibackend.service;

import com.sesac.aibackend.domain.Dept;
import com.sesac.aibackend.domain.Emp;
import com.sesac.aibackend.domain.User;
import com.sesac.aibackend.error.NotFoundException;
import com.sesac.aibackend.repository.DeptRepository;
import com.sesac.aibackend.repository.EmpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeptService {

    private final DeptRepository deptRepository;
    /**
     * 사용자 조회/저장 서비스.
     *
     * 1. 부서 조회 시 소속 직원들 조회
     * 2. 소속 직원이 있으면 부서 삭제 제한
     */
    private final EmpRepository empRepository;

    // --조회--
    // 전체 부서 조회
    @Transactional(readOnly = true)
    public List<Dept> findAll() {
        return deptRepository.findAll();
    }

    // Id로 부서 조회
    @Transactional(readOnly = true)
    public Optional<Dept> findById(Long id) {
        return deptRepository.findById(id);
    }

    // 부서명으로 부서 조회
    @Transactional(readOnly = true)
    public Optional<Dept> findByDeptname(String deptname) {
        return deptRepository.findByDeptname(deptname);
    }

    // Id로 부서 존재 여부 조회
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return deptRepository.existsById(id);
    }

    // 부서명으로 부서 존재 여부 조회
    @Transactional(readOnly = true)
    public boolean existsByDeptname(String deptname) {
        return deptRepository.existsByDeptname(deptname);
    }

    // 최상위 부서 조회
    @Transactional(readOnly = true)
    public List<Dept> findRootDepts() {
        return deptRepository.findByParentIsNull();
    }

    // 하위 부서 조회
    @Transactional(readOnly = true)
    public List<Dept> findChildrenByParentId(Long parentId) {
        if (!deptRepository.existsById(parentId)) {
            throw NotFoundException.of("dept", parentId);
        }
        return deptRepository.findByParentId(parentId);
    }

    // 하위 부서와 속한 상위 부서 정보까지 함께 조회
    @Transactional(readOnly = true)
    public Optional<Dept> findByIdWithParent(Long id) {
        return deptRepository.findByIdWithParent(id);
    }

    // 부서 소속 직원들 조회
    @Transactional(readOnly = true)
    public List<Emp> findEmpsByDeptId(Long deptId) {
        if (!deptRepository.existsById(deptId)) {
            throw NotFoundException.of("dept", deptId);
        }
        return empRepository.findByDeptIdOrderByHireAtDesc(deptId);
    }

    //--변경--
    // 새 부서 생성
    @Transactional
    public Dept create(String deptname, Long parentId) {
        if (deptRepository.existsByDeptname(deptname)) {
            throw new IllegalArgumentException("이미 존재하는 부서명입니다: " + deptname);
        }

        Dept parent = null;
        if (parentId != null) {
            parent = deptRepository.findById(parentId)
                    .orElseThrow(() -> NotFoundException.of("dept", parentId));
        }
        return deptRepository.save(
                Dept.builder()
                        .deptname(deptname)
                        .parent(parent)
                        .empCount(0)
                        .build()
        );
    }

    // 부서 정보 수정
    @Transactional
    public Dept save(Dept dept) {
        return deptRepository.save(dept);
    }

    // 부서 삭제
    public void deleteById(Long id) {
        if (!deptRepository.findByParentId(id).isEmpty()) {
            throw new IllegalStateException("하위 부서가 있어서 삭제할 수 없습니다.");
        }
        if (empRepository.countByDeptId(id) > 0) {
            throw new IllegalStateException("소속 직원이 있어 삭제할 수 없습니다.");
        }
        deptRepository.deleteById(id);
    }
}
