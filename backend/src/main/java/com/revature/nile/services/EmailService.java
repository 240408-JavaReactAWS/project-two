package com.revature.nile.services;

import com.revature.nile.models.Item;
import com.revature.nile.models.Order;
import com.revature.nile.models.OrderItem;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.List;

@Configuration
public class EmailService {
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendNotificationToSeller(String sellerEmail, String subject, OrderItem orderItem) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String htmlContent = generateHtmlContent(orderItem);

        try {
            helper.setTo(sellerEmail);
            helper.setFrom(senderEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateHtmlContent(OrderItem orderItem) {
        StringBuilder itemsHtml = new StringBuilder();
        List<OrderItem> orderItems = orderItem.getItem().getOrderItems();
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem item = orderItems.get(i);
            String backgroundColor = i % 2 == 0 ? "#f0f0f0" : "#c6ced1"; // Alternate row color

            itemsHtml.append("<tr style=\"background-color:").append(backgroundColor).append(";\">")
                    .append("<td>").append(item.getItem().getName()).append("</td>")
                    .append("<td>").append(item.getQuantity()).append("</td>")
                    .append("</tr>");
        }

        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Order Notification</title>\n" +
                "    <style>\n" +
                "        h1 {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        th, td {\n" +
                "            text-align: center;\n" +
                "            font-size: 14px;\n" +
                "            padding: 10px;\n" +
                "            width: 300px;\n" +
                "        }\n" +
                "        tbody tr:nth-child(odd) {\n" +
                "            background-color: #f0f0f0;\n" +
                "        }\n" +
                "        tbody tr:nth-child(even) {\n" +
                "            background-color: #c6ced1;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h2>Order Notification</h2>\n" +
                "        <p>Hello Seller,</p>\n" +
                "        <p>You have received a new order. Details are as follows:</p>\n" +
                "        <table>\n" +
                "            <thead>\n" +
                "                <tr>\n" +
                "                    <th>Item Name</th>\n" +
                "                    <th>Quantity</th>\n" +
                "                </tr>\n" +
                "            </thead>\n" +
                "            <tbody>\n" +
                                itemsHtml.toString() +
                "            </tbody>\n" +
                "        </table>\n" +
                "        <p>Thank you for your attention.</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

}
