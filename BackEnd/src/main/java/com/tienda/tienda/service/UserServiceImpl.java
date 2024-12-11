package com.tienda.tienda.service;

import com.tienda.tienda.model.User;
import com.tienda.tienda.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    // Método para guardar un usuario
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Método para obtener todos los usuarios
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Método para obtener un usuario por su id
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    // Método para eliminar un usuario
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Método para actualizar un usuario
    @Override
    public Optional<User> updateUser(Long id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setNombre(updatedUser.getNombre());
            user.setPassword(updatedUser.getPassword());
            user.setPermisos(updatedUser.getPermisos());
            user.setRut(updatedUser.getRut());
            user.setCorreo(updatedUser.getCorreo());
            user.setTelefono(updatedUser.getTelefono());
            user.setDireccionEnvio(updatedUser.getDireccionEnvio());
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }

    // Método para el login
    @Override
    public Optional<User> login(String email, String password) {
        Optional<User> user = userRepository.findByCorreo(email);
        if (user.isPresent() && password != null && password.equals(user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    // Método para verificar si un RUT ya está registrado
    @Override
    public boolean isRutRegistered(String rut) {
        return userRepository.findByRut(rut).isPresent();
    }

    // Método para verificar si un correo electrónico ya está registrado
    @Override
    public boolean isEmailRegistered(String email) {
        return userRepository.findByCorreo(email).isPresent();
    }

    // Método para actualizar la contraseña de un usuario
    @Override
    public boolean updatePassword(String email, String newPassword) {
        Optional<User> user = userRepository.findByCorreo(email);
        if (user.isPresent()) {
            User userToUpdate = user.get();
            userToUpdate.setPassword(newPassword);
            userRepository.save(userToUpdate);
            return true;
        }
        return false;
    }

    
}