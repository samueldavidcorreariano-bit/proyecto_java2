package com.parquesoft.ecommerce.service;

import com.parquesoft.ecommerce.entity.OrderEntity;
import com.parquesoft.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional()
    public  OrderEntity createOrder(OrderEntity order){
        if (order.getTotalAmount() == null || order.getTotalAmount() <= 0) {
            throw new IllegalArgumentException("El monto total del pedido debe ser mayor a cero");
        }

        if (order.getOrderDate() == null){
            order.setOrderDate(LocalDateTime.now());
        }

        order.setStatus("PENDING");

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public  Optional<OrderEntity> getOrderById(Long id){
        return orderRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<OrderEntity> getOrdersByCustomerId(Long id){
        return orderRepository.findByCustomerId(id);
    }

    @Transactional(readOnly = true)
    public  List<OrderEntity> getOrdersByStatus(String status){
        return orderRepository.findByStatus(status);
    }

    @Transactional()
    public OrderEntity updateOrderStatus(Long id, String newStatus){
        OrderEntity existing = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada con ID: " + id));
        existing.setStatus(newStatus);
        return orderRepository.save(existing);
    }

    @Transactional()
    public void cancelOrder(Long id){
        OrderEntity existing = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada con ID: " + id));

        if ("SHIPPED".equalsIgnoreCase(existing.getStatus())){
            throw new IllegalStateException("La orden no puede ser cancelada en este momento");
        }
        existing.setStatus("CANCELLED");
        orderRepository.save(existing);
    }
}