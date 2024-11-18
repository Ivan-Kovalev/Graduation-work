package ru.skypro.homework.util.imlp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.file.FileSavingException;
import ru.skypro.homework.exception.file.FileUpdatingException;
import ru.skypro.homework.util.FileService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileServiceImpl implements FileService {

    @Value("${image.upload.directory}")
    private String uploadDirectory;

    public String saveFile(MultipartFile file) {
        Path directory = Paths.get(uploadDirectory);

        try {
            Files.createDirectories(directory);
            Path filePath = directory.resolve(UUID.randomUUID() + "-" + file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath);
            return uploadDirectory.substring(18) + "/" + filePath.getFileName();
        } catch (IOException e) {
            throw new FileSavingException("Ошибка при сохранении файла");
        }
    }

    public String updateFile(String fileName, MultipartFile newFile) {
        Path oldFile = Paths.get(fileName);

        if (Files.exists(oldFile)) {
            try {
                Files.delete(oldFile);
            } catch (IOException e) {
                throw new FileUpdatingException("Ошибка при удалении файла");
            }
        }

        return saveFile(newFile);
    }

    public Resource getFile(String fileName) throws FileNotFoundException {
        Path filePath = Paths.get(uploadDirectory, fileName);

        Resource resource = new FileSystemResource(filePath);
        if (!resource.exists()) {
            throw new FileNotFoundException("Файл не найден");
        }
        return resource;
    }

}
