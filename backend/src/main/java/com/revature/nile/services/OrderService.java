package com.revature.nile.services;

import com.revature.nile.exceptions.NullAddressException;
import com.revature.nile.exceptions.OrderProcessingException;
import com.revature.nile.exceptions.UserIdMissMatchException;
import com.revature.nile.exceptions.UserNotFoundException;
import com.revature.nile.models.Item;
import com.revature.nile.models.Order;
import com.revature.nile.models.OrderItem;
import com.revature.nile.models.User;
import com.revature.nile.repositories.ItemRepository;
import com.revature.nile.repositories.OrderItemRepository;
import com.revature.nile.repositories.OrderRepository;
import com.revature.nile.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrderService {
      private final OrderRepository orderRepository;
      private final UserRepository userRepository;
      private final ItemRepository itemRepository;
      private final OrderItemRepository orderItemRepository;

      @Autowired
      public OrderService(OrderRepository orderRepository, UserRepository userRepository, ItemRepository itemRepository, OrderItemRepository orderItemRepository) {
            this.orderRepository = orderRepository;
            this.userRepository = userRepository;
            this.itemRepository = itemRepository;
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

      public void deleteOrderItem(int orderItemId) {
            Optional<OrderItem> orderItem = orderItemRepository.findById(orderItemId);
            if (orderItem.isPresent()) {
                  orderItemRepository.delete(orderItem.get());
            }
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

      public Order cartCheckout(int userId, Order order, int loggedInUser){
            //Can't checkout if the User ID doesn't match the logged in User
            if(userId != loggedInUser){
                  throw new UserIdMissMatchException("USER ID MISMATCH");
            }
            //Make sure the user exists!
            if (!userRepository.existsById(userId) || !userRepository.existsById(loggedInUser)){
                  throw new UserNotFoundException("NO SUCH USER");
            }
            //Make sure the Order's shipping and billing addresses are not empty
            if(order.getShipToAddress()==null || order.getShipToAddress().isEmpty() || order.getBillAddress()==null || order.getBillAddress().isEmpty()){
                  throw new NullAddressException("Shipping or Billing address is Empty");
            }
            //Check to make sure that the Order has no OrderItems with a quantity greater than the Item's stock
            List<OrderItem> invalidOrderItems = findInvalidOrderItem(userId);
            if (invalidOrderItems.isEmpty()) {

                  order.setStatus(Order.StatusEnum.APPROVED);
                  order.setDateOrdered(new Date());
                  for(OrderItem orderItem : order.getOrderItems()){ // update stock
                        Item item = orderItem.getItem();
                        item.setStock(item.getStock() - orderItem.getQuantity());
                        itemRepository.save(item);
                  }

                  orderRepository.save(order);
                  return order;
            }
            else{
                  String errorMessage = "Not enough stock for item(s):";
                  for(OrderItem orderItem : invalidOrderItems){
                        errorMessage += " " + orderItem.getItem().getName() + ": " +
                                    orderItem.getQuantity() + " in cart, " +
                                    orderItem.getItem().getStock() + " in stock.";
                  }
                  throw new OrderProcessingException(errorMessage);
            }
      }

      /*
       * This method searches a User's current Order for any OrderItems that have a quantity greater than the Item's stock.
       * If it finds one, it returns that OrderItem. Otherwise, it returns null.
       */
      public List<OrderItem> findInvalidOrderItem(int userId) {
            List<OrderItem> invalidOrderItems = new ArrayList<OrderItem>();
            Optional<Order> findOrder = orderRepository.findByUserIdAndStatus(userId, "PENDING");
            if (findOrder.isPresent()) {
                  //For each of the items in the order...
                  for (OrderItem orderItem : findOrder.get().getOrderItems()) {
                        //If the quantity of the OrderItem is greater than the Item's stock, return the OrderItem
                        if (orderItem.getQuantity() > orderItem.getItem().getStock()) {
                              invalidOrderItems.add(orderItem);
                        }
                  }
            }
            return invalidOrderItems;
      }
}
