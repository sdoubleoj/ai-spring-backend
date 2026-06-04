package com.sesac.aibackend.dto;
import com.sesac.aibackend.domain.Car;

public record CarResponse(
        Long id,
        String name,
        String brand,
        Long price,
        String color
) {
    public static CarResponse from(Car car) {
        return new CarResponse(car.getId(), car.getName(), car.getBrand(), car.getPrice(), car.getColor());
    }
}
