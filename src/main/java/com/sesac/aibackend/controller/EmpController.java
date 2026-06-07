package com.sesac.aibackend.controller;

import com.sesac.aibackend.domain.Dept;
import com.sesac.aibackend.domain.Emp;
import com.sesac.aibackend.dto.EmpRequest;
import com.sesac.aibackend.dto.EmpResponse;
import com.sesac.aibackend.error.NotFoundException;
import com.sesac.aibackend.repository.DeptRepository;
import com.sesac.aibackend.service.DeptService;
import com.sesac.aibackend.service.EmpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/emps")
@RequiredArgsConstructor
public class EmpController {
    private final EmpService empService;
    private final DeptService deptService;

    //--GET--
    @GetMapping
    public List<EmpResponse> list() {
        return empService.findAll().stream()
                .map(EmpResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public EmpResponse get(@PathVariable Long id) {
        Emp emp = empService.findById(id)
                .orElseThrow(() -> NotFoundException.of("emp", id));
        return EmpResponse.from(emp);
    }

    //--POST--
    @PostMapping
    public ResponseEntity<EmpResponse> create(@Valid @RequestBody EmpRequest req) {
        Emp saved = empService.create(
                req.empname(),
                req.level(),
                req.hireAt(),
                req.deptId()
        );
        URI location =  URI.create("/emps/" + saved.getId());
        return ResponseEntity.created(location).body(EmpResponse.from(saved));
    }

    //--PUT--
    @PutMapping("/{id}")
    public EmpResponse update(@PathVariable Long id, @Valid @RequestBody EmpRequest req) {
        Emp existing = empService.findById(id)
                .orElseThrow(() -> NotFoundException.of("emp", id));

        Dept dept = deptService.findById(req.deptId())
                .orElseThrow(() -> NotFoundException.of("dept", req.deptId()));

        Emp updated = Emp.builder()
                .id(existing.getId())
                .empname(req.empname())
                .level(req.level())
                .hireAt(req.hireAt())
                .dept(dept)
                .build();
        return EmpResponse.from(empService.save(updated));
    }

    //--DELETE--
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!empService.existsById(id)) {
            throw NotFoundException.of("emp", id);
        }
        empService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
