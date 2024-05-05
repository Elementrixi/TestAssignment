package com.alpashaev.testAssignment;

import com.alpashaev.testAssignment.Controller.UserController;
import com.alpashaev.testAssignment.entities.User;
import com.alpashaev.testAssignment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "123456789");
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        ResponseEntity<User> response = userController.createUser(testUser);

        assertEquals(testUser, response.getBody());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        ResponseEntity<User> response = userController.updateUser(1L, testUser);

        assertEquals(testUser, response.getBody());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.updateUser(1L, testUser);

        assertNull(response.getBody());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testSearchUsersByBirthDateRange_Success() {
        List<User> users = new ArrayList<>();
        users.add(testUser);

        when(userRepository.findByBirthDateBetween(any(Date.class), any(Date.class))).thenReturn(users);

        ResponseEntity<List<User>> response = userController.searchUsersByBirthDateRange(new Date(), new Date());

        assertEquals(users, response.getBody());
        verify(userRepository, times(1)).findByBirthDateBetween(any(Date.class), any(Date.class));
    }

    @Test
    void testSearchUsersByBirthDateRange_EmptyArray() {
        ResponseEntity<List<User>> response = userController.searchUsersByBirthDateRange(new Date(), new Date());

        assertEquals(new ArrayList<>(), response.getBody());
    }

}

