package com.tjahzi.api.config;

import brave.Tracing;
import brave.propagation.CurrentTraceContext;
import brave.propagation.StrictScopeDecorator;
import brave.propagation.ThreadLocalCurrentTraceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfig {

    @Value("${service.name}")
    private String serviceName;

    @Bean
    public CurrentTraceContext currentTraceContext() {
        return ThreadLocalCurrentTraceContext.newBuilder()
                .addScopeDecorator(StrictScopeDecorator.create()) // Trace ID 유지
                .build();
    }

    @Bean
    public Tracing tracing(CurrentTraceContext currentTraceContext) {
        return Tracing.newBuilder()
                .localServiceName(serviceName)
//                .propagationFactory(B3Propagation.FACTORY)
                .currentTraceContext(currentTraceContext) // 기존 빈을 활용
//                .sampler(Sampler.ALWAYS_SAMPLE)
                .build();
    }
}
