package com.revature.nile.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@Table(name = "orders")
public class Order {

    @Id
    @Column(name ="orderId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    // @ManyToOne
    // @JoinColumn(nullable = false, name = "userId")
    // private int userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(nullable = false)
    private String shipToAddress;

    @Column(nullable = false)
    private String billAddress;

    @CreationTimestamp
    private Date dateOrdered;

    private enum StatusEnum {
        PENDING,
        APPROVED
    }

    public Order(int userId, StatusEnum status, String shipToAddress, String billAddress, Date dateOrdered) {
        // this.userId = userId;
        this.status = status;
        this.shipToAddress = shipToAddress;
        this.billAddress = billAddress;
        this.dateOrdered = dateOrdered;
    }
}
