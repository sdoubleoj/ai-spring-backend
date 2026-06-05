package com.sesac.aibackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
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

    // 부서 내 직원 수
    @Column(
            name = "emp_count",
            nullable = false
    )
    private int empCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private Role role;

    @OneToMany(
            mappedBy = "dept",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Emp> chatLogs = new ArrayList<>();
}
