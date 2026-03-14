package com.kt.setly.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health", description = "Health check APIs")
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public String health() {
        return "Setly backend is running";
    }
}
