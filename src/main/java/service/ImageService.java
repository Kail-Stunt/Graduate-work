package service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
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
        return fileName;
    }

    public Resource getResource(String imageName) {
        try {
            Path filePath = fileStorageLocation.resolve(imageName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read image: " + imageName, e);
        }
        return null;
    }

    public void deleteImage(String imageName) {
        if (imageName == null || imageName.isBlank()) {
            return;
        }
        try {
            Path filePath = fileStorageLocation.resolve(imageName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete image: " + imageName, e);
        }
    }
}
