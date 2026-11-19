
package service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    private static final List<String> ALLOWED_EXTENSIONS = List.of(
            ".jpg", ".jpeg", ".png", ".gif", ".webp"
    );

    private final Path fileStorageLocation;

    public ImageService(@Value("${app.storage.path:./uploads}") String storagePath) {
        this.fileStorageLocation = Paths.get(storagePath).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public String storeImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Invalid file type: " + contentType + ". Allowed: " + ALLOWED_CONTENT_TYPES);
        }

        String originalFilename = file.getOriginalFilename();
        String lowerFilename = originalFilename != null ? originalFilename.toLowerCase() : "";
        boolean hasAllowedExtension = ALLOWED_EXTENSIONS.stream().anyMatch(lowerFilename::endsWith);
        if (!hasAllowedExtension) {
            throw new IllegalArgumentException("Invalid file extension: " + originalFilename);
        }

        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }
        String fileName = UUID.randomUUID() + extension;
        Path targetLocation = fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation);
        return "/images/" + fileName;
    }

    public byte[] getImage(String imagePath) throws IOException {
        String fileName = imagePath.substring("/images/".length());
        Path filePath = fileStorageLocation.resolve(fileName).normalize();
        return Files.readAllBytes(filePath);
    }

    public void deleteImage(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            return;
        }
        try {
            String fileName = imagePath.substring("/images/".length());
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete image: " + imagePath, e);
        }
    }
}
