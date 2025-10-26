package org.example.controllers;

import org.example.daos.OrderDao;
import org.example.models.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for the orders endpoint.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderDao orderDao;

    public OrderController(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    /**
     * GET /orders - Retrieves all orders.
     */
    @GetMapping
    public List<Order> getAllOrders() {
        return orderDao.getAll();
    }

    /**
     * GET /orders/{id} - Retrieves an order by ID.
     */
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable int id) {
        Order order = orderDao.getById(id);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return order;
    }

    /**
     * POST /orders - Creates a new order.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@RequestBody Order order) {
        try {
            Order created = orderDao.create(order);
            if (created == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Order creation failed");
            }
            return created;
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username");
        }
    }

    /**
     * PUT /orders/{id} - Updates an existing order.
     */
    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable int id, @RequestBody Order order) {
        order.setId(id);
        try {
            Order updated = orderDao.update(order);
            if (updated == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
            }
            return updated;
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username");
        }
    }

    /**
     * DELETE /orders/{id} - Deletes an order by ID.
     */
    @DeleteMapping("/{id}")
    public int deleteOrder(@PathVariable int id) {
        int rowsAffected = orderDao.delete(id);
        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return rowsAffected;
    }
}
