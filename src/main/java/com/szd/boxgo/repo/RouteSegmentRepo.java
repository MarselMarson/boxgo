package com.szd.boxgo.repo;

import com.szd.boxgo.entity.RouteSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteSegmentRepo extends JpaRepository<RouteSegment, Long> {
}
