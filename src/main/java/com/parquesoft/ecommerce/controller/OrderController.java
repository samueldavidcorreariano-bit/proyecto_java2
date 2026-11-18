package com.parquesoft.ecommerce.controller;


import com.parquesoft.ecommerce.entity.OrderEntity;
import com.parquesoft.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "4. Ordenes", description = "Endpoints para ordenes")
@SecurityRequirement(name = "BearerAuth")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Crear nueva orden", description = "Crear una nueva orden de compra")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Orden creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalido")
    })
    public ResponseEntity<OrderEntity> createOrder(@RequestBody OrderEntity orden) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orden));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Listar ordenes", description = "Listar todas las ordenes registradas")
    @ApiResponse(responseCode = "200", description = "Ordenes obtenidas exitosamente")
    public ResponseEntity<List<OrderEntity>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar orden por ID",description = "Obtener el detalle de una orden por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden encontrada"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    public ResponseEntity<OrderEntity> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Buscar orden por CustomerID",description = "Consultar pedidos de un cliente especifico")
    @ApiResponse(responseCode = "200", description = "Ordenes obtenidas exitosamente")
    public ResponseEntity<List<OrderEntity>> getOrderByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar orden por Status",description = "Filtrar pedidos segun su estado")
    @ApiResponse(responseCode = "200", description = "Ordenes obtenidas exitosamente")
    public ResponseEntity<List<OrderEntity>> getOrderByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Actualizar estado",description = "Actualizar el estado del pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden actualizada"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    public ResponseEntity<OrderEntity> updateOrderStatus(@PathVariable Long id,@RequestParam String status) {
        try {
            return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
