package com.alpashaev.testAssignment.repository;

import com.alpashaev.testAssignment.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<User, String> {

    Optional<User> findUserById(Long id);

    User save(User user);

    boolean deleteUserById(Long id);

    List<User> findAllByBirthDateAfterAndBirthDateBefore(Date from, Date to);
}