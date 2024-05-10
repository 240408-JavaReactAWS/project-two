package com.revature.nile.services;

import com.revature.nile.models.Item;
import com.revature.nile.models.Order;
import com.revature.nile.models.OrderItem;
import com.revature.nile.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.text.DecimalFormat;
import java.util.List;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
@Configuration
@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    //Email to be send to seller
    public void sendNotificationToSeller(User user, Order order) {
        // Send email
        String email = user.getEmail();
        String subject = "New Order Notification for Order #" + order.getOrderId();

        String buyerEmail = order.getUser().getEmail();

        StringBuilder body = new StringBuilder();
        body.append("Hello, " + user.getFirstName() + " " + user.getLastName() + ",\n\n");
        body.append("You have a new order from " + buyerEmail + " for the following items:\n\n");
        body.append("Shipping Address: " + order.getShipToAddress() + "\n\n\n");

        DecimalFormat df = new DecimalFormat("#.00");
        double totalAmount= 0.0;
        for(OrderItem orderitem : order.getOrderItems()){
            Item item = orderitem.getItem();
            body.append("Item: " + item.getName() + "\n\n");
            body.append("Quantity: " + orderitem.getQuantity() + "\n\n");
            body.append("Price: $" + item.getPrice() + "\n\n");
            body.append("Total: $" + df.format(item.getPrice() * orderitem.getQuantity())+ "\n\n");
            totalAmount += item.getPrice() * orderitem.getQuantity();
            body.append("------------------------------------------------\n\n");
        }
        body.append("Total Order Price: $" + df.format(totalAmount)+ "\n\n\n");
        body.append("Items will be shipped to the following address: " + order.getShipToAddress() + "\n\n\n");
        body.append("Thank you for using Nile!");
        sendEmail(email, subject, body.toString());

    }

    //Email to be sent to buyer
    public void prepareCheckoutEmail(User user, Order order) {
        // Send email
        String email = user.getEmail();
        String subject = "Order Confirmation for Order #" + order.getOrderId();
        String body = "Thank you for shopping with Nile! Your order is being processed and will be shipped soon.\n\n";
        double total = 0;
        DecimalFormat df = new DecimalFormat("#.00");
        for(OrderItem orderItem : order.getOrderItems()) {
            body = body.concat(orderItem.getItem().getName() + " x" + orderItem.getQuantity() + ": $"+ df.format(orderItem.getQuantity() * orderItem.getItem().getPrice()) + "\n");
            total += orderItem.getItem().getPrice() * orderItem.getQuantity();
        }
        body += "\nTotal: $" + df.format(total) + "\n\n";
        body += "Shipping address:" + order.getShipToAddress() + "\n";
        body += "Billing address:" + order.getBillAddress() + "\n";
        this.sendEmail(email, subject, body);
    }


    public void sendEmail(String email, String subject, String body) {
        // Send email
        SimpleMailMessage message = new SimpleMailMessage();
        //This was previously hardcoded to nileshoppingapp@gmail.com. Change it back if need be!
        message.setFrom(senderEmail);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }
}
