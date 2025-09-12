package com.szd.boxgo.scheduler;

import com.szd.boxgo.service.ArchiveSchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class ArchiveScheduler {

    private final ArchiveSchedulerService archiveService;

    // Запуск каждые 15 минут
    @Scheduled(cron = "0 */15 * * * *")
    public void archiveExpiredSegments() {
        try {
            log.info("Starting archive scheduler");
            int archivedCount = archiveService.archiveExpiredSegments();

            if (archivedCount > 0) {
                log.info("Archive scheduler completed. Archived {} segments", archivedCount);
            } else {
                log.debug("No segments to archive");
            }

        } catch (Exception e) {
            log.error("Archive scheduler failed: {}", e.getMessage(), e);
        }
    }
}
