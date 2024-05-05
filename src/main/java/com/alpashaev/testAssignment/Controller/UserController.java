package com.alpashaev.testAssignment.Controller;

import com.alpashaev.testAssignment.entities.User;
import com.alpashaev.testAssignment.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    UserRepository userRepository;

    @Value("${minimum_Age}")
    private int minimumAge;


    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        LocalDate currentDate = LocalDate.now();
        int age = calculateAge(user.getBirthDate(), currentDate);
        if (age < minimumAge) {
            return ResponseEntity.badRequest().build();
        }

        User createdUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") Long userId, @Valid @RequestBody User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User existingUser = optionalUser.get();

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setBirthDate(updatedUser.getBirthDate());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

        userRepository.save(existingUser);

        return ResponseEntity.ok(existingUser);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<User> updateAllUserFields(@PathVariable("userId") Long userId, @Valid @RequestBody User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User existingUser = optionalUser.get();

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setBirthDate(updatedUser.getBirthDate());

        userRepository.save(existingUser);

        return ResponseEntity.ok(existingUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long id) {
        try {
            userRepository.delete(id);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Deleting unexisting by id" + id);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsersByBirthDateRange(@RequestParam("from") Date from, @RequestParam("to") Date to) {
        if (from.after(to)) {
            return ResponseEntity.badRequest().build();
        }

        List<User> users = userRepository.findByBirthDateBetween(from, to);
        return ResponseEntity.ok(users);
    }

    private int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        Period period = Period.between(birthDate, currentDate);
        return period.getYears();
    }

}
