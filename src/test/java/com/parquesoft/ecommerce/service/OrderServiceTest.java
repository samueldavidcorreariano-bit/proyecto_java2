package com.parquesoft.ecommerce.service;

import com.parquesoft.ecommerce.entity.OrderEntity;

import com.parquesoft.ecommerce.repository.OrderRepository;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName(" Verificar que cuando el totalAmount es positivo (> 0), el pedido se guarde con estado 'PENDING' y se retorne la entidad persistida.")
    void testCreateOrder_Success() {

        OrderEntity validOrder = new OrderEntity();
        validOrder.setCustomerId(1L);
        validOrder.setTotalAmount(150.0);

        OrderEntity savedOrder = new OrderEntity();
        savedOrder.setId(10L);
        savedOrder.setCustomerId(1L);
        savedOrder.setTotalAmount(150.0);
        savedOrder.setStatus("PENDING");

        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedOrder);

        OrderEntity result = orderService.createOrder(validOrder);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(150.0, result.getTotalAmount());
        assertEquals("PENDING", result.getStatus());

        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    @DisplayName("Verificar que cuando el totalAmount sea menor o igual a cero (ej. 0.0 o -50.0), el metodo lance IllegalArgumentException y se verifique mediante verify(orderRepository, never()).save(any()) que NUNCA se intente guardar en base de datos")
    void testCreateOrder_ThrowsException_WhenTotalIsZeroOrNegative() {

        OrderEntity invalidOrder = new OrderEntity();
        invalidOrder.setCustomerId(1L);
        invalidOrder.setTotalAmount(-10.0);

        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(invalidOrder);
        });

        verify(orderRepository, never()).save(any(OrderEntity.class));
    }

    @Test
    @DisplayName(" Verificar el retorno exitoso de una orden existente al consultar por ID.")
    void testGetOrderById_Success() {
        Long searchId = 10L;

        OrderEntity searchOrder = new OrderEntity();
        searchOrder.setId(10L);
        searchOrder.setCustomerId(1L);
        searchOrder.setTotalAmount(250.0);
        searchOrder.setStatus("PENDING");

        when(orderRepository.findById(searchId)).thenReturn(Optional.of(searchOrder));

        Optional<OrderEntity> result = orderService.getOrderById(searchId);

        assertTrue(result.isPresent());
        assertEquals(searchId, result.get().getId());
        assertEquals(250.0, result.get().getTotalAmount());
        assertEquals("PENDING", result.get().getStatus());

        verify(orderRepository, times(1)).findById(searchId);
    }

    @Test
    @DisplayName("Verificar que retorne Optional.empty() cuando se consulta un ID inexistente.")
    void testGetOrderById_NotFound() {
        Long orderId = 999L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

         Optional<OrderEntity> result = orderService.getOrderById(orderId);
         assertTrue(result.isEmpty());

        verify(orderRepository, times(1)).findById(orderId);
    }
}
