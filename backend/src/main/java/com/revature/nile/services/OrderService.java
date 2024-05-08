package com.revature.nile.services;

import com.revature.nile.models.Order;
import com.revature.nile.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class OrderService {

      private final OrderRepository orderRepository;


      @Autowired
      public OrderService(OrderRepository orderRepository) {
            this.orderRepository = orderRepository;
      }


      public boolean updateOrderStatus(int id, Order.StatusEnum status) {


            Optional<Order> orderOptional = this.orderRepository.findById(id);
            if (orderOptional.isPresent()) {
                  Order order = orderOptional.get();
                  order.setStatus(status);

                  this.orderRepository.save(order);
                  return true;
            }
            return false;
      }

}
