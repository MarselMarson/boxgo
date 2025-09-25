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
    @Query(value = "UPDATE unread_chats_count_version SET version = version + 1 WHERE user_id = :userId RETURNING version",
            nativeQuery = true)
    Long incrementAndGetVersion(@Param("userId") Long userId);
}
