package com.szd.boxgo.repo;

import com.szd.boxgo.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepo extends JpaRepository<City, Long> {
}
