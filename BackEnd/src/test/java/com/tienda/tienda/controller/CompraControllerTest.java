package com.tienda.tienda.controller;

import com.tienda.tienda.model.Compra;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.model.User;
import com.tienda.tienda.service.CompraService;
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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CompraControllerTest {

    @Mock
    private CompraService compraService;

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
    
}
