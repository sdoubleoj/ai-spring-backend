package com.sesac.aibackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "emps")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Emp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            length = 50
    )
    private String empname;

    // 직급
    @Column(
            nullable = false,
            length = 20
    )
    private String level;

    // 입사일
    @Column(
            nullable = false
    )
    private LocalDateTime hireAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "dept_id",
            nullable = false
    )
    private Dept dept;
}
