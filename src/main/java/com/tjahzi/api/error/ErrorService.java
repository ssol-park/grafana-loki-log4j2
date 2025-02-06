package com.tjahzi.api.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ExecutorService;

@Service
public class ErrorService {
    private static final Logger logger = LoggerFactory.getLogger(ErrorService.class);
    private static final Random RANDOM = new Random();

    private final ExecutorService executorService;
    public ErrorService(@Qualifier("mdcAwareExecutorService") ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void getRandomError() {
        int idx = RANDOM.nextInt(5);
        logger.info("Start Random Error .. idx: {}", idx);

        executorService.submit(() -> {
            try {
                doJob(idx);
            } catch (RuntimeException e) {
                logger.error("{}", e.getMessage(), e);
            }
        });
    }

    private void doJob(int idx) {
        switch (idx) {
            case 0:
                throw new RuntimeException("NullPointerException");
            case 1:
                throw new RuntimeException("TimeoutException");
            case 2:
                throw new RuntimeException("IllegalArgumentException");
            case 3:
                throw new RuntimeException("OutOfMemoryError");
            case 4:
                logger.error("idx == 4, Some error, not throw exception");
            default:
                logger.info("idx: {}, Not matched. Success", idx);
        }
    }
}
