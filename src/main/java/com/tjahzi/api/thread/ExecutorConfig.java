package com.tjahzi.api.thread;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.util.concurrent.*;

@Configuration
public class ExecutorConfig{
    public static final String TRACE_ID = "traceId";

    @Bean
    public ThreadPoolTaskExecutor mdcAwareTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("mdc-executor-");
        executor.setTaskDecorator(new MDCTaskDecorator());
        executor.initialize();
        return executor;
    }

    private class MDCTaskDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable task) {
            String traceId = MDC.get(TRACE_ID);
            return () -> {
                try {
                    if (StringUtils.hasText(traceId)) {
                        MDC.put(TRACE_ID, traceId);
                    }
                    task.run();
                } finally {
                    MDC.clear();
                }
            };
        }
    }

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
