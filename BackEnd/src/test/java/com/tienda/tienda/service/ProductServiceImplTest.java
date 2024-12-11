package com.tienda.tienda.service;

import com.tienda.tienda.model.Product;
import com.tienda.tienda.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveProduct() {
        // Arrange
        Product product = new Product();
        product.setNombre("Producto A");
        product.setPrecio(100.0);

        when(productRepository.save(product)).thenReturn(product);

        // Act
        Product savedProduct = productService.saveProduct(product);

        // Assert
        assertNotNull(savedProduct);
        assertEquals("Producto A", savedProduct.getNombre());
        assertEquals(100.0, savedProduct.getPrecio());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testGetProductById_ProductExists() {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setNombre("Producto B");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        Optional<Product> foundProduct = productService.getProductById(productId);

        // Assert
        assertTrue(foundProduct.isPresent());
        assertEquals("Producto B", foundProduct.get().getNombre());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testGetProductById_ProductDoesNotExist() {
        // Arrange
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        Optional<Product> foundProduct = productService.getProductById(productId);

        // Assert
        assertFalse(foundProduct.isPresent());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        Product product1 = new Product();
        product1.setNombre("Producto A");

        Product product2 = new Product();
        product2.setNombre("Producto B");

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        // Act
        List<Product> products = productService.getAllProducts();

        // Assert
        assertEquals(2, products.size());
        assertEquals("Producto A", products.get(0).getNombre());
        assertEquals("Producto B", products.get(1).getNombre());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testDeleteProduct() {
        // Arrange
        Long productId = 1L;

        doNothing().when(productRepository).deleteById(productId);

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testUpdateProduct_ProductExists() {
        // Arrange
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setNombre("Producto A");

        Product updatedProduct = new Product();
        updatedProduct.setNombre("Producto A Updated");
        updatedProduct.setPrecio(200.0);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        // Act
        Optional<Product> result = productService.updateProduct(productId, updatedProduct);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Producto A Updated", result.get().getNombre());
        assertEquals(200.0, result.get().getPrecio());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void testUpdateProduct_ProductDoesNotExist() {
        // Arrange
        Long productId = 1L;
        Product updatedProduct = new Product();
        updatedProduct.setNombre("Producto A Updated");

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        Optional<Product> result = productService.updateProduct(productId, updatedProduct);

        // Assert
        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any());
    }
}
