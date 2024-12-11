package com.tienda.tienda.repository;

import com.tienda.tienda.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    // Método para encontrar un usuario por su RUT
    Optional<User> findByRut(String rut);

    // Método para encontrar un usuario por su correo electrónico
    Optional<User> findByCorreo(String correo);
}