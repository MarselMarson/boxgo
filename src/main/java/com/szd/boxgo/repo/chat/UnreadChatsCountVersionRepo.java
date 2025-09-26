package com.szd.boxgo.repo.chat;

import com.szd.boxgo.entity.chat.UnreadChatsCountVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UnreadChatsCountVersionRepo extends JpaRepository<UnreadChatsCountVersion, Long> {

    @Modifying
    @Query("UPDATE UnreadChatsCountVersion u SET u.version = u.version + 1 WHERE u.userId = :userId")
    void incrementVersion(@Param("userId") Long userId);

    @Query("SELECT u.version FROM UnreadChatsCountVersion u WHERE u.userId = :userId")
    int findVersionByUserId(@Param("userId") Long userId);
}
