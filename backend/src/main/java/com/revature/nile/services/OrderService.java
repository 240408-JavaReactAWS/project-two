package com.revature.nile.services;

import com.revature.nile.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrderService {

      private final OrderRepository orderRepository;


      @Autowired
      public OrderService(OrderRepository orderRepository) {
            this.orderRepository = orderRepository;
      }


}