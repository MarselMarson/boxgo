package com.szd.boxgo.repo;

import com.szd.boxgo.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepo extends JpaRepository<Listing, Long> {
}
