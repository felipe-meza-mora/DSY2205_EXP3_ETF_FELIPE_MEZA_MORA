package com.tienda.tienda.service;

import com.tienda.tienda.model.User;
import java.util.Optional;
import java.util.List;

public interface UserService {

    // Método para guardar un usuario
    User saveUser(User user);

    // Método para obtener todos los usuarios
    List<User> getAllUsers();
    
    // Método para eliminar un usuario
    void deleteUser(Long id);
    
    // Método para obtener un usuario por su id
    Optional<User> findById(Long id);
    
    // Método para actualizar un usuario
    Optional<User> updateUser(Long id, User updatedUser);
    
    // Método para el login
    Optional<User> login(String email, String password);
    
    // Método para verificar si un RUT ya está registrado
    boolean isRutRegistered(String rut);
    
    // Método para verificar si un correo electrónico ya está registrado
    boolean isEmailRegistered(String email);
    
    // Método para actualizar la contraseña de un usuario
    boolean updatePassword(String email, String newPassword);

    
}