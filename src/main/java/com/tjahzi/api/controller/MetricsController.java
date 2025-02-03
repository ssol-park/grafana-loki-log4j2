package com.tjahzi.api.controller;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetricsController {

    private final PrometheusMeterRegistry prometheusMeterRegistry;

    public MetricsController(PrometheusMeterRegistry prometheusMeterRegistry) {
        this.prometheusMeterRegistry = prometheusMeterRegistry;
    }

    @GetMapping(value = "/metrics", produces = "text/plain")
    public String metrics() {
        return prometheusMeterRegistry.scrape();
    }
}
