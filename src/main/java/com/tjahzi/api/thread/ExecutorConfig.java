package com.tjahzi.api.thread;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.concurrent.*;

@Configuration
public class ExecutorConfig{
    public static final String TRACE_ID = "traceId";

    @Bean
    public ExecutorService mdcAwareExecutorService() {
        return new ThreadPoolExecutor(
                10, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()
        ) {
            @Override
            public void execute(Runnable command) {
                String traceId = MDC.get(TRACE_ID);

                super.execute(() -> {
                    try {
                        if (StringUtils.hasText(traceId)) {
                            MDC.put(TRACE_ID, traceId);
                        }
                        command.run();
                    } finally {
                        MDC.clear();
                    }
                });
            }
        };
    }
}
