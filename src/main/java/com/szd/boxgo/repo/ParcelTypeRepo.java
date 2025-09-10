package com.szd.boxgo.repo;

import com.szd.boxgo.entity.ParcelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelTypeRepo extends JpaRepository<ParcelType, Integer> {
}
