package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers() {
        logger.debug("Fetching all users");
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<UserResponse> getUserById(Long id) {
        logger.debug("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .map(UserResponse::fromEntity);
    }

    public Optional<UserResponse> getUserByEmail(String email) {
        logger.debug("Fetching user with email: {}", email);
        return userRepository.findByEmail(email)
                .map(UserResponse::fromEntity);
    }

    public List<UserResponse> getUsersByDepartment(String department) {
        logger.debug("Fetching users in department: {}", department);
        return userRepository.findByDepartment(department).stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public UserResponse createUser(UserRequest request) {
        logger.info("Creating new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with email " + request.getEmail() + " already exists");
        }

        User user = new User(
            request.getName(),
            request.getEmail(),
            request.getDepartment()
        );

        User savedUser = userRepository.save(user);
        logger.info("Created user with id: {}", savedUser.getId());

        return UserResponse.fromEntity(savedUser);
    }

    public Optional<UserResponse> updateUser(Long id, UserRequest request) {
        logger.info("Updating user with id: {}", id);

        return userRepository.findById(id)
                .map(existingUser -> {
                    // Check if email is being changed to one that already exists
                    if (!existingUser.getEmail().equals(request.getEmail())
                            && userRepository.existsByEmail(request.getEmail())) {
                        throw new IllegalArgumentException(
                            "User with email " + request.getEmail() + " already exists");
                    }

                    existingUser.setName(request.getName());
                    existingUser.setEmail(request.getEmail());
                    existingUser.setDepartment(request.getDepartment());

                    User updatedUser = userRepository.save(existingUser);
                    logger.info("Updated user with id: {}", updatedUser.getId());

                    return UserResponse.fromEntity(updatedUser);
                });
    }

    public boolean deleteUser(Long id) {
        logger.info("Deleting user with id: {}", id);

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            logger.info("Deleted user with id: {}", id);
            return true;
        }

        logger.warn("User with id {} not found for deletion", id);
        return false;
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
