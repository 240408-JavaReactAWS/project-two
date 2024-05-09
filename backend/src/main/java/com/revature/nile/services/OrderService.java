package com.revature.nile.services;

import com.revature.nile.models.Item;
import com.revature.nile.models.Order;
import com.revature.nile.models.OrderItem;
import com.revature.nile.models.User;
import com.revature.nile.repositories.OrderItemRepository;
import com.revature.nile.repositories.OrderRepository;
import com.revature.nile.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrderService {
      private final OrderRepository orderRepository;
      private final UserRepository userRepository;
      private final OrderItemRepository orderItemRepository;

      @Autowired
      public OrderService(OrderRepository orderRepository, UserRepository userRepository, OrderItemRepository orderItemRepository) {
            this.orderRepository = orderRepository;
            this.userRepository = userRepository;
            this.orderItemRepository = orderItemRepository;
      }

      public Order createOrder(Order order) {
            return orderRepository.save(order);
      }

      public Order getOrderById(int orderId) {
            return orderRepository.findById(orderId).orElse(null);
      }

      public Order getCurrentOrderByUserId(int userId) {
            Optional<Order> currOrder = orderRepository.findByUserIdAndStatus(userId, "PENDING");
            if (currOrder.isPresent()) {
                  return currOrder.get();
            }
            else {
                  return null;
            }
            
      }

      public OrderItem createOrderItem(OrderItem orderItem) {
            return orderItemRepository.save(orderItem);
      }

      public User getUserByOrderId(int orderId) {
            Optional<Order> order = orderRepository.findById(orderId);
            if (order.isPresent()) {
                  return order.get().getUser();
            }
            return null;
      }

      public Order addOrderItemToCart(int userId, OrderItem orderItem) throws EntityNotFoundException {
            Optional<User> optionalUser = userRepository.findById(userId);
            if(optionalUser.isEmpty()) {
                  throw new EntityNotFoundException("User with id: " + userId + " doesn't exist");
            }
            User user = optionalUser.get();

            //Make sure a User has a current order. If not, make one!
            Order currOrder = this.getCurrentOrderByUserId(userId);
            if (currOrder == null) {
                  Order newOrder = new Order();
                  newOrder.setUser(user);
                  newOrder.setStatus(Order.StatusEnum.PENDING);
                  newOrder.setBillAddress(user.getAddress());
                  newOrder.setShipToAddress(user.getAddress());
                  newOrder.setOrderItems(new ArrayList<OrderItem>());
                  this.createOrder(newOrder);
                  currOrder = newOrder;
            }
            orderItem.setOrder(currOrder);

            //Edge case for if a User tries to add an Item to their cart when it's already in there
            //Instead of making a separate OrderItem for both, combine them!
            for(OrderItem cartItems : currOrder.getOrderItems()) { // if item id is already in cart, add quantity
                  if(cartItems.getItem().getItemId() == orderItem.getItem().getItemId()) {
                        if(cartItems.getQuantity() + orderItem.getQuantity() > cartItems.getItem().getStock()) {
                              throw new EntityNotFoundException("Not enough stock for item: " + cartItems.getItem().getItemId());
                        }
                        cartItems.setQuantity(cartItems.getQuantity() + orderItem.getQuantity());
                        orderItemRepository.save(cartItems);
                        return currOrder;
                  }
            }
            //Persist the OrderItem to the database
            this.createOrderItem(orderItem);
            //Add the OrderItem to the current Order
            currOrder.getOrderItems().add(orderItem);
            //Return the current order
            return currOrder;
      }
}
