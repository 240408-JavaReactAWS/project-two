package com.revature.nile.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;
import java.util.List;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(nullable = false)
    private String shipToAddress;

    @Column(nullable = false)
    private String billAddress;

    @CreationTimestamp
    private Date dateOrdered;

    public enum StatusEnum {
        PENDING,
        APPROVED
    }

    // An order can have multiple items
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

    // An order can have only one user
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Order(StatusEnum status, String shipToAddress, String billAddress, Date dateOrdered) {
        this.status = status;
        this.shipToAddress = shipToAddress;
        this.billAddress = billAddress;
        this.dateOrdered = dateOrdered;
    }
}
