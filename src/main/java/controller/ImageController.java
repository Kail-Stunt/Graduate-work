package controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.ImageService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String imageName = imageService.storeImage(file);
        return ResponseEntity.ok(imageName);
    }

    @GetMapping("/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) throws IOException {
        Resource resource = imageService.getResource(imageName);
        if (resource == null || !resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = getContentType(imageName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{imageName}")
    public ResponseEntity<Void> deleteImage(@PathVariable String imageName) {
        imageService.deleteImage(imageName);
        return ResponseEntity.noContent().build();
    }

    private String getContentType(String imageName) {
        if (imageName.toLowerCase().endsWith(".png")) {
            return MediaType.IMAGE_PNG_VALUE;
        } else if (imageName.toLowerCase().endsWith(".gif")) {
            return MediaType.IMAGE_GIF_VALUE;
        } else if (imageName.toLowerCase().endsWith(".webp")) {
            return "image/webp";
        }
        return MediaType.IMAGE_JPEG_VALUE;
    }
}
