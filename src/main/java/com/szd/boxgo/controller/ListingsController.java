package com.szd.boxgo.controller;

import com.szd.boxgo.dto.ListingDto;
import com.szd.boxgo.dto.NewListingDto;
import com.szd.boxgo.dto.auth.AuthUserId;
import com.szd.boxgo.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/listings")
@RequiredArgsConstructor
public class ListingsController {
    private final ListingService listingService;

    /*@GetMapping
    public List<ListingDto> get() {
        UserDto user = UserDto.builder()
                .id(123L)
                .firstName("John")
                .lastName("Doe")
                .photoUrl("https://cdn.example.com/users/123/avatar.jpg")
                .createdAt(OffsetDateTime.parse("2025-07-13T20:15:00Z"))
                .build();

        PackageDto package1 = PackageDto.builder()
                .id(17865L)
                .parcelType(new ParcelTypeDto(3L))
                .categories(List.of(new CategoryDto(1L), new CategoryDto(2L)))
                .length(40)
                .width(25)
                .height(20)
                .weight(5)
                .quantity(1)
                .price(20)
                .build();

        PackageDto package2 = PackageDto.builder()
                .id(17866L)
                .parcelType(new ParcelTypeDto(5L))
                .categories(List.of(new CategoryDto(3L)))
                .length(60)
                .width(45)
                .height(24)
                .weight(20)
                .quantity(2)
                .price(50)
                .build();

        return List.of(
                // Listing 10001
                ListingDto.builder()
                        .id(10001L)
                        .segments(List.of(
                                SegmentDto.builder()
                                        .fromCity(new CityDto(643006L))
                                        .departureAt(OffsetDateTime.parse("2025-08-04T21:00:00Z"))
                                        .toCity(new CityDto(643001L))
                                        .arrivalAt(OffsetDateTime.parse("2025-08-04T21:00:00Z"))
                                        .build(),
                                SegmentDto.builder()
                                        .fromCity(new CityDto(643001L))
                                        .departureAt(OffsetDateTime.parse("2025-08-05T21:00:00Z"))
                                        .toCity(new CityDto(320001L))
                                        .arrivalAt(OffsetDateTime.parse("2025-08-07T03:00:00Z"))
                                        .build()
                        ))
                        .availablePackages(List.of(package1, package2))
                        .user(user)
                        .isFavourite(false)
                        .createdAt(OffsetDateTime.parse("2025-07-19T00:00:00Z"))
                        .build(),
                ListingDto.builder()
                        .id(10002L)
                        .segments(List.of(
                                SegmentDto.builder()
                                        .fromCity(new CityDto(643001L))
                                        .departureAt(OffsetDateTime.parse("2025-08-05T21:00:00Z"))
                                        .toCity(new CityDto(360002L))
                                        .arrivalAt(OffsetDateTime.parse("2025-08-05T16:00:00Z"))
                                        .build()
                        ))
                        .availablePackages(List.of(package1, package2))
                        .user(user)
                        .isFavourite(false)
                        .createdAt(OffsetDateTime.parse("2025-07-19T00:00:00Z"))
                        .build(),

                // Listing 10003
                ListingDto.builder()
                        .id(10003L)
                        .segments(List.of(
                                SegmentDto.builder()
                                        .fromCity(new CityDto(643001L))
                                        .departureAt(OffsetDateTime.parse("2025-08-06T21:00:00Z"))
                                        .toCity(new CityDto(784002L))
                                        .arrivalAt(OffsetDateTime.parse("2025-08-06T20:00:00Z"))
                                        .build(),
                                SegmentDto.builder()
                                        .fromCity(new CityDto(784002L))
                                        .departureAt(OffsetDateTime.parse("2025-08-07T20:00:00Z"))
                                        .toCity(new CityDto(360002L))
                                        .arrivalAt(OffsetDateTime.parse("2025-08-07T16:00:00Z"))
                                        .build()
                        ))
                        .availablePackages(List.of(package1, package2))
                        .user(user)
                        .isFavourite(false)
                        .createdAt(OffsetDateTime.parse("2025-07-19T00:00:00Z"))
                        .build(),

                // Listing 10004
                ListingDto.builder()
                        .id(10004L)
                        .segments(List.of(
                                SegmentDto.builder()
                                        .fromCity(new CityDto(840002L))
                                        .departureAt(OffsetDateTime.parse("2025-08-07T04:00:00Z"))
                                        .toCity(new CityDto(643001L))
                                        .arrivalAt(OffsetDateTime.parse("2025-08-06T21:00:00Z"))
                                        .build()
                        ))
                        .availablePackages(List.of(package1, package2))
                        .user(user)
                        .isFavourite(false)
                        .createdAt(OffsetDateTime.parse("2025-07-19T00:00:00Z"))
                        .build(),

                // Listing 10005
                ListingDto.builder()
                        .id(10005L)
                        .segments(List.of(
                                SegmentDto.builder()
                                        .fromCity(new CityDto(643001L))
                                        .departureAt(OffsetDateTime.parse("2025-08-07T21:00:00Z"))
                                        .toCity(new CityDto(51001L))
                                        .arrivalAt(OffsetDateTime.parse("2025-08-07T20:00:00Z"))
                                        .build()
                        ))
                        .availablePackages(List.of(package1, package2))
                        .user(user)
                        .isFavourite(false)
                        .createdAt(OffsetDateTime.parse("2025-07-19T00:00:00Z"))
                        .build(),

                // Listing 10006
                ListingDto.builder()
                        .id(10006L)
                        .segments(List.of(
                                SegmentDto.builder()
                                        .fromCity(new CityDto(643001L))
                                        .departureAt(OffsetDateTime.parse("2025-08-08T21:00:00Z"))
                                        .toCity(new CityDto(792002L))
                                        .arrivalAt(OffsetDateTime.parse("2025-08-08T21:00:00Z"))
                                        .build(),
                                SegmentDto.builder()
                                        .fromCity(new CityDto(792002L))
                                        .departureAt(OffsetDateTime.parse("2025-08-08T21:00:00Z"))
                                        .toCity(new CityDto(840003L))
                                        .arrivalAt(OffsetDateTime.parse("2025-08-09T04:00:00Z"))
                                        .build()
                        ))
                        .availablePackages(List.of(package1, package2))
                        .user(user)
                        .isFavourite(false)
                        .createdAt(OffsetDateTime.parse("2025-07-19T00:00:00Z"))
                        .build(),

                // Listing 10007
                ListingDto.builder()
                        .id(10007L)
                        .segments(List.of(
                                SegmentDto.builder()
                                        .fromCity(new CityDto(276001L))
                                        .departureAt(OffsetDateTime.parse("2025-08-08T22:00:00Z"))
                                        .toCity(new CityDto(688001L))
                                        .arrivalAt(OffsetDateTime.parse("2025-08-08T22:00:00Z"))
                                        .build(),
                                SegmentDto.builder()
                                        .fromCity(new CityDto(688001L))
                                        .departureAt(OffsetDateTime.parse("2025-08-08T22:00:00Z"))
                                        .toCity(new CityDto(643006L))
                                        .arrivalAt(OffsetDateTime.parse("2025-08-08T21:00:00Z"))
                                        .build()
                        ))
                        .availablePackages(List.of(package1, package2))
                        .user(user)
                        .isFavourite(false)
                        .createdAt(OffsetDateTime.parse("2025-07-19T00:00:00Z"))
                        .build(),

                // Listing 10008
                ListingDto.builder()
                        .id(10008L)
                        .segments(List.of(
                                SegmentDto.builder()
                                        .fromCity(new CityDto(643006L))
                                        .departureAt(OffsetDateTime.parse("2025-08-08T21:00:00Z"))
                                        .toCity(new CityDto(51001L))
                                        .arrivalAt(OffsetDateTime.parse("2025-08-08T20:00:00Z"))
                                        .build()
                        ))
                        .availablePackages(List.of(package1, package2))
                        .user(user)
                        .isFavourite(false)
                        .createdAt(OffsetDateTime.parse("2025-07-19T00:00:00Z"))
                        .build()
        );
    }*/

    @GetMapping
    public Page<ListingDto> getAll(@PageableDefault(size = 15) Pageable pageable) {
        return listingService.getAll(pageable);
    }

    @PostMapping("/new")
    public ListingDto create(
            @AuthUserId Long userId,
            @RequestBody NewListingDto listing) {
        return listingService.create(userId, listing);
    }

    @GetMapping("/get/{id}")
    public ListingDto getById(@PathVariable Long id) {
        return listingService.getById(id);
    }
}
