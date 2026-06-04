package com.sesac.aibackend.controller;

import com.sesac.aibackend.domain.Car;
import com.sesac.aibackend.dto.CarRequest;
import com.sesac.aibackend.dto.CarResponse;
import com.sesac.aibackend.error.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/legacy/cars")
public class CarController {

    private final Map<Long, Car> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @GetMapping
    public List<CarResponse> list() {
        return storage.values().stream().map(CarResponse::from).toList();
    }

    @GetMapping("/{id}")
    public CarResponse get(@PathVariable Long id) {
        Car car = storage.get(id);
        if (car == null) {
            throw NotFoundException.of("car", id);
        }
        return CarResponse.from(car);
    }

    @PostMapping
    public ResponseEntity<CarResponse> create(@Valid @RequestBody CarRequest req) {
        long id = sequence.getAndIncrement();
        Car saved = Car.builder().id(id).name(req.name()).brand(req.brand()).price((long) req.price()).color(req.color()).build();
        storage.put(id, saved);
        return ResponseEntity.created(URI.create("/legacy/cars/" + id)).body(CarResponse.from(saved));
    }

    @PutMapping("/{id}")
    public CarResponse update(@PathVariable Long id, @Valid @RequestBody CarRequest req) {
        Car existing = storage.get(id);
        if (existing == null) {
            throw NotFoundException.of("car", id);
        }
        existing.setName(req.name());
        existing.setBrand(req.brand());
        existing.setPrice((long) req.price());
        existing.setColor(req.color());
        return CarResponse.from(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (storage.remove(id) == null) {
            throw NotFoundException.of("car", id);
        }
        return ResponseEntity.noContent().build();
    }
}
