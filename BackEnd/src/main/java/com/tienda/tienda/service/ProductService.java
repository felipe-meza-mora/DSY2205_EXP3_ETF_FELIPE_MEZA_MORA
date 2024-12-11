package com.tienda.tienda.service;

import com.tienda.tienda.model.Product;
import java.util.Optional;
import java.util.List;

public interface ProductService {
    // Método para guardar un producto
    Product saveProduct(Product product);
    // Método para obtener un producto por su id
    Optional<Product> getProductById(Long id);
    // Método para obtener todos los productos
    List<Product> getAllProducts();
    // Método para eliminar un producto
    void deleteProduct(Long id);
    // Método para actualizar un producto
    Optional<Product> updateProduct(Long id, Product updatedProduct);
    Product findById(Long id);
}