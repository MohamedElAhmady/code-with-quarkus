package org.stibodx.service;

import org.stibodx.entity.User;
import org.stibodx.dto.UserDTO;
import org.stibodx.dto.PagedResult;
import org.stibodx.mapper.UserMapper;
import org.stibodx.exception.UserNotFoundException;
import org.stibodx.exception.InvalidEmailException;
import org.stibodx.exception.UserAlreadyExistsException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {

    @Inject
    UserMapper userMapper;

    @Transactional
    public UserDTO createUser(@Valid UserDTO userDTO) {
        Optional<User> existingUser = findUserByEmail(userDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + userDTO.getEmail() + " already exists");
        }
        
        User user = userMapper.toEntity(userDTO);
        user.persist();
        return userMapper.toDTO(user);
    }

    public UserDTO findById(UUID id) {
        Optional<User> user = Optional.ofNullable(User.findById(id));
        return user.map(userMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public List<UserDTO> findAll() {
        List<User> users = User.listAll();
        return users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PagedResult<UserDTO> findAllPaginated(int page, int size) {
        // Validate pagination parameters
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be positive");
        }
        if (size > 100) {
            throw new IllegalArgumentException("Page size cannot exceed 100");
        }

        // Calculate offset
        int offset = page * size;

        // Get total count
        long totalElements = User.count();

        // Get paginated results
        List<User> users = User.findAll()
                .page(page, size)
                .list();

        List<UserDTO> userDTOs = users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());

        return new PagedResult<>(userDTOs, page, size, totalElements);
    }

    public UserDTO findByEmail(String email) {
        // Validate email format
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidEmailException("Email cannot be null or empty");
        }
        
        if (!isValidEmail(email)) {
            throw new InvalidEmailException("Invalid email format");
        }
        
        Optional<User> user = findUserByEmail(email);
        return user.map(userMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
    
    private boolean isValidEmail(String email) {
        // Basic email validation - contains @ and has characters before and after
        return email.contains("@") && 
               email.indexOf("@") > 0 && 
               email.indexOf("@") < email.length() - 1;
    }

    private Optional<User> findUserByEmail(String email) {
        return User.find("email", email).firstResultOptional();
    }
}