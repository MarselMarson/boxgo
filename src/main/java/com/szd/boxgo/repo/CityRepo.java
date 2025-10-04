package com.szd.boxgo.repo;

import com.szd.boxgo.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepo extends JpaRepository<City, Long> {
    @Query("SELECT ct.name FROM City c JOIN c.translations ct WHERE c.id = :cityId AND ct.languageCode = :languageCode")
    String findCityNameByIdAndLanguage(@Param("cityId") Long cityId, @Param("languageCode") String languageCode);
}
