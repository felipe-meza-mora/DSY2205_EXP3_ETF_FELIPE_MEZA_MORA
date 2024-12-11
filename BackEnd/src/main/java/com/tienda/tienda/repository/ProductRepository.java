package com.tienda.tienda.repository;

import com.tienda.tienda.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
