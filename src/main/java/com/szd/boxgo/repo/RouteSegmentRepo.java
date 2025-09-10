package com.szd.boxgo.repo;

import com.szd.boxgo.entity.RouteSegment;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteSegmentRepo extends JpaRepository<RouteSegment, Long>, JpaSpecificationExecutor<RouteSegment> {
    @NotNull Page<RouteSegment> findAll(@NotNull Specification<RouteSegment> spec, @NotNull Pageable pageable);
}
