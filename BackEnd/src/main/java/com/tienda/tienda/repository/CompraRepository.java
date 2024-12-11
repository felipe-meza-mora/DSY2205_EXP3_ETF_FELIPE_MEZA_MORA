package com.tienda.tienda.repository;

import com.tienda.tienda.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByUsuarioId(Long userId);
    List<Compra> findByProductoId(Long productId);
    
}