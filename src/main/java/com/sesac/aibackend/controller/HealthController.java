package com.sesac.aibackend.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 없이 접근 가능한 헬스 체크 라우트.
 */
@RestController
public class HealthController {

    @GetMapping("/health2") // /health로 GET 요청이 오면 사용
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }
}
