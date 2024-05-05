package com.alpashaev.testAssignment.repository;

import com.alpashaev.testAssignment.entities.User;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private static UserRepo userRepo;

    public Optional<User> findById(Long id) {
        return userRepo.findUserById(id);
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    public void delete(Long id) {
        userRepo.deleteUserById(id);
    }

    public List<User> findByBirthDateBetween(Date from, Date to) {
        return userRepo.findAllByBirthDateAfterAndBirthDateBefore(from, to);
    }
}
