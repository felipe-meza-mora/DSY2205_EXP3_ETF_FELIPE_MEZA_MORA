package com.tienda.tienda.controller;

import com.tienda.tienda.model.User;
import com.tienda.tienda.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setNombre("Juan");
        user.setCorreo("juan@example.com");
        user.setPassword("password123");
    }
    @SuppressWarnings("null")
    @Test
    void testGetUserByUsername() {
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<Map<String, Object>> response = userController.getUserByUsername(1L);

        // Verificación adicional para asegurar que el cuerpo no sea nulo
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody().get("user"));
        assertEquals("Usuario encontrado: Juan", response.getBody().get("message"));
    }

    @Test
    void testUpdatePassword() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "juan@example.com");
        request.put("newPassword", "newpassword123");
        
        when(userService.updatePassword("juan@example.com", "newpassword123")).thenReturn(true);

        ResponseEntity<String> response = userController.updatePassword(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Contraseña actualizada correctamente.", response.getBody());
    }

    @Test
    void testDeleteUser() {
        // Preparar el mock del servicio para simular la búsqueda del usuario
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        // Llamar al método deleteUser del controlador
        ResponseEntity<Map<String, String>> response = userController.deleteUser(1L);

        // Verificar que la respuesta sea correcta
        assertEquals(200, response.getStatusCode().value());

        // Verificar que el mensaje en la respuesta sea el esperado
        assertEquals("Usuario eliminado correctamente: Juan", response.getBody().get("message"));
    }

    @SuppressWarnings("null")
    @Test
    void testLogin() {
        when(userService.login("juan@example.com", "password123")).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.login(user);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("juan@example.com", response.getBody().getCorreo());
    }
    
    @SuppressWarnings("null")
    @Test
    void testIsRutRegistered() {
        Map<String, String> request = new HashMap<>();
        request.put("rut", "12345678-9");

        when(userService.isRutRegistered("12345678-9")).thenReturn(true);

        ResponseEntity<Boolean> response = userController.isRutRegistered(request);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody());
    }
    
    @SuppressWarnings("null")
    @Test
    void testIsEmailRegistered() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "juan@example.com");

        when(userService.isEmailRegistered("juan@example.com")).thenReturn(true);

        ResponseEntity<Boolean> response = userController.isEmailRegistered(request);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody());
    }
    
    @SuppressWarnings("null")
    @Test
    void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(user); 
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<Map<String, Object>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Se han encontrado 1 usuarios.", response.getBody().get("message"));
        assertTrue(((List<User>) response.getBody().get("users")).size() > 0);  // Acast seguro a List<User>
    }

    @SuppressWarnings("null")
    @Test
    void testGetAllUsersEmpty() {
        when(userService.getAllUsers()).thenReturn(new ArrayList<>()); // Lista vacía

        ResponseEntity<Map<String, Object>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Se han encontrado 0 usuarios.", response.getBody().get("message"));
    }
    
    @SuppressWarnings("null")
    @Test
    void testDeleteUserNotFound() {
        // Simula que el usuario no existe
        when(userService.findById(1L)).thenReturn(Optional.empty());

        // Llamar al método deleteUser del controlador
        ResponseEntity<Map<String, String>> response = userController.deleteUser(1L);

        // Verificar que el código de estado sea 404
        assertEquals(404, response.getStatusCode().value());

        // Verificar que el mensaje de la respuesta sea el esperado
        assertEquals("Usuario no encontrado para eliminar", response.getBody().get("message"));
    }
    
    @SuppressWarnings("null")
    @Test
    void testGetAllUsersNoUsers() {
        when(userService.getAllUsers()).thenReturn(new ArrayList<>()); // Simula que no hay usuarios

        ResponseEntity<Map<String, Object>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Se han encontrado 0 usuarios.", response.getBody().get("message"));
    }

    @Test
    void testUpdatePasswordUserNotFound() {
        when(userService.updatePassword("nonexistent@example.com", "newpassword123")).thenReturn(false); // Usuario no encontrado

        Map<String, String> request = new HashMap<>();
        request.put("email", "nonexistent@example.com");
        request.put("newPassword", "newpassword123");

        ResponseEntity<String> response = userController.updatePassword(request);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Usuario no encontrado o error al actualizar la contraseña.", response.getBody());
    }

    @Test
    void testLoginFailed() {
        when(userService.login("juan@example.com", "wrongpassword")).thenReturn(Optional.empty()); // Login fallido

        ResponseEntity<User> response = userController.login(user);

        assertEquals(401, response.getStatusCode().value());
        assertNull(response.getBody());
    }
    
    @SuppressWarnings("null")
    @Test
    void testGetUserByUsernameNotFound() {
        when(userService.findById(999L)).thenReturn(Optional.empty()); // Caso donde el usuario no existe

        ResponseEntity<Map<String, Object>> response = userController.getUserByUsername(999L);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Usuario no encontrado", response.getBody().get("message"));
    }
    
    @SuppressWarnings("null")
    @Test
    void testGetAllUsersWithUsers() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setNombre("Juan");
        user1.setCorreo("juan@example.com");
        user1.setPassword("password123");
        user1.setPermisos("USER");
        user1.setRut("12345678-9");
        user1.setTelefono("123456789");
        user1.setDireccionEnvio("Direccion de prueba");

        User user2 = new User();
        user2.setNombre("Pedro");
        user2.setCorreo("pedro@example.com");
        user2.setPassword("password456");
        user2.setPermisos("USER");
        user2.setRut("12345678-9");
        user2.setTelefono("987654321");
        user2.setDireccionEnvio("Otra direccion de prueba");

        // Agregar dos usuarios a la lista
        users.add(user1);
        users.add(user2);

        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<Map<String, Object>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Se han encontrado 2 usuarios.", response.getBody().get("message"));
        assertTrue(((List<User>) response.getBody().get("users")).size() > 0);
    }
    
    @SuppressWarnings("null")
    @Test
    void testRegisterUser() {
        User user = new User();
        user.setNombre("Juan");
        user.setCorreo("juan@example.com");
        user.setPassword("password123");
        user.setPermisos("USER");
        user.setRut("12345678-9");
        user.setTelefono("123456789");
        user.setDireccionEnvio("Direccion de prueba");

        when(userService.saveUser(any(User.class))).thenReturn(user);

        ResponseEntity<Map<String, String>> response = userController.registerUser(user);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Usuario añadido correctamente: Juan", response.getBody().get("message"));
    }
   
    @SuppressWarnings("null")
    @Test
    void testUpdateUserWithLambda() {
        // Crea un usuario de ejemplo con los datos actualizados
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setNombre("Juan Actualizado");
        updatedUser.setCorreo("juan_updated@example.com");
        updatedUser.setPassword("newpassword123");
        updatedUser.setPermisos("ADMIN");
        updatedUser.setRut("12345678-9");
        updatedUser.setTelefono("123456789");
        updatedUser.setDireccionEnvio("Nueva dirección de prueba");

        // Simula la llamada al servicio y retorna el usuario actualizado
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(Optional.of(updatedUser));

        // Llama al controlador para actualizar el usuario
        ResponseEntity<Map<String, Object>> response = userController.updateUser(1L, updatedUser);

        // Verificaciones
        assertEquals(200, response.getStatusCode().value());  // Verifica que la respuesta sea 200 OK
        assertNotNull(response.getBody());  // Verifica que el cuerpo de la respuesta no esté vacío
        assertEquals("Usuario actualizado correctamente", response.getBody().get("message"));  // Verifica que el mensaje sea correcto
        assertEquals(updatedUser, response.getBody().get("data"));  // Verifica que los datos del usuario estén correctamente actualizados
    }
    
    @SuppressWarnings("null")
    @Test
    void testGetUserById() {
        // Datos de prueba
        User user = new User();
        user.setId(1L);
        user.setNombre("Juan");

        // Mock de userService.findById()
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        // Llamada al endpoint
        ResponseEntity<Map<String, Object>> response = userController.getUserByUsername(1L);

        // Verificación
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Usuario encontrado: Juan", response.getBody().get("message"));
    }
    
    @SuppressWarnings("null")
    @Test
    void testGetUserByIdNotFound() {
        // Mock de userService.findById() retornando vacío
        when(userService.findById(1L)).thenReturn(Optional.empty());

        // Llamada al endpoint
        ResponseEntity<Map<String, Object>> response = userController.getUserByUsername(1L);

        // Verificación
        assertEquals(404, response.getStatusCode().value());
        assertEquals("Usuario no encontrado", response.getBody().get("message"));
    }

}