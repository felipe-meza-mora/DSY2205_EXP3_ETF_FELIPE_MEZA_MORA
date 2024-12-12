package com.tienda.tienda.controller;

import com.tienda.tienda.model.Compra;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.model.User;
import com.tienda.tienda.service.UserService;
import com.tienda.tienda.service.CompraService;
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

class CompraControllerTest {

    @Mock
    private CompraService compraService;

    @Mock
    private UserService userService;


    @Mock
    private ProductService productService;

    @InjectMocks
    private CompraController compraController;

    private Compra compra;
    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Creación de datos de prueba
        user = new User();
        user.setId(1L);
        user.setNombre("Juan");

        product = new Product();
        product.setId(1L);
        product.setNombre("Producto 1");
        product.setPrecio(100.0);

        compra = new Compra();
        compra.setId(1L);
        compra.setUsuario(user);
        compra.setProducto(product);
        compra.setCantidad(2);
        compra.setFechaCompra(new java.util.Date());
        compra.setTotal(200.0);
        compra.setEstado("Enviado");
    }
    
    @SuppressWarnings("null")
    @Test
    void testGetComprasByUsuarioId() {
        List<Compra> compras = new ArrayList<>();
        compras.add(compra);

        when(compraService.findByUsuarioId(1L)).thenReturn(compras);

        ResponseEntity<Map<String, Object>> response = compraController.getComprasByUsuarioId(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Se encontraron 1 compras para el usuario con ID: 1", response.getBody().get("message"));
        assertNotNull(response.getBody().get("compras"));
        assertEquals(1, ((List<Compra>) response.getBody().get("compras")).size());
    }
    
    @SuppressWarnings("null")
    @Test
    void testDeleteCompra() {
        when(compraService.findById(1L)).thenReturn(Optional.of(compra));

        ResponseEntity<Map<String, String>> response = compraController.deleteCompra(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Compra eliminada correctamente.", response.getBody().get("message"));
        assertEquals("1", response.getBody().get("compraId"));
    }
    
    @SuppressWarnings("null")
    @Test
    void testGetAllCompras() {
        List<Compra> compras = new ArrayList<>();
        compras.add(compra);

        when(compraService.getAllCompras()).thenReturn(compras);

        ResponseEntity<Map<String, Object>> response = compraController.getAllCompras();

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Se encontraron 1 compras en total.", response.getBody().get("message"));
        assertNotNull(response.getBody().get("compras"));
        assertEquals(1, ((List<Compra>) response.getBody().get("compras")).size());
    }

    // Test de manejo de error: Compra no encontrada
    @SuppressWarnings("null")
    @Test
    void testGetComprasByUsuarioIdNotFound() {
        when(compraService.findByUsuarioId(999L)).thenReturn(new ArrayList<>());  // Usuario no tiene compras

        ResponseEntity<Map<String, Object>> response = compraController.getComprasByUsuarioId(999L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Se encontraron 0 compras para el usuario con ID: 999", response.getBody().get("message"));
    }

    @Test
    void testUpdateCompraSuccess() {
        Compra updatedCompra = new Compra();
        updatedCompra.setId(1L);
        updatedCompra.setEstado("Entregado");

        when(compraService.updateCompra(eq(1L), any(Compra.class))).thenReturn(Optional.of(updatedCompra));

        ResponseEntity<Compra> response = compraController.updateCompra(1L, updatedCompra);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Entregado", response.getBody().getEstado());
    }


    @Test
    void testUpdateCompraNotFound() {
        Compra compraActualizada = new Compra();
        compraActualizada.setId(999L);
        compraActualizada.setEstado("Cancelado");

        when(compraService.updateCompra(eq(999L), any(Compra.class))).thenReturn(Optional.empty());

        ResponseEntity<Compra> response = compraController.updateCompra(999L, compraActualizada);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testGetComprasByUsuarioIdMultipleCompras() {
        // Crear múltiples compras
        List<Compra> compras = new ArrayList<>();
        Compra compra2 = new Compra();
        compra2.setId(2L);
        compra2.setUsuario(user);
        compra2.setProducto(product);
        compra2.setTotal(300.0);

        compras.add(compra);
        compras.add(compra2);

        // Mock del servicio
        when(compraService.findByUsuarioId(user.getId())).thenReturn(compras);

        // Ejecutar el controlador
        ResponseEntity<Map<String, Object>> response = compraController.getComprasByUsuarioId(user.getId());

        // Validaciones
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Se encontraron 2 compras para el usuario con ID: 1", response.getBody().get("message"));
        assertNotNull(response.getBody().get("compras"));
        assertEquals(2, ((List<Compra>) response.getBody().get("compras")).size());
    }

    @Test
    void testUpdateCompraEstadoSuccess() {
        // Actualización exitosa
        Compra compraActualizada = new Compra();
        compraActualizada.setId(compra.getId());
        compraActualizada.setEstado("Entregado");

        // Mock del servicio
        when(compraService.updateCompra(eq(compra.getId()), any(Compra.class))).thenReturn(Optional.of(compraActualizada));

        // Ejecutar el controlador
        ResponseEntity<Compra> response = compraController.updateCompra(compra.getId(), compraActualizada);

        // Validaciones
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Entregado", response.getBody().getEstado());
    }

    @Test
    void testDeleteCompraSuccess() {
        // Mock de eliminación exitosa
        doNothing().when(compraService).deleteCompra(compra.getId());

        // Ejecutar el controlador
        ResponseEntity<Map<String, String>> response = compraController.deleteCompra(compra.getId());

        // Validaciones
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Compra eliminada correctamente.", response.getBody().get("message"));
        assertEquals(String.valueOf(compra.getId()), response.getBody().get("compraId"));
    }

    @Test
    void testUpdateCompraInvalidId() {
        // Intentar actualizar con ID inexistente
        Compra compraActualizada = new Compra();
        compraActualizada.setEstado("Entregado");
    
        when(compraService.updateCompra(eq(999L), any(Compra.class))).thenReturn(Optional.empty());
    
        ResponseEntity<Compra> response = compraController.updateCompra(999L, compraActualizada);
    
        // Validaciones
        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }
    
    @SuppressWarnings("null")
    @Test
    void testAddCompraUserNotFound() {
        Compra compra = new Compra();
        compra.setUsuario(new User());
        compra.getUsuario().setId(999L); // ID de usuario inexistente
        compra.setProducto(new Product());
        compra.getProducto().setId(1L);

        when(userService.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, Object>> response = compraController.addCompra(compra);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Usuario no encontrado", response.getBody().get("message"));
    }

    @SuppressWarnings("null")
    @Test
    void testAddCompraProductNotFound() {
        Compra compra = new Compra();
        compra.setUsuario(new User());
        compra.getUsuario().setId(1L);
        compra.setProducto(new Product());
        compra.getProducto().setId(999L); // ID de producto inexistente
    
        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(productService.getProductById(999L)).thenReturn(Optional.empty());
    
        ResponseEntity<Map<String, Object>> response = compraController.addCompra(compra);
    
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Producto no encontrado", response.getBody().get("message"));
    }
    
    @SuppressWarnings("null")
    @Test
    void testAddCompraSuccess() {
        Compra compra = new Compra();
        compra.setUsuario(new User());
        compra.getUsuario().setId(1L);
        compra.setProducto(new Product());
        compra.getProducto().setId(1L);

        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));
        when(compraService.saveCompra(any(Compra.class))).thenReturn(compra);

        ResponseEntity<Map<String, Object>> response = compraController.addCompra(compra);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Compra registrada correctamente.", response.getBody().get("message"));
        assertNotNull(response.getBody().get("compra"));
    }
    

}
