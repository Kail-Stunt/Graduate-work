package controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import dto.request.NewPasswordRequest;
import dto.request.UpdateUserRequest;
import dto.response.UserResponse;
import service.ImageService;
import service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ImageService imageService;

    public UserController(UserService userService, ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUser() {
        UserResponse response = userService.getUser();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest request) {
        UserResponse response = userService.updateUser(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me/image")
    public ResponseEntity<Void> updateUserImage(@RequestParam("image") MultipartFile image) throws IOException {
        String imagePath = imageService.storeImage(image);
        userService.updateUserImage(imagePath);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@Valid @RequestBody NewPasswordRequest request) {
        userService.setPassword(request);
        return ResponseEntity.ok().build();
    }
}
