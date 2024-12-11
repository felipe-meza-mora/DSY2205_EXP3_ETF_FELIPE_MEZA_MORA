package com.tienda.tienda.service;

import com.tienda.tienda.model.Compra;
import com.tienda.tienda.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompraServiceImpl implements CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Override
    public Compra saveCompra(Compra compra) {
        return compraRepository.save(compra);
    }

    @Override
    public Optional<Compra> findById(Long id) {
        return compraRepository.findById(id);
    }

    @Override
    public void deleteCompra(Long id) {
        compraRepository.deleteById(id);
    }

    @Override
    public Optional<Compra> updateCompra(Long id, Compra updatedCompra) {
        Optional<Compra> existingCompra = compraRepository.findById(id);
        if (existingCompra.isPresent()) {
            Compra compra = existingCompra.get();

            // Validación para evitar sobrescribir con nulos
            if (updatedCompra.getUsuario() != null) {
                compra.setUsuario(updatedCompra.getUsuario());
            }
            if (updatedCompra.getProducto() != null) {
                compra.setProducto(updatedCompra.getProducto());
            }
            if (updatedCompra.getCantidad() != null) {
                compra.setCantidad(updatedCompra.getCantidad());
            }
            if (updatedCompra.getFechaCompra() != null) {
                compra.setFechaCompra(updatedCompra.getFechaCompra());
            }
            if (updatedCompra.getTotal() != null) {
                compra.setTotal(updatedCompra.getTotal());
            }
            if (updatedCompra.getEstado() != null) {
                compra.setEstado(updatedCompra.getEstado());
            }
            return Optional.of(compraRepository.save(compra));
        }
        return Optional.empty();
    }

    @Override
    public List<Compra> findByUsuarioId(Long usuarioId) {
        return compraRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Compra> findByProductoId(Long productId) {
        // Necesitamos implementar este método en CompraRepository
        // Asumiendo que existe una consulta `findByProductoId` definida
        return compraRepository.findByProductoId(productId);
    }

    @Override
    public List<Compra> getAllCompras() {
        return compraRepository.findAll();
    }
}