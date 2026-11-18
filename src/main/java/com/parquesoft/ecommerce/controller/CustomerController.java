package com.parquesoft.ecommerce.controller;

import com.parquesoft.ecommerce.entity.CustomerEntity;
import com.parquesoft.ecommerce.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "5. Clientes", description = "Endpoints para gestion de los clientes")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerEntity>> getAll(){
        return ResponseEntity.ok(customerService.getAllCustomer());
    }

    @PostMapping
    @Operation(summary = "Registrar cliente", description = "Crea un nuevo cliente validando que el correo no exista")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o correo ya registrado")
    })
    public ResponseEntity<CustomerEntity> createCustomer(@RequestBody CustomerEntity customer) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
