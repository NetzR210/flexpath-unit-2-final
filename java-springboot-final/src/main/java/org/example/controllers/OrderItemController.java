package org.example.controllers;

import org.example.daos.OrderItemDao;
import org.example.models.OrderItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {
    private final OrderItemDao orderItemDao;

    public OrderItemController(OrderItemDao orderItemDao) {
        this.orderItemDao = orderItemDao;
    }

    private boolean isAuthenticated(Authentication auth) {
        return auth != null && auth.isAuthenticated();
    }

    @GetMapping
    public ResponseEntity<List<OrderItem>> getAllOrderItems(Authentication auth) {
        if (!isAuthenticated(auth)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(orderItemDao.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getOrderItem(@PathVariable int id, Authentication auth) {
        if (!isAuthenticated(auth)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        OrderItem item = orderItemDao.getById(id);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // returns 404 if not found
        }
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<OrderItem> createOrderItem(@RequestBody OrderItem item, Authentication auth) {
        if (!isAuthenticated(auth)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        OrderItem created = orderItemDao.create(item);
        if (created == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable int id, @RequestBody OrderItem item, Authentication auth) {
        if (!isAuthenticated(auth)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        item.setId(id);
        OrderItem updated = orderItemDao.update(item);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // returns 404 if not found
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteOrderItem(@PathVariable int id, Authentication auth) {
        if (!isAuthenticated(auth)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        OrderItem item = orderItemDao.getById(id);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // returns 404 if not found
        }
        int rows = orderItemDao.delete(id);
        if (rows == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(rows);
    }
}
