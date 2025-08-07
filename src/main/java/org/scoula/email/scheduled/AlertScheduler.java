package org.scoula.email.scheduled;

import lombok.RequiredArgsConstructor;
import org.scoula.bookmark.dto.BookmarkEmailInfo;
import org.scoula.bookmark.mapper.BookmarkMapper;
import org.scoula.email.service.MailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertScheduler {

    private final BookmarkMapper bookmarkMapper;
    private final MailService mailService;

//    @Scheduled(cron = "0 0 8 * * *") 테스트용 주석
    public void sendApplicationDateAlert() {
        LocalDate today = LocalDate.now();
        List<BookmarkEmailInfo> alerts = bookmarkMapper.findAlertsForToday(today);

        for (BookmarkEmailInfo info : alerts) {
            mailService.sendCustomAlertMail(info.getEmail(), info.getTitle(), info.getType());
        }
    }
}
