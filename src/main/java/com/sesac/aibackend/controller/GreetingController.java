package com.sesac.aibackend.controller;

import com.sesac.aibackend.service.GreetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
// @RestController를 달아야 GreetingController 클래스가 Bean으로 인식됨
@RestController
@RequiredArgsConstructor
public class GreetingController {

    private final GreetingService greetingService; // final로 GreetingService에 greetingService라는 의존성을 주입할 때, greetingService도 bean으로 등록되어 있어야 함.

//    [GET] /greeting?name=홀길동
    @GetMapping("/greeting")
    public Map<String, String> greeting(
            //    name이 key값
            @RequestParam(defaultValue = "World") String name) {
        return Map.of("message", greetingService.hello(name));
    }
}