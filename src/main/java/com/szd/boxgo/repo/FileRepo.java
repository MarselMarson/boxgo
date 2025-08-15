package com.szd.boxgo.repo;

import com.szd.boxgo.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepo extends JpaRepository<File, Long> {
    Optional<File> findByName(String name);
}
