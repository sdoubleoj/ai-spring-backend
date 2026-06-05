package com.sesac.aibackend.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Part {

    private String partNumber;
    private String name;
    private String category;
    private String supplier;
    private String unit;
    private int stock;
    private int safetyStock;
    private String warehouseLocation;
}
