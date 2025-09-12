package com.szd.boxgo.repo;

import com.szd.boxgo.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PackageRepo extends JpaRepository<Package, Long> {
    List<Package> findByIsArchivedFalseAndListingIdIn(Collection<Long> listingIds);
}
