package com.revature.nile.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.repository.Temporal;

import java.util.Date;

import static jakarta.persistence.TemporalType.DATE;

@Entity
@Table(name = "orders")
public class Order {

        public enum StatusEnum {
            PENDING,
            APPROVED,
            COMPLETED
        }

        // Data Fields
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int orderId;

        // Foreign
        @ManyToOne
        @JoinColumn(nullable = false, name = "user_id")
        @JsonBackReference
        private User user;

        private StatusEnum status;

        private String shipToAddress;
        private String billAddress;

        @CreationTimestamp
        private Date dateOrdered;



    // Constructors

    // no-args constructor
    public Order() {}
    // all-args constructor
    public Order(int orderId, User user, StatusEnum status, String shipToAddress, String billAddress, Date dateOrdered) {
        this.orderId = orderId;
        this.user = user;
        this.status = status;
        this.shipToAddress = shipToAddress;
        this.billAddress = billAddress;
        this.dateOrdered = dateOrdered;
    }


    // Getters and Setters

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getShipToAddress() {
        return shipToAddress;
    }

    public void setShipToAddress(String shipToAddress) {
        this.shipToAddress = shipToAddress;
    }

    public String getBillAddress() {
        return billAddress;
    }

    public void setBillAddress(String billAddress) {
        this.billAddress = billAddress;
    }

    public Date getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(Date dateOrdered) {
        this.dateOrdered = dateOrdered;
    }


}
