package com.losmessias.leherer.tasks;

import com.losmessias.leherer.domain.Homework;
import com.losmessias.leherer.domain.enumeration.HomeworkStatus;
import com.losmessias.leherer.repository.HomeworkRepository;
import com.losmessias.leherer.service.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class HomeworkOrchestrator {

    private static final Logger log = Logger.getLogger(HomeworkOrchestrator.class.getName());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
    private final HomeworkService homeworkService;
    private final HomeworkRepository homeworkRepository;

    private static LocalDateTime convertToGMTMinus3(LocalDateTime gmtDateTime) {
        // Set the time zone to GMT
        ZoneId gmtZone = ZoneId.of("GMT");
        // Convert the GMT time to GMT-3
        return gmtDateTime.atZone(gmtZone)
                .withZoneSameInstant(ZoneId.of("GMT-3"))
                .toLocalDateTime();
    }

    @Scheduled(cron = "0 * * * * *")
    public void checkHomework() {
        // LocalDate -3 due to de Render Hosting time zone
        LocalDateTime endingTimeInGMT = LocalDateTime.now();
        LocalDateTime endingTimeInGMTMinus3 = convertToGMTMinus3(endingTimeInGMT);
        log.info("Executing on: " + endingTimeInGMTMinus3);
        List<Homework> homeworks = homeworkRepository.findByDeadline(endingTimeInGMTMinus3);
        if (homeworks.isEmpty()) {
            log.info("No homeworks found for this time");
            return;
        }
        homeworks.forEach(homework -> {
            log.info("Found homework: " + homework.getId() + " at " + homework.getDeadline());
            if (homework.getStatus().equals(HomeworkStatus.PENDING)) {
                log.info("Homework can't be answered any more");
                homework.setStatus(HomeworkStatus.LATE);
                homeworkRepository.save(homework);
            }
        });
    }
}
