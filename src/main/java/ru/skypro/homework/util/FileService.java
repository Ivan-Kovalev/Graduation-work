package ru.skypro.homework.util;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String saveFile(MultipartFile file);
    String updateFile(String oldFilePath, MultipartFile newFile);

}
