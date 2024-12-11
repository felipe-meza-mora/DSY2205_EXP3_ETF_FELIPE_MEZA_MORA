package com.tienda.tienda.controller;

import com.tienda.tienda.model.Product;
import com.tienda.tienda.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    @Autowired
    private ProductService productService;

    // Endpoint para agregar un producto
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addProduct(@Valid @RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Producto añadido correctamente");
        response.put("product", savedProduct);
        return ResponseEntity.ok(response);
    }

    // Endpoint para obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        Map<String, Object> response = new HashMap<>();

        if (product.isPresent()) {
            response.put("message", "Producto encontrado: " + product.get().getNombre());
            response.put("product", product.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Producto no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Asegúrate de usar HttpStatus.NOT_FOUND
        }
    }

    // Endpoint para obtener todos los productos
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Se han encontrado " + products.size() + " productos.");
        response.put("products", products);

        return ResponseEntity.ok(response);
    }

    // Endpoint para actualizar un producto
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Long id, @Valid @RequestBody Product updatedProduct) {
        Optional<Product> product = productService.updateProduct(id, updatedProduct);

        return product.map(p -> {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Producto actualizado correctamente");
            response.put("product", p);
            return ResponseEntity.ok(response);
        })
        .orElseGet(() -> {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Producto no encontrado para actualización");
            return ResponseEntity.status(404).body(response);
        });
    }
    

    // Endpoint para eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        Map<String, String> response = new HashMap<>();
        if (product.isPresent()) {
            productService.deleteProduct(id);
            response.put("message", "Producto eliminado correctamente: " + product.get().getNombre());
            return ResponseEntity.ok(response); // Devuelve una respuesta JSON con un mensaje
        } else {
            response.put("message", "Producto no encontrado para eliminar");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Respuesta con 404 y mensaje
        }
    }
}