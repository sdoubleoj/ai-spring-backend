package com.sesac.aibackend.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {

    private Long id;
    private String name;
    private String brand;
    private Long price;
    private String color;

}
