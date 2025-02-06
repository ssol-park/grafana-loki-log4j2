package com.tjahzi.api.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/err")
public class ErrorController {

    private final ErrorService errorService;

    public ErrorController(ErrorService errorService) {
        this.errorService = errorService;
    }

    @GetMapping
    public ResponseEntity<Void> getRandomError() {
        errorService.getRandomError();
        return ResponseEntity.ok().build();
    }
}
