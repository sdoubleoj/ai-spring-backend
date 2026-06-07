package com.sesac.aibackend.controller;

import com.sesac.aibackend.domain.Dept;
import com.sesac.aibackend.domain.Emp;
import com.sesac.aibackend.dto.DeptDetailResponse;
import com.sesac.aibackend.dto.DeptRequest;
import com.sesac.aibackend.dto.DeptResponse;
import com.sesac.aibackend.error.NotFoundException;
import com.sesac.aibackend.service.DeptService;
import com.sun.net.httpserver.HttpsServer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/depts")
@RequiredArgsConstructor
public class DeptController {
    private final DeptService deptService;

    //--GET--
    @GetMapping
    public List<DeptResponse> list() {
        return deptService.findAll().stream()
                .map(DeptResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public DeptResponse get(@PathVariable Long id) {
        Dept dept = deptService.findByIdWithParent(id)
                .orElseThrow(() -> NotFoundException.of("dept", id));
        return DeptResponse.fromWithParent(dept);
    }

    // 부서 조회 시 소속 직원 조회
    @GetMapping("/{id}/emps")
    public DeptDetailResponse getWithEmps(@PathVariable Long id) {
        Dept dept = deptService.findByIdWithParent(id)
                .orElseThrow(() -> NotFoundException.of("dept", id));
        List<Emp> emps = deptService.findEmpsByDeptId(id);
        return DeptDetailResponse.fromWithParent(dept, emps);
    }

    @GetMapping("/roots")
    public List<DeptResponse> listRoots() {
        return deptService.findRootDepts().stream()
                .map(DeptResponse::from)
                .toList();
    }

    @GetMapping("/{id}/children")
    public List<DeptResponse> listChildren(@PathVariable Long id) {
        return deptService.findChildrenByParentId(id).stream()
                .map(DeptResponse::from)
                .toList();
    }

    //--POST--
    @PostMapping
    public ResponseEntity<DeptResponse> create(@Valid @RequestBody DeptRequest req) {
        if (deptService.existsByDeptname(req.deptname())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "부서명이 이미 있습니다: " + req.deptname());
        }
        Dept saved = deptService.create(req.deptname(), req.parentId());
        URI location = URI.create("/depts/" + saved.getId());
        return ResponseEntity.created(location).body(DeptResponse.from(saved));
    }

    //--PUT--
    @PutMapping("/{id}")
    public DeptResponse update(@PathVariable Long id, @Valid @RequestBody DeptRequest req) {
        Dept existing = deptService.findById(id)
                .orElseThrow(() -> NotFoundException.of("dept", id));

        if (!existing.getDeptname().equals(req.deptname())
                && deptService.existsByDeptname(req.deptname())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "부서명이 이미 있습니다: " + req.deptname());
        }

        Dept parent = null;
        if (req.parentId() != null) {
            parent = deptService.findById(req.parentId())
                    .orElseThrow(() -> NotFoundException.of("dept", req.parentId()));
        }

        Dept updated = Dept.builder()
                .id(existing.getId())
                .deptname(req.deptname())
                .parent(parent)
                .empCount(existing.getEmpCount())
                .build();

        return DeptResponse.from(deptService.save(updated));
    }

    //--DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!deptService.existsById(id)) {
            throw NotFoundException.of("dept", id);
        }
        deptService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
