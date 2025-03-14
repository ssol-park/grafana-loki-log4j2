package com.tjahzi.api.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.util.concurrent.*;

@Configuration
public class TreadPoolConfig {
    public static final String TRACE_ID = "traceId";

    @Bean(name = "mdcAwareTaskExecutor")
    public ThreadPoolTaskExecutor mdcAwareTaskExecutor(MeterRegistry registry) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("mdc-executor-");
        executor.setTaskDecorator(new MDCTaskDecorator());
        executor.initialize();

        String executorName = "mdc-executor";

        // 활성 스레드 수
        Gauge.builder("executor_active_threads", executor, ThreadPoolTaskExecutor::getActiveCount)
                .tags("name", executorName)
                .description("현재 실행 중인 스레드 개수")
                .register(registry);

        // 완료된 작업 수
        Gauge.builder("executor_completed_tasks_total", executor, e -> e.getThreadPoolExecutor().getCompletedTaskCount())
                .tags("name", executorName)
                .description("완료된 작업 개수")
                .register(registry);

        // 유휴 상태 스레드 수
        Gauge.builder("executor_idle_seconds_count", executor, e -> e.getThreadPoolExecutor().getPoolSize() - e.getActiveCount())
                .tags("name", executorName)
                .description("유휴 상태 스레드 개수")
                .register(registry);

        // 유휴 상태 최대 스레드 수
        Gauge.builder("executor_idle_seconds_max", executor, e -> e.getThreadPoolExecutor().getMaximumPoolSize() - e.getActiveCount())
                .tags("name", executorName)
                .description("최대 유휴 상태 스레드 개수")
                .register(registry);

        // 총 유휴 상태 스레드 수
        Gauge.builder("executor_idle_seconds_sum", executor, e -> e.getThreadPoolExecutor().getCorePoolSize() - e.getActiveCount())
                .tags("name", executorName)
                .description("총 유휴 상태 스레드 개수")
                .register(registry);

        // 코어 스레드 개수
        Gauge.builder("executor_pool_core_threads", executor, ThreadPoolTaskExecutor::getCorePoolSize)
                .tags("name", executorName)
                .description("코어 스레드 개수")
                .register(registry);

        // 최대 스레드 개수
        Gauge.builder("executor_pool_max_threads", executor, ThreadPoolTaskExecutor::getMaxPoolSize)
                .tags("name", executorName)
                .description("최대 스레드 개수")
                .register(registry);

        // 현재 생성된 스레드 개수
        Gauge.builder("executor_pool_size_threads", executor, e -> e.getThreadPoolExecutor().getPoolSize())
                .tags("name", executorName)
                .description("현재 생성된 스레드 개수")
                .register(registry);

        // 큐에서 남은 작업 공간
        Gauge.builder("executor_queue_remaining_tasks", executor, e -> e.getThreadPoolExecutor().getQueue().remainingCapacity())
                .tags("name", executorName)
                .description("큐에서 남은 작업 공간")
                .register(registry);

        // 현재 큐에 쌓인 작업 개수
        Gauge.builder("executor_queued_tasks", executor, e -> e.getThreadPoolExecutor().getQueue().size())
                .tags("name", executorName)
                .description("현재 큐에 쌓인 작업 개수")
                .register(registry);

        // 실행된 작업 수
        Gauge.builder("executor_seconds_count", executor, e -> e.getThreadPoolExecutor().getCompletedTaskCount())
                .tags("name", executorName)
                .description("총 실행된 작업 수")
                .register(registry);

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
