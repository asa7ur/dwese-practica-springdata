package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
    private final Path rootLocation;

    public FileStorageService(@Value("${UPLOAD_PATH}") String uploadPath) {
        this.rootLocation = Paths.get(uploadPath);
    }

    public String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            logger.warn("Intento de guardar un archivo vacío.");
            return null;
        }

        try {
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
            Path destinationFile = this.rootLocation.resolve(uniqueFileName).normalize().toAbsolutePath();

            if (!Files.exists(this.rootLocation)) {
                Files.createDirectories(this.rootLocation);
                logger.info("Directorio creado en: {}", this.rootLocation.toAbsolutePath());
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            logger.info("Archivo guardado: {}", uniqueFileName);
            return uniqueFileName;

        } catch (IOException e) {
            logger.error("Fallo al almacenar el archivo: {}", e.getMessage());
            return null;
        }
    }

    public void deleteFile(String fileName) {
        try {
            Path filePath = this.rootLocation.resolve(fileName).normalize();
            boolean deleted = Files.deleteIfExists(filePath);

            if (deleted) {
                logger.info("Archivo eliminado: {}", fileName);
            } else {
                logger.warn("No se pudo eliminar el archivo (tal vez no existía): {}", fileName);
            }
        } catch (IOException e) {
            logger.error("Error al eliminar el archivo {}: {}", fileName, e.getMessage());
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "";
    }
}