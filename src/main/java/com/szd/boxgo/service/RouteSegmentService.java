package com.szd.boxgo.service;

import com.szd.boxgo.entity.RouteSegment;
import com.szd.boxgo.repo.RouteSegmentRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteSegmentService {
    private final RouteSegmentRepo segmentRepo;

    public RouteSegment findById(Long id) {
        return segmentRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("segment id " + id + " not found"));
    }
}
