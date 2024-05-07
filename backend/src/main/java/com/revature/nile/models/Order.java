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

        private enum StatusEnum {
            PENDING,
            APPROVED
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
    public Order() {
    }

    public Order(int userId, StatusEnum status, String shipToAddress, String billAddress, Date dateOrdered) {
        this.userId = userId;
        this.status = status;
        this.shipToAddress = shipToAddress;
        this.billAddress = billAddress;
        this.dateOrdered = dateOrdered;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return getOrderId() == order.getOrderId() && userId == order.userId && getStatus() == order.getStatus() && Objects.equals(getShipToAddress(), order.getShipToAddress()) && Objects.equals(getBillAddress(), order.getBillAddress()) && Objects.equals(getDateOrdered(), order.getDateOrdered());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), userId, getStatus(), getShipToAddress(), getBillAddress(), getDateOrdered());
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
