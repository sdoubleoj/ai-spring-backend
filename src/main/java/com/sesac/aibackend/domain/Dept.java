package com.sesac.aibackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "depts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Dept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            unique = true,
            nullable = false,
            length = 100
    )
    private String deptname;

    // 부서 계층
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id") // 최상위 부서는 null
    private Dept parent;

    // 부서 내 직원 수
    @Column(
            name = "emp_count",
            nullable = false
    )
    private int empCount = 0;

    public void increaseEmpCount() {
        this.empCount++;
    }

    public void decreaseEmpCount() {
        if (this.empCount > 0) {
            this.empCount--;
        }
    }

    // 직원 목록
    @OneToMany(
            mappedBy = "dept"
    )
    private List<Emp> emps = new ArrayList<>();
}
