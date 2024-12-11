package com.tienda.tienda.service;

import com.tienda.tienda.model.Compra;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.model.User;
import com.tienda.tienda.repository.CompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CompraServiceImplTest {

    @Mock
    private CompraRepository compraRepository;

    @InjectMocks
    private CompraServiceImpl compraService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCompra() {
        Compra compra = new Compra();
        when(compraRepository.save(compra)).thenReturn(compra);

        Compra result = compraService.saveCompra(compra);

        assertNotNull(result);
        verify(compraRepository, times(1)).save(compra);
    }

    @Test
    void testUpdateCompra_ValidData() {
        Long compraId = 1L;
        Compra existingCompra = new Compra();
        existingCompra.setId(compraId);

        Compra updatedCompra = new Compra();
        updatedCompra.setUsuario(new User());
        updatedCompra.setProducto(new Product());
        updatedCompra.setCantidad(5);

        when(compraRepository.findById(compraId)).thenReturn(Optional.of(existingCompra));
        when(compraRepository.save(existingCompra)).thenReturn(existingCompra);

        Optional<Compra> result = compraService.updateCompra(compraId, updatedCompra);

        assertTrue(result.isPresent());
        assertEquals(5, result.get().getCantidad());
        verify(compraRepository, times(1)).findById(compraId);
        verify(compraRepository, times(1)).save(existingCompra);
    }

    @Test
    void testUpdateCompra_NullFields() {
        Long compraId = 1L;
        Compra existingCompra = new Compra();
        existingCompra.setId(compraId);
        existingCompra.setUsuario(new User());
        existingCompra.setProducto(new Product());
        existingCompra.setCantidad(3);

        Compra updatedCompra = new Compra();
        updatedCompra.setCantidad(null);

        when(compraRepository.findById(compraId)).thenReturn(Optional.of(existingCompra));
        when(compraRepository.save(existingCompra)).thenReturn(existingCompra);

        Optional<Compra> result = compraService.updateCompra(compraId, updatedCompra);

        assertTrue(result.isPresent());
        assertEquals(3, result.get().getCantidad());
        verify(compraRepository, times(1)).findById(compraId);
        verify(compraRepository, times(1)).save(existingCompra);
    }

    @Test
    void testUpdateCompra_NonExistentCompra() {
        Long compraId = 1L;
        Compra updatedCompra = new Compra();

        when(compraRepository.findById(compraId)).thenReturn(Optional.empty());

        Optional<Compra> result = compraService.updateCompra(compraId, updatedCompra);

        assertFalse(result.isPresent());
        verify(compraRepository, times(1)).findById(compraId);
        verify(compraRepository, never()).save(any());
    }

    @Test
    void testFindById() {
        Long compraId = 1L;
        Compra compra = new Compra();
        when(compraRepository.findById(compraId)).thenReturn(Optional.of(compra));

        Optional<Compra> result = compraService.findById(compraId);

        assertTrue(result.isPresent());
        verify(compraRepository, times(1)).findById(compraId);
    }

    @Test
    void testFindAll() {
        when(compraRepository.findAll()).thenReturn(List.of(new Compra(), new Compra()));

        List<Compra> result = compraService.getAllCompras();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(compraRepository, times(1)).findAll();
    }

    @Test
    void testDeleteCompra() {
        Long compraId = 1L;
        doNothing().when(compraRepository).deleteById(compraId);

        compraService.deleteCompra(compraId);

        verify(compraRepository, times(1)).deleteById(compraId);
    }

    @Test
    void testFindByUsuarioId() {
        Long usuarioId = 1L;
        when(compraRepository.findByUsuarioId(usuarioId)).thenReturn(List.of(new Compra()));

        List<Compra> result = compraService.findByUsuarioId(usuarioId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(compraRepository, times(1)).findByUsuarioId(usuarioId);
    }

    @Test
    void testFindByProductoId() {
        Long productoId = 1L;
        when(compraRepository.findByProductoId(productoId)).thenReturn(List.of(new Compra()));

        List<Compra> result = compraService.findByProductoId(productoId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(compraRepository, times(1)).findByProductoId(productoId);
    }

    @Test
    void testGetAllCompras() {
        when(compraRepository.findAll()).thenReturn(List.of(new Compra(), new Compra()));

        List<Compra> result = compraService.getAllCompras();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(compraRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCompra_NullFechaCompra() {
        // Arrange
        Long compraId = 1L;
        Compra existingCompra = new Compra();
        existingCompra.setId(compraId);
        existingCompra.setFechaCompra(new Date()); 

        Compra updatedCompra = new Compra();
        updatedCompra.setFechaCompra(null);

        when(compraRepository.findById(compraId)).thenReturn(Optional.of(existingCompra));
        when(compraRepository.save(any(Compra.class))).thenReturn(existingCompra);

        // Act
        Optional<Compra> result = compraService.updateCompra(compraId, updatedCompra);

        // Assert
        assertTrue(result.isPresent());
        assertNotNull(result.get().getFechaCompra()); 
        verify(compraRepository, times(1)).findById(compraId);
        verify(compraRepository, times(1)).save(existingCompra);
    }

    @Test
    void testUpdateCompra_SomeFieldsNull() {
        // Arrange
        Long compraId = 1L;
        Compra existingCompra = new Compra();
        existingCompra.setId(compraId);
        existingCompra.setUsuario(new User());
        existingCompra.setProducto(new Product());
        existingCompra.setCantidad(5);
        existingCompra.setFechaCompra(new Date());

        Compra updatedCompra = new Compra();
        updatedCompra.setUsuario(null);
        updatedCompra.setProducto(new Product());
        updatedCompra.setCantidad(null);
        updatedCompra.setFechaCompra(new Date());

        when(compraRepository.findById(compraId)).thenReturn(Optional.of(existingCompra));
        when(compraRepository.save(any(Compra.class))).thenReturn(existingCompra);

        // Act
        Optional<Compra> result = compraService.updateCompra(compraId, updatedCompra);

        // Assert
        assertTrue(result.isPresent());
        assertNotNull(result.get().getUsuario()); 
        assertNotNull(result.get().getProducto());
        assertEquals(5, result.get().getCantidad()); 
        verify(compraRepository, times(1)).save(existingCompra);
    }
}