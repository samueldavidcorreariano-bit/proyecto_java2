package com.parquesoft.ecommerce.repository;

import com.parquesoft.ecommerce.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT o FROM OrderEntity o WHERE o.customer.id = :customerId")
    List<OrderEntity> findByCustomerId(Long customerId);


    List<OrderEntity> findByStatus(String status);

    @Query("SELECT o FROM OrderEntity o WHERE o.totalAmount >= :minAmount")
    List<OrderEntity> findOrdersAboveAmount(@Param("minAmount") double minAmount);

    List<OrderEntity> id(Long id);
}