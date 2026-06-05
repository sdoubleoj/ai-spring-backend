package com.sesac.aibackend.repository;

import com.sesac.aibackend.domain.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DeptRepository extends JpaRepository<Dept, Long> {

    Optional<Dept> findByDeptname(String deptname);

    boolean existsByDeptname(String deptname);

    // 상위 부서에 속한 하위 부서들 조회
    List<Dept> findByParentId(Long parentId);

    List<Dept> findByParentIsNull();

    // 하위 부서와 속한 상위 부서 정보까지 함께 조회
    @Query(
            """
            select d from Dept d
            join fetch d.parent
            where d.id = :id
            """
    )
    Optional<Dept> findByIdWithParent(Long id);
}
