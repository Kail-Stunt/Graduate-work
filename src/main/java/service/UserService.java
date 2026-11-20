package service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import dto.request.NewPasswordRequest;
import dto.request.RegisterRequest;
import dto.request.UpdateUserRequest;
import dto.response.UserResponse;
import entity.User;
import entity.UserRole;
import mapper.UserMapper;
import repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        if (request.getRole() != null && !request.getRole().isBlank()) {
            user.setRole(UserRole.valueOf(request.getRole()));
        } else {
            user.setRole(UserRole.USER);
        }

        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    public UserResponse getUser() {
        User currentUser = getCurrentUser();
        return userMapper.toResponse(currentUser);
    }

    public UserResponse updateUser(UpdateUserRequest request) {
        User currentUser = getCurrentUser();

        if (request.getFirstName() != null) {
            currentUser.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            currentUser.setLastName(request.getLastName());
        }
        if (request.getPhone() != null) {
            currentUser.setPhone(request.getPhone());
        }

        User saved = userRepository.save(currentUser);
        return userMapper.toResponse(saved);
    }

    public void updateUserImage(String imagePath) {
        User currentUser = getCurrentUser();
        currentUser.setImage(imagePath);
        userRepository.save(currentUser);
    }

    public void setPassword(NewPasswordRequest request) {
        User currentUser = getCurrentUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserResponse toResponse(User user) {
        return userMapper.toResponse(user);
    }
}
