package com.szd.boxgo.controller;

import com.szd.boxgo.dto.CreatedListingDto;
import com.szd.boxgo.dto.ListingDto;
import com.szd.boxgo.dto.NewListingDto;
import com.szd.boxgo.dto.SegmentDto;
import com.szd.boxgo.dto.auth.AuthUserId;
import com.szd.boxgo.service.ListingSegmentFilter;
import com.szd.boxgo.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/listings")
@RequiredArgsConstructor
public class ListingsController {
    private final ListingService listingService;

    @GetMapping
    public Page<SegmentDto> getAll(@PageableDefault(size = 15, sort = "departureLocalAt", direction = Sort.Direction.ASC) Pageable pageable,
                                   ListingSegmentFilter filter) {
        return listingService.getAll(pageable, filter);
    }

    @PostMapping("/new")
    public CreatedListingDto create(
            @AuthUserId Long userId,
            @RequestBody NewListingDto listing) {
        return listingService.create(userId, listing);
    }

    @GetMapping("/my")
    public Page<CreatedListingDto> getCreated(@PageableDefault(size = 15) Pageable pageable,
                                        @AuthUserId Long userId) {
        return listingService.getCreated(userId, pageable);
    }

    @GetMapping("/get/{id}")
    public ListingDto getById(@PathVariable Long id) {
        return listingService.getById(id);
    }

    @GetMapping("/segment/{id}")
    public SegmentDto getSegmentById(@PathVariable Long id) {
        return listingService.getSegmentById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id, @AuthUserId Long userId) {
        listingService.archiveById(id, userId);
    }
}
