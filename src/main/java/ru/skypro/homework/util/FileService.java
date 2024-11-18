package ru.skypro.homework.util;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

public interface FileService {

    String saveFile(MultipartFile file);
    String updateFile(String fileName, MultipartFile newFile);
    Resource getFile(String fileName) throws FileNotFoundException;
}
