package ru.skypro.homework.util.imlp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.util.FileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    @Value("${image.upload.directory}")
    private String uploadDirectory;

    public String saveFile(MultipartFile file) {
        Path directory = Paths.get(uploadDirectory);

        try {
            Files.createDirectories(directory);
            Path filePath = directory.resolve(System.currentTimeMillis() + "-" + file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath);
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении файла");
        }
    }

    public String updateFile(String oldFilePath, MultipartFile newFile) {
        Path oldFile = Paths.get(oldFilePath);

        if (Files.exists(oldFile)) {
            try {
                Files.delete(oldFile);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при удалении файла");
            }
        }

        return saveFile(newFile);
    }

}
