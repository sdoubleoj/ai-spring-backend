package com.sesac.aibackend.dto;

import com.sesac.aibackend.domain.Car;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CarRequest(
        @NotBlank String name,
        @NotBlank String brand,
        @Min(0) Long price,
        @NotBlank String color
) {
    public Car toEntity() {
        return Car.builder().name(name).brand(brand).price((long) price).color(color).build();
    }
}
