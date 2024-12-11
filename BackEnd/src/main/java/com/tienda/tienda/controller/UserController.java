package com.tienda.tienda.controller;

import com.tienda.tienda.model.User;
import com.tienda.tienda.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    private UserService userService;

    // Endpoint para registrar un usuario
    public boolean containsSQLInjection(String value) {
        // Lista de palabras clave comunes en inyecciones SQL
        String[] sqlInjectionKeywords = {"DROP", "DELETE", "INSERT", "UPDATE", "--", ";", "/*", "*/"};
        
        // Verifica si el valor contiene alguna palabra clave
        for (String keyword : sqlInjectionKeywords) {
            if (value != null && value.toUpperCase().contains(keyword)) {
                return true; // Si se encuentra una palabra clave, es una inyección SQL
            }
        }
        return false; // Si no se encuentran palabras clave, no es una inyección SQL
    }
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        // Validación rápida de inyecciones SQL en los campos relevantes
        if (containsSQLInjection(user.getCorreo()) || containsSQLInjection(user.getTelefono()) || containsSQLInjection(user.getDireccionEnvio())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error: Datos no válidos.");
            return ResponseEntity.badRequest().body(errorResponse); // Devuelve un error si se detecta inyección SQL
        }
    
        // Si no hay inyecciones SQL, guarda el usuario
        User savedUser = userService.saveUser(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario añadido correctamente: " + savedUser.getNombre());
        return ResponseEntity.ok(response); // Devuelve una respuesta exitosa
    }

    // Endpoint para obtener el usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserByUsername(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        Map<String, Object> response = new HashMap<>();
        
        if (user.isPresent()) {
            response.put("message", "Usuario encontrado: " + user.get().getNombre());
            response.put("user", user.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Usuario no encontrado");
            return ResponseEntity.status(404).body(response);
        }
    }

    // Endpoint para obtener todos los usuarios
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Se han encontrado " + users.size() + " usuarios.");
        response.put("users", users);
        
        return ResponseEntity.ok(response);
    }

    // Endpoint para actualizar un usuario
    @PutMapping("{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody User user) {
        // Lógica para actualizar el usuario
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuario actualizado correctamente");
        response.put("data", user); 
        return ResponseEntity.ok(response);
    }

    // Endpoint para eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            userService.deleteUser(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuario eliminado correctamente: " + user.get().getNombre());
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuario no encontrado para eliminar");
            return ResponseEntity.status(404).body(response);
        }
    }

    // Endpoint para el login
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User loginRequest) {
        Optional<User> user = userService.login(loginRequest.getCorreo(), loginRequest.getPassword());
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }
    // Endpoint para verificar si el RUT está registrado
    @PostMapping("/check-rut")
    public ResponseEntity<Boolean> isRutRegistered(@RequestBody Map<String, String> requestBody) {
        String rut = requestBody.get("rut");
        boolean exists = userService.isRutRegistered(rut);
        return ResponseEntity.ok(exists);
    }

    // Endpoint para verificar si el correo electrónico está registrado
    @PostMapping("/check-email")
    public ResponseEntity<Boolean> isEmailRegistered(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        boolean exists = userService.isEmailRegistered(email);
        return ResponseEntity.ok(exists);
    }

    // Endpoint para actualizar la contraseña de un usuario
    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        
        boolean updated = userService.updatePassword(email, newPassword);
        if (updated) {
            return ResponseEntity.ok("Contraseña actualizada correctamente.");
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado o error al actualizar la contraseña.");
        }
    }
}