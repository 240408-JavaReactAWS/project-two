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

@Configuration
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

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

    public void sendEmail(String email, String subject, String body) {
        // Send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }

}
