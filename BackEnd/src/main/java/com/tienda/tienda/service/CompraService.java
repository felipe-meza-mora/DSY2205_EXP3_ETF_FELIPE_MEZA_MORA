package com.tienda.tienda.service;

import com.tienda.tienda.model.Compra;

import java.util.List;
import java.util.Optional;

public interface CompraService {
    Compra saveCompra(Compra compra);
    Optional<Compra> findById(Long id);
    void deleteCompra(Long id);
    Optional<Compra> updateCompra(Long id, Compra updatedCompra);
    List<Compra> findByUsuarioId(Long usuarioId);
    List<Compra> findByProductoId(Long productId);
    List<Compra> getAllCompras();
}