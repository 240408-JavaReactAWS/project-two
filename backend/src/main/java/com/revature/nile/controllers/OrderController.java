package com.revature.nile.controllers;

import com.revature.nile.models.Order;
import com.revature.nile.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
//@CrossOrigin(origins = "http://", allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.PATCH})
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PatchMapping("/status")
    public ResponseEntity<Order> orderStatus(@RequestBody Order order) {
        int id = order.getOrderId();
        Order.StatusEnum status = order.getStatus();
        boolean returnedStatus = this.orderService.updateOrderStatus(id, status);

        return returnedStatus? (new ResponseEntity<>(HttpStatus.NO_CONTENT)) : (new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

}
