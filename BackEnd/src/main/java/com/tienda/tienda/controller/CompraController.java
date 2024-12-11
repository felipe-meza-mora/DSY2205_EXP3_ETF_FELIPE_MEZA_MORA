package com.tienda.tienda.controller;

import com.tienda.tienda.model.Compra;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.model.User;
import com.tienda.tienda.service.CompraService;
import com.tienda.tienda.service.ProductService;
import com.tienda.tienda.service.UserService;

import jakarta.validation.Valid;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "http://localhost:4200")
public class CompraController {

    @Autowired
    private CompraService compraService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    // Endpoint para registrar una compra
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addCompra(@Valid @RequestBody Compra compra) {
        Optional<User> userOptional = userService.findById(compra.getUsuario().getId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Usuario no encontrado"));
        }
        compra.setUsuario(userOptional.get());

        Optional<Product> productOptional = productService.getProductById(compra.getProducto().getId());
        if (productOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Producto no encontrado"));
        }
        compra.setProducto(productOptional.get());

        Compra savedCompra = compraService.saveCompra(compra);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Compra registrada correctamente.");
        response.put("compra", savedCompra);

        return ResponseEntity.ok(response);
    }

    // Endpoint para obtener el historial de compras de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> getComprasByUsuarioId(@PathVariable Long usuarioId) {
        List<Compra> compras = compraService.findByUsuarioId(usuarioId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Se encontraron " + compras.size() + " compras para el usuario con ID: " + usuarioId);
        response.put("compras", compras);

        return ResponseEntity.ok(response);
    }

    // Endpoint para eliminar una compra por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCompra(@PathVariable Long id) {
        compraService.deleteCompra(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Compra eliminada correctamente.");
        response.put("compraId", String.valueOf(id));

        return ResponseEntity.ok(response);
    }

    // Endpoint para obtener todas las compras
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCompras() {
        List<Compra> compras = compraService.getAllCompras();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Se encontraron " + compras.size() + " compras en total.");
        response.put("compras", compras);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
     public ResponseEntity<Compra> updateCompra(@PathVariable Long id, @RequestBody Compra updateCompra) {
        Optional<Compra> updatedCompra = compraService.updateCompra(id, updateCompra);
        
        if (updatedCompra.isPresent()) {
            // Si la compra se actualizó con éxito, devolver la compra actualizada
            return ResponseEntity.ok(updatedCompra.get());
        } else {
            // Si la compra no existe, devolver un error 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
