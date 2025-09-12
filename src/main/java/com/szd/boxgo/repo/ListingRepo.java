package com.szd.boxgo.repo;

import com.szd.boxgo.entity.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepo extends JpaRepository<Listing, Long>, JpaSpecificationExecutor<Listing> {
    @EntityGraph(attributePaths = {"owner", "routeSegments", "packages"})
    Page<Listing> findAllByOwnerIdOrderByFirstDepartureLocalAt(Long ownerId, Pageable pageable);
}
