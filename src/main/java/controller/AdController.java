package controller;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import dto.request.CreateOrUpdateAdRequest;
import dto.response.AdResponse;
import dto.response.AdsResponse;
import dto.response.ExtendedAdResponse;
import entity.User;
import service.AdService;
import service.ImageService;
import service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/ads")
public class AdController {

    private final AdService adService;
    private final UserService userService;
    private final ImageService imageService;

    public AdController(AdService adService, UserService userService, ImageService imageService) {
        this.adService = adService;
        this.userService = userService;
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<AdsResponse> getAllAds() {
        AdsResponse response = adService.getAllAds();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<AdsResponse> getAdsMe() {
        User currentUser = userService.getCurrentUser();
        AdsResponse response = adService.getAdsByUser(currentUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdResponse> addAd(
            @RequestPart("properties") @Valid CreateOrUpdateAdRequest request,
            @RequestPart("image") MultipartFile image) throws IOException {
        User currentUser = userService.getCurrentUser();
        String imagePath = imageService.storeImage(image);
        AdResponse response = adService.addAd(request, currentUser, imagePath);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAdResponse> getAd(@PathVariable Integer id) {
        ExtendedAdResponse response = adService.getAd(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdResponse> updateAd(
            @PathVariable Integer id,
            @Valid @RequestBody CreateOrUpdateAdRequest request) {
        User currentUser = userService.getCurrentUser();
        AdResponse response = adService.updateAd(id, request, currentUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable Integer id) {
        User currentUser = userService.getCurrentUser();
        adService.removeAd(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateImage(@PathVariable Integer id, @RequestPart("image") MultipartFile image) throws IOException {
        User currentUser = userService.getCurrentUser();
        String imagePath = imageService.storeImage(image);
        adService.updateAdImage(id, imagePath, currentUser);
        return ResponseEntity.ok().build();
    }
}
