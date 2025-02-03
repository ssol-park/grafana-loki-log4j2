package com.tjahzi.api.config;

import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public PrometheusMeterRegistry prometheusMeterRegistry() {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        // JVM 메모리
        new JvmMemoryMetrics().bindTo(registry);

        // JVM 스레드
        new JvmThreadMetrics().bindTo(registry);

        // JVM 클래스 로딩
        new ClassLoaderMetrics().bindTo(registry);

        // CPU 및 프로세서 메트릭
        new ProcessorMetrics().bindTo(registry);

        // JVM Uptime (프로세스 실행 시간)
        new UptimeMetrics().bindTo(registry);

        // 가비지 컬렉터 (GC)
        new JvmGcMetrics().bindTo(registry);

        // 파일 디스크립터 (사용 중인 파일 핸들)
        new FileDescriptorMetrics().bindTo(registry);

        return registry;
    }
}
