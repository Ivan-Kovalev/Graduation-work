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

/**
 * Реализация сервиса для работы с файлами.
 * Предоставляет функциональность для сохранения, обновления и получения файлов.
 */
@Component
public class FileServiceImpl implements FileService {

    @Value("${image.upload.directory}")
    private String uploadDirectory;
    @Value("${resourceses.root}")
    protected String resources;

    /**
     * Сохраняет файл в указанной директории.
     *
     * @param file файл для сохранения
     * @return путь к сохраненному файлу относительно директории загрузки
     * @throws FileSavingException если произошла ошибка при сохранении файла
     */
    public String saveFile(MultipartFile file) {
        Path directory = Paths.get(resources + uploadDirectory);

        try {
            Files.createDirectories(directory);
            Path filePath = directory.resolve(UUID.randomUUID() + "-" + file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath);
            return uploadDirectory + "/" + filePath.getFileName();
        } catch (IOException e) {
            throw new FileSavingException("Ошибка при сохранении файла");
        }
    }

    /**
     * Обновляет файл, удаляя старый и сохраняя новый.
     *
     * @param fileName имя старого файла, который будет удален
     * @param newFile  новый файл для сохранения
     * @return путь к новому файлу относительно директории загрузки
     * @throws FileUpdatingException если произошла ошибка при удалении старого файла или сохранении нового
     */
    public String updateFile(String fileName, MultipartFile newFile) {
        if (fileName != null) {
            Path oldFile = Paths.get(fileName);
            if (Files.exists(oldFile)) {
                try {
                    Files.delete(oldFile);
                } catch (IOException e) {
                    throw new FileUpdatingException("Ошибка при удалении файла");
                }
            }
        }
        return saveFile(newFile);
    }

    public Resource getFile(String fileName) throws FileNotFoundException {
        Path filePath = Paths.get(resources + uploadDirectory, fileName);

        Resource resource = new FileSystemResource(filePath);
        if (!resource.exists()) {
            throw new FileNotFoundException("Файл не найден");
        }
        return resource;
    }

}
