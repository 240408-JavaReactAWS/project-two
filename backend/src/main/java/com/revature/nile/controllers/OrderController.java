package com.revature.nile.controllers;

import com.revature.nile.models.Order;
import com.revature.nile.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /*
     * This function creates a new Order and stores it in the database.
     * This function does not require authorization.
     * The function takes in the Order object.
     * On success, the function returns the created Order.
     * TO-DO: This function should never be called directly by the user. An order will be
     * automatically created when a user adds an item to their current order.
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order newOrder = orderService.createOrder(order);
        return ResponseEntity.ok(newOrder);
    }

    /*
     * This function returns an order by its ID.
     * This function does not require authorization.
     * The function takes in the order ID.
     * On success, the function returns an OK status with the Order.
     * TO-DO: This function is currently not used to satisfy any user stories. Consider removing it.
     */
    @GetMapping("{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable int orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

}
