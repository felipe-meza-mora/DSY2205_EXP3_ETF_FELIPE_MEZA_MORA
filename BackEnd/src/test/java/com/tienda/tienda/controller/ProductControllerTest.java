package com.tienda.tienda.controller;

import com.tienda.tienda.model.Product;
import com.tienda.tienda.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setNombre("Producto de prueba");
        product.setPrecio(100.0);
        product.setDescripcion("Descripción de prueba");
        product.setImagen("imagen.png");
    }
    
    @SuppressWarnings("null")
    @Test
    void testAddProduct() {
        Product newProduct = new Product();
        newProduct.setNombre("Producto Nuevo");
        newProduct.setPrecio(100.0);
        newProduct.setDescripcion("Descripción del producto");
        newProduct.setImagen("nuevo_producto.png");

        // Simula la llamada al servicio
        when(productService.saveProduct(any(Product.class))).thenReturn(newProduct);

        // Llama al controlador y obtiene la respuesta
        ResponseEntity<Map<String, Object>> response = productController.addProduct(newProduct);

        // Verifica que el código de estado sea 200 OK
        assertEquals(200, response.getStatusCode().value());

        // Verifica que el mensaje de éxito sea el correcto
        assertEquals("Producto añadido correctamente", response.getBody().get("message"));

        // Verifica que el producto agregado sea el correcto
        Product savedProduct = (Product) response.getBody().get("product");
        assertNotNull(savedProduct);
        assertEquals("Producto Nuevo", savedProduct.getNombre());
    }
    
    @SuppressWarnings("null")
    @Test
    void testGetProductById() {
        Product product = new Product();
        product.setId(1L);
        product.setNombre("Producto Encontrado");
        product.setPrecio(50.0);
        product.setDescripcion("Descripción de producto");
        product.setImagen("producto_encontrado.png");

        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        ResponseEntity<Map<String, Object>> response = productController.getProductById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Producto encontrado: Producto Encontrado", response.getBody().get("message"));
        assertNotNull(response.getBody().get("product"));
    }
    
    @SuppressWarnings("null")
    @Test
    void testGetAllProducts() {
        Product product1 = new Product();
        product1.setNombre("Producto 1");
        product1.setPrecio(50.0);

        Product product2 = new Product();
        product2.setNombre("Producto 2");
        product2.setPrecio(70.0);

        List<Product> products = List.of(product1, product2);

        when(productService.getAllProducts()).thenReturn(products);

        ResponseEntity<Map<String, Object>> response = productController.getAllProducts();

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Se han encontrado 2 productos.", response.getBody().get("message"));
        assertTrue(((List<Product>) response.getBody().get("products")).size() > 0);
    }

    @Test
    void testUpdateProduct() {
        // Crear el producto actualizado
        Product updatedProduct = new Product();
        updatedProduct.setNombre("Producto Actualizado");
        updatedProduct.setPrecio(120.0);

        // Crear el producto existente
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setNombre("Producto Existente");
        existingProduct.setPrecio(100.0);

        // Simula la respuesta del servicio
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(Optional.of(updatedProduct));

        // Llamada al controlador
        ResponseEntity<Map<String, Object>> response = productController.updateProduct(1L, updatedProduct);

        // Verifica que el código de respuesta sea 200 OK
        assertEquals(200, response.getStatusCode().value());

        // Verifica que el mensaje sea el esperado
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Producto actualizado correctamente", responseBody.get("message"));

        // Verifica que el producto actualizado esté en el cuerpo de la respuesta
        Product productInResponse = (Product) responseBody.get("product");
        assertNotNull(productInResponse);
        assertEquals("Producto Actualizado", productInResponse.getNombre());
        assertEquals(120.0, productInResponse.getPrecio());
    }


    @SuppressWarnings("null")
    @Test
    void testDeleteProduct() {
        // Crear un producto de prueba
        Product product = new Product();
        product.setId(1L);
        product.setNombre("Producto a Eliminar");

        // Configurar el mock para el servicio productService
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        // Llamar al endpoint deleteProduct
        ResponseEntity<Map<String, String>> response = productController.deleteProduct(1L);

        // Comprobar que el código de estado es 200 OK
        assertEquals(200, response.getStatusCode().value());

        // Comprobar que la respuesta contiene el mensaje esperado
        assertEquals("Producto eliminado correctamente: Producto a Eliminar", response.getBody().get("message"));
    }
    
    @SuppressWarnings("null")
    @Test
    void testGetProductByIdNotFound() {
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, Object>> response = productController.getProductById(1L);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Producto no encontrado", response.getBody().get("message"));
    }
    
    @SuppressWarnings("null")
    @Test
    void testGetAllProductsEmpty() {
        when(productService.getAllProducts()).thenReturn(new ArrayList<>());  // Lista vacía

        ResponseEntity<Map<String, Object>> response = productController.getAllProducts();

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Se han encontrado 0 productos.", response.getBody().get("message"));
    }

    @Test
    void testUpdateProductNotFound() {
        // Crear el producto actualizado
        Product updatedProduct = new Product();
        updatedProduct.setNombre("Nuevo Producto");
        updatedProduct.setPrecio(100.0);

        // Simula que el producto con ID 999 no se encuentra
        when(productService.updateProduct(eq(999L), any(Product.class))).thenReturn(Optional.empty()); // Producto no encontrado

        // Llamada al controlador
        ResponseEntity<Map<String, Object>> response = productController.updateProduct(999L, updatedProduct);

        // Verifica que el código de respuesta sea 404 Not Found
        assertEquals(404, response.getStatusCode().value());

        // Verifica que el mensaje sea el esperado
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Producto no encontrado para actualización", responseBody.get("message"));
    }
    
    @SuppressWarnings("null")
    @Test
    void testDeleteProductNotFound() {
        // Mockeamos el servicio para devolver un Optional vacío, simulando que el producto no existe
        when(productService.getProductById(999L)).thenReturn(Optional.empty());

        // Llamamos al endpoint deleteProduct
        ResponseEntity<Map<String, String>> response = productController.deleteProduct(999L);

        // Comprobamos que el código de estado es 404 Not Found
        assertEquals(404, response.getStatusCode().value());

        // Comprobamos que la respuesta contiene el mensaje esperado
        assertEquals("Producto no encontrado para eliminar", response.getBody().get("message"));
    }
    
    @SuppressWarnings("null")
    @Test
    void testDeleteProductSuccess() {
        // Creamos un producto de prueba
        Product product = new Product();
        product.setId(1L);
        product.setNombre("Producto A");

        // Configuramos el mock para el servicio productService, simulando que el producto fue encontrado
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        // Llamamos al endpoint deleteProduct
        ResponseEntity<Map<String, String>> response = productController.deleteProduct(1L);

        // Comprobamos que el código de estado es 200 OK
        assertEquals(200, response.getStatusCode().value());

        // Comprobamos que la respuesta contiene el mensaje esperado
        assertEquals("Producto eliminado correctamente: Producto A", response.getBody().get("message"));
    }

}
