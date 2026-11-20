
package controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import dto.request.LoginRequest;
import dto.response.UserResponse;
import service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public LoginController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserResponse userResponse = userService.findByUsername(request.getUsername())
                .map(userService::toResponse)
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));
        return ResponseEntity.ok(userResponse);
    }
}
