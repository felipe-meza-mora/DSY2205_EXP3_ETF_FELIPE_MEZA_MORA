package com.tienda.tienda.service;

import com.tienda.tienda.model.User;
import com.tienda.tienda.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser() {
        // Arrange
        User user = new User();
        user.setNombre("Juan");
        user.setCorreo("juan@example.com");

        when(userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = userService.saveUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals("Juan", savedUser.getNombre());
        assertEquals("juan@example.com", savedUser.getCorreo());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindById_UserExists() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setNombre("Carlos");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = userService.findById(userId);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("Carlos", foundUser.get().getNombre());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindById_UserDoesNotExist() {
        // Arrange
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> foundUser = userService.findById(userId);

        // Assert
        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testUpdateUser_UserExists() {
        // Arrange
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setNombre("Carlos");

        User updatedUser = new User();
        updatedUser.setNombre("Carlos Updated");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Act
        Optional<User> result = userService.updateUser(userId, updatedUser);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Carlos Updated", result.get().getNombre());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_UserDoesNotExist() {
        // Arrange
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setNombre("Carlos Updated");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.updateUser(userId, updatedUser);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setNombre("User1");
        User user2 = new User();
        user2.setNombre("User2");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testDeleteUser() {
        // Arrange
        Long userId = 1L;

        doNothing().when(userRepository).deleteById(userId);

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testLogin_ValidCredentials() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setCorreo(email);
        user.setPassword(password);

        when(userRepository.findByCorreo(email)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.login(email, password);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getCorreo());
        verify(userRepository, times(1)).findByCorreo(email);
    }

    @Test
    void testLogin_InvalidCredentials() {
        // Arrange
        String email = "test@example.com";
        String password = "wrongPassword";
        User user = new User();
        user.setCorreo(email);
        user.setPassword("password");

        when(userRepository.findByCorreo(email)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.login(email, password);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByCorreo(email);
    }

    @Test
    void testIsRutRegistered() {
        // Arrange
        String rut = "12345678-9";

        when(userRepository.findByRut(rut)).thenReturn(Optional.of(new User()));

        // Act
        boolean result = userService.isRutRegistered(rut);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).findByRut(rut);
    }

    @Test
    void testUpdatePassword_UserExists() {
        // Arrange
        String email = "test@example.com";
        String newPassword = "newPassword";
        User user = new User();
        user.setCorreo(email);

        when(userRepository.findByCorreo(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // Act
        boolean result = userService.updatePassword(email, newPassword);

        // Assert
        assertTrue(result);
        assertEquals(newPassword, user.getPassword());
        verify(userRepository, times(1)).findByCorreo(email);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdatePassword_UserDoesNotExist() {
        // Arrange
        String email = "test@example.com";
        String newPassword = "newPassword";

        when(userRepository.findByCorreo(email)).thenReturn(Optional.empty());

        // Act
        boolean result = userService.updatePassword(email, newPassword);

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findByCorreo(email);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testIsEmailRegistered_EmailExists() {
        // Arrange
        String email = "existing@example.com";

        when(userRepository.findByCorreo(email)).thenReturn(Optional.of(new User()));

        // Act
        boolean result = userService.isEmailRegistered(email);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).findByCorreo(email);
    }

    @Test
    void testIsEmailRegistered_EmailDoesNotExist() {
        // Arrange
        String email = "nonexistent@example.com";

        when(userRepository.findByCorreo(email)).thenReturn(Optional.empty());

        // Act
        boolean result = userService.isEmailRegistered(email);

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findByCorreo(email);
    }

    @Test
    void testLogin_UserWithNullPassword() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setCorreo(email);
        user.setPassword(null); // Contraseña nula

        when(userRepository.findByCorreo(email)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.login(email, password);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByCorreo(email);
    }

    @Test
    void testLogin_EmailExistsButPasswordDoesNotMatch() {
        // Arrange
        String email = "test@example.com";
        String wrongPassword = "wrongPassword";
        User user = new User();
        user.setCorreo(email);
        user.setPassword("correctPassword"); // Contraseña correcta

        when(userRepository.findByCorreo(email)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.login(email, wrongPassword);

        // Assert
        assertFalse(result.isPresent(), "El login no debe ser exitoso si la contraseña no coincide");
        verify(userRepository, times(1)).findByCorreo(email);
    }

    @Test
    void testLogin_NullPassword() {
        // Arrange
        String email = "test@example.com";
        String password = null; // Simulamos una contraseña nula.
        User user = new User();
        user.setCorreo(email);
        user.setPassword("securePassword");

        when(userRepository.findByCorreo(email)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.login(email, password);

        // Assert
        assertFalse(result.isPresent()); // Aseguramos que no devuelve ningún usuario.
        verify(userRepository, times(1)).findByCorreo(email);
    }
}
