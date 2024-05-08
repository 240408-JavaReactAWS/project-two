package com.revature.nile.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
// import lombok data
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.repository.Temporal;

import java.util.Date;
import java.util.Objects;

import static jakarta.persistence.TemporalType.DATE;


@Entity
@Table(name = "orders")
@Data
public class Order {

        public enum StatusEnum {
            PENDING,
            COMPLETED,
        }

        // Data Fields
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int orderId;

        // Foreign
        @ManyToOne
        @JoinColumn(nullable = false, name = "userId")
        private int userId;

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        private StatusEnum status;

        @Column(nullable = false)
        private String shipToAddress;

        @Column(nullable = false)
        private String billAddress;

        @CreationTimestamp
        private Date dateOrdered;




    // Constructors
//    public Order() {
//    }

    public Order(int userId, StatusEnum status, String shipToAddress, String billAddress, Date dateOrdered) {
        this.userId = userId;
        this.status = status;
        this.shipToAddress = shipToAddress;
        this.billAddress = billAddress;
        this.dateOrdered = dateOrdered;
    }


    public Order(int orderId, int userId, StatusEnum status, String shipToAddress, String billAddress, Date dateOrdered) {
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.shipToAddress = shipToAddress;
        this.billAddress = billAddress;
        this.dateOrdered = dateOrdered;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return this.orderId == order.orderId && this.userId == order.userId && this.status == order.status && Objects.equals(this.shipToAddress, order.shipToAddress) && Objects.equals(this.billAddress, order.billAddress) && Objects.equals(this.dateOrdered, order.dateOrdered);
    }




    @Override
    public int hashCode() {
        return Objects.hash(this.orderId, this.userId, this.status, this.shipToAddress, this.billAddress, this.dateOrdered);
    }

    @Override
    public String toString() {

        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", status=" + status +
                ", shipToAddress='" + shipToAddress + '\'' +
                ", billAddress='" + billAddress + '\'' +
                ", dateOrdered=" + dateOrdered +
                '}';
    }
}
