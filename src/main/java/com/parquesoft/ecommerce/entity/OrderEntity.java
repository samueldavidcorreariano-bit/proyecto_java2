package com.parquesoft.ecommerce.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id",nullable = false)
    private CustomerEntity customer;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount = 0.0;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING";

    public OrderEntity() {
    }

    public OrderEntity(Long id, CustomerEntity customer, LocalDateTime orderDate, Double totalAmount, String status) {
        this.id = id;
        this.customer = customer;
        this.orderDate = (orderDate != null) ? orderDate : LocalDateTime.now();
        this.totalAmount = (totalAmount != null) ? totalAmount : 0.0;
        this.status = (status != null && !status.isBlank()) ? status : "PENDING";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Long getCustomerId() {
        return (customer != null) ? customer.getId() : null;
    }

    public void setCustomerId(Long id) {
        if (id != null) {
            CustomerEntity customer = new CustomerEntity();
            customer.setId(id);
            this.customer = customer;
        } else {
            this.customer = null;
        }
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
