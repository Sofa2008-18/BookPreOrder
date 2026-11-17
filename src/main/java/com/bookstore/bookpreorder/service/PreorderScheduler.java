
package com.bookstore.bookpreorder.service;

import com.bookstore.bookpreorder.model.Preorder;
import com.bookstore.bookpreorder.model.PreorderStatus;
import com.bookstore.bookpreorder.repository.PreorderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PreorderScheduler {

    private final PreorderRepository preorderRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateReleasedPreorders() {
        LocalDate today = LocalDate.now();

        for (Preorder preorder : preorderRepository.findAll()) {
            if (preorder.getStatus() == PreorderStatus.WAITING_FOR_RELEASE
                    && preorder.getBook().getReleaseDate() != null
                    && !preorder.getBook().getReleaseDate().isAfter(today)) {

                preorder.setStatus(PreorderStatus.RELEASED);
                preorderRepository.save(preorder);
            }
        }
    }
}
