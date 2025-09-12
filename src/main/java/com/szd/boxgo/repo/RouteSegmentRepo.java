package com.szd.boxgo.repo;

import com.szd.boxgo.entity.RouteSegment;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface RouteSegmentRepo extends JpaRepository<RouteSegment, Long>, JpaSpecificationExecutor<RouteSegment> {
    @NotNull Page<RouteSegment> findAll(@NotNull Specification<RouteSegment> spec, @NotNull Pageable pageable);

    @Modifying
    @Query(value = """
        WITH expired_segments AS (
            SELECT id FROM route_segments
            WHERE archive_at <= :currentTime
            AND is_archived = false
            ORDER BY id
            FOR UPDATE SKIP LOCKED
            LIMIT :batchSize
        )
        UPDATE route_segments rs
        SET is_archived = true, archived_at = :currentTime
        FROM expired_segments es
        WHERE rs.id = es.id
        """,
            nativeQuery = true)
    int archiveExpiredSegmentsBatch(
            @Param("currentTime") OffsetDateTime currentTime,
            @Param("batchSize") int batchSize);

    @Query("SELECT COUNT(rs) FROM RouteSegment rs WHERE rs.archiveAt <= :currentTime AND rs.isArchived = false")
    long countExpiredSegments(@Param("currentTime") OffsetDateTime currentTime);
}
