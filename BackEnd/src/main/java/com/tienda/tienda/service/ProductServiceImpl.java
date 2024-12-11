package com.tienda.tienda.service;

import com.tienda.tienda.model.Product;
import com.tienda.tienda.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Implementación del método para guardar un producto
    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Implementación del método para obtener un producto por su ID
    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Implementación del método para obtener todos los productos
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Implementación del método para eliminar un producto por su ID
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Implementación del método para actualizar un producto
    @Override
    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        // Buscar si el producto existe
        Optional<Product> existingProduct = productRepository.findById(id);

        // Si el producto existe, actualizar sus detalles
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setNombre(updatedProduct.getNombre());
            product.setDescripcion(updatedProduct.getDescripcion());
            product.setPrecio(updatedProduct.getPrecio());
            product.setImagen(updatedProduct.getImagen());

            // Guardar el producto actualizado
            return Optional.of(productRepository.save(product));
        }
        
        // Si el producto no existe, retornar Optional vacío
        return Optional.empty();
    }

    // Implementación del método para obtener un producto por su ID
    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null); // Retorna null si no lo encuentra
    }
}
