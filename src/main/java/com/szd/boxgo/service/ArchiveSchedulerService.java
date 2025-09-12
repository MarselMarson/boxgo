package com.szd.boxgo.service;

import com.szd.boxgo.repo.RouteSegmentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArchiveSchedulerService {

    private final RouteSegmentRepo routeSegmentRepository;
    private final Clock clock;

    @Transactional
    public int archiveExpiredSegments() {
        OffsetDateTime now = OffsetDateTime.now(clock);
        int totalArchived = 0;
        int batchSize = 100; // Размер пачки для обработки

        log.info("Starting archive process at {}", now);

        // Сначала посчитаем, сколько записей нужно обработать
        long expiredCount = routeSegmentRepository.countExpiredSegments(now);
        log.info("Found {} expired segments to archive", expiredCount);

        if (expiredCount == 0) {
            return 0;
        }

        // Обрабатываем записи пачками
        int batchNumber = 0;
        while (true) {
            int archivedInBatch = routeSegmentRepository.archiveExpiredSegmentsBatch(now, batchSize);

            if (archivedInBatch == 0) {
                break; // Больше нет записей для обработки
            }

            totalArchived += archivedInBatch;
            batchNumber++;

            log.debug("Batch {}: Archived {} segments", batchNumber, archivedInBatch);

            // Если обработали меньше записей, чем размер пачки, значит это последняя пачка
            if (archivedInBatch < batchSize) {
                break;
            }

            // Небольшая пауза между пачками для снижения нагрузки на БД
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.info("Completed archive process. Total archived: {}", totalArchived);
        return totalArchived;
    }
}
