package ru.skypro.homework.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.util.imlp.FileServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "image.upload.directory=/temp/uploads"
})
public class FileServiceImplTest {

    @InjectMocks
    private FileServiceImpl fileService;

    @Mock
    private MultipartFile multipartFile;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(fileService, "uploadDirectory", tempDir.toString());
    }

    @Test
    void saveFile_ShouldSaveFile_WhenValidFileIsProvided() {
        String fileName = "testFile.jpg";
        MockMultipartFile mockFile = new MockMultipartFile("file", fileName, "image/jpeg", new byte[0]);
        String savedFilePath = fileService.saveFile(mockFile);
        Path savedFile = Path.of(tempDir.toString(), savedFilePath.substring(savedFilePath.indexOf("/") + 1));
        assertTrue(Files.exists(savedFile));
    }

    @Test
    void updateFile_ShouldDeleteOldFileAndSaveNewFile_WhenFileExists() throws Exception {
        String oldFileName = tempDir.resolve("oldFile.jpg").toString();
        String newFileName = "newFile.jpg";
        MockMultipartFile newMultipartFile = new MockMultipartFile("file", newFileName, "image/jpeg", new byte[0]);
        Files.createFile(Path.of(oldFileName));
        String updatedFilePath = fileService.updateFile(oldFileName, newMultipartFile);
        assertNotNull(updatedFilePath);
        assertTrue(updatedFilePath.endsWith(newFileName));
        assertFalse(Files.exists(Path.of(oldFileName)));
    }
}
