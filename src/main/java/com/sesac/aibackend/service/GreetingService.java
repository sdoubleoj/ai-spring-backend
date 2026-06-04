package com.sesac.aibackend.service;

import com.sesac.aibackend.util.MessageFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // = 필수 항목은 생성자가 만들어라(필수 객체는 직접 만들어라. 의존성 주입은 직접 해라?)
public class GreetingService {

    private final MessageFormatter formatter; // final을 통해서 MessageFormatter에 @RequiredArgsConstructor를 통해서 MessageFormatter의 formatter 의존성 주입

    public String hello(String name) {
        return formatter.format(name);
    }
}