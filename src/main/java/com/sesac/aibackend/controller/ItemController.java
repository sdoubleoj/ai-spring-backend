package com.sesac.aibackend.controller;

import com.sesac.aibackend.domain.Item;
import com.sesac.aibackend.dto.ItemRequest;
import com.sesac.aibackend.dto.ItemResponse;
import com.sesac.aibackend.error.NotFoundException;
import jakarta.validation.Valid;
//import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/legacy/items") // ItemController 객체가 무슨 경로로 들어올 때 최상위 어쩌구...
public class ItemController {

    private final Map<Long, Item> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    // 엔드포인트가 "/legacy/items"일 경우
    @GetMapping
    public List<ItemResponse> list() {
        return storage.values().stream().map(ItemResponse::from).toList();
    }

    // 엔드포인트가 예를 들어 "/legacy/items/1"일 경우
    @GetMapping("/{id}")
    public ItemResponse get(@PathVariable Long id) {
        Item item = storage.get(id); // Request가 오면, 해당 id가 storage에 있는지 찾음
        if (item == null) {
            throw NotFoundException.of("item", id);
        }
        return ItemResponse.from(item);
    }

    // ItemRequest에다가 검증할 수 있는(@Valid), 무언가를(@RequestBody)를 넣어서 전달?
    @PostMapping
    public ResponseEntity<ItemResponse> create(@Valid @RequestBody ItemRequest req) {
        long id = sequence.getAndIncrement(); //  id가 순차적으로 1씩 늘어남. (게시물 id 처럼)
        Item saved = Item.builder().id(id).name(req.name()).price(req.price()).build();
        storage.put(id, saved);
        return ResponseEntity.created(URI.create("/legacy/items/" + id)).body(ItemResponse.from(saved));
    }

    @PutMapping("/{id}")
    public ItemResponse update(@PathVariable Long id, @Valid @RequestBody ItemRequest req) {
        Item existing = storage.get(id);
        if (existing == null) {
            throw NotFoundException.of("item", id);
        }
        existing.setName(req.name());
        existing.setPrice(req.price());
        return ItemResponse.from(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (storage.remove(id) == null) {
            throw NotFoundException.of("item", id);
        }
        return ResponseEntity.noContent().build();
    }
}
