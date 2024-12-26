package error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeoutException;

@Service
public class ErrorService {
    private static final Logger logger = LoggerFactory.getLogger(ErrorService.class);
    private static final Random RANDOM = new Random();

    public void getRandomError() throws TimeoutException {
        logger.info("[getRandomError] Start");

        int idx = RANDOM.nextInt(5);

        switch (idx) {
            case 0:
                logger.info("idx == 0, NullPointerException");
                throw new NullPointerException();

            case 1:
                logger.info("idx == 1, TimeoutException");
                throw new TimeoutException("Operation timed out");
            case 2:
                logger.info("idx == 2, llegalArgumentException");
                throw new IllegalArgumentException("Duplicate key value violates unique constraint");
            case 3:
                logger.info("idx == 3, OutOfMemoryError");
                throw new OutOfMemoryError("Heap space exceeded");
            default:
                logger.info("idx: {}, Not matched. Success", idx);
        }
    }
}
