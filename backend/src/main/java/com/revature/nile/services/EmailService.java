package com.revature.nile.services;

import com.revature.nile.models.Order;
import com.revature.nile.models.OrderItem;
import com.revature.nile.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

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
        message.setFrom("nileshoppingapp@gmail.com");
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }
}
