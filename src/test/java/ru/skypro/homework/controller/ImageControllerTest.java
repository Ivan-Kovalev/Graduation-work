package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.util.FileService;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageControllerTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private ImageController imageController;

    @Mock
    private Resource resource;

    @Test
    public void testGetFileSuccess() throws Exception {
        String fileName = "image.jpg";
        when(fileService.getFile(fileName)).thenReturn(resource);
        ResponseEntity<Resource> response = imageController.getFile(fileName);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetFileNotFound() throws Exception {
        String fileName = "image_not_found.jpg";
        when(fileService.getFile(fileName)).thenThrow(new FileNotFoundException());
        ResponseEntity<Resource> response = imageController.getFile(fileName);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}