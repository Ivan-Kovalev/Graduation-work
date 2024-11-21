package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.ForbiddenActionException;
import ru.skypro.homework.mappers.AdMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AdServiceImpl;
import ru.skypro.homework.util.FileService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdServiceImplTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdMapper adMapper;

    @Mock
    private FileService fileService;

    @InjectMocks
    private AdServiceImpl adService;

    @Test
    void getAllAdv_ShouldReturnAllAds() {
        List<AdEntity> adEntities = List.of(
                new AdEntity(1, "image1.png", 100, "Title1", "Description1", null, null),
                new AdEntity(2, "image2.png", 200, "Title2", "Description2", null, null)
        );
        List<Ad> ads = List.of(
                new Ad(1, "image1.png", 1, 100, "Title1"),
                new Ad(2, "image2.png", 2, 200, "Title2")
        );
        when(adRepository.findAll()).thenReturn(adEntities);
        when(adMapper.mapAdEntityToAd(any(AdEntity.class)))
                .thenAnswer(invocation -> {
                    AdEntity entity = invocation.getArgument(0);
                    return new Ad(entity.getPk(), entity.getImage(), entity.getPk(), entity.getPrice(), entity.getTitle());
                });
        Ads result = adService.getAllAdv();
        assertEquals(2, result.getCount());
        assertEquals("Title1", result.getResults()[0].getTitle());
        assertEquals("Title2", result.getResults()[1].getTitle());
        verify(adRepository, times(1)).findAll();
    }

    @Test
    void addAdv_ShouldAddAdSuccessfully() {
        String username = "test@example.com";
        UserEntity userEntity = new UserEntity(1, username, "password", "John", "Doe", "1234567890", Role.USER, null, null, null);
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd("Title", 150, "Description");
        MultipartFile file = mock(MultipartFile.class);
        String filePath = "path/to/image.png";
        AdEntity savedEntity = new AdEntity(1, filePath, 150, "Title", "Description", null, userEntity);
        Ad mappedAd = new Ad(1, filePath, 1, 150, "Title");
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(userEntity));
        when(fileService.saveFile(file)).thenReturn(filePath);
        when(adRepository.save(any(AdEntity.class))).thenReturn(savedEntity);
        when(adMapper.mapAdEntityToAd(savedEntity)).thenReturn(mappedAd);
        ArgumentCaptor<AdEntity> adEntityCaptor = ArgumentCaptor.forClass(AdEntity.class);
        Ad result = adService.addAdv(file, createOrUpdateAd, username);
        assertEquals(mappedAd, result);
        verify(userRepository, times(1)).findByEmail(username);
        verify(fileService, times(1)).saveFile(file);
        verify(adRepository, times(1)).save(adEntityCaptor.capture()); // Проверим, какой объект передаётся
        verify(adMapper, times(1)).mapAdEntityToAd(savedEntity);
    }


    @Test
    void addAdv_ShouldThrowException_WhenUserNotFound() {
        String username = "test@example.com";
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd("Title", 150, "Description");
        MultipartFile file = mock(MultipartFile.class);
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> adService.addAdv(file, createOrUpdateAd, username)
        );
        assertEquals("Ошибка! Пользователь не найден!", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(username);
        verifyNoInteractions(fileService);
        verifyNoInteractions(adMapper);
        verifyNoInteractions(adRepository);
    }

    @Test
    void getAdvInfo_ShouldReturnAdInfoSuccessfully() {
        int id = 1;
        AdEntity adEntity = new AdEntity(id, "image.png", 150, "Title", "Description", null, null);
        ExtendedAd extendedAd = new ExtendedAd(id, "John", "Doe", "Description", "test@example.com", "image.png", "1234567890", 150, "Title");
        when(adRepository.findById(id)).thenReturn(Optional.of(adEntity));
        when(adMapper.mapAdEntityToExtendedAd(adEntity)).thenReturn(extendedAd);
        ExtendedAd result = adService.getAdvInfo(id);
        assertEquals(extendedAd, result);
        verify(adRepository, times(1)).findById(id);
        verify(adMapper, times(1)).mapAdEntityToExtendedAd(adEntity);
    }

    @Test
    void getAdvInfo_ShouldThrowException_WhenAdNotFound() {
        int id = 1;
        when(adRepository.findById(id)).thenReturn(Optional.empty());
        AdNotFoundException exception = assertThrows(
                AdNotFoundException.class,
                () -> adService.getAdvInfo(id)
        );
        assertEquals("Ошибка! Реклама с данным id не найдена", exception.getMessage());
        verify(adRepository, times(1)).findById(id);
        verifyNoInteractions(adMapper);
    }

    @Test
    void deleteAdv_ShouldDeleteAd_WhenUserIsAuthor() {
        int id = 1;
        String username = "author@example.com";
        UserEntity author = new UserEntity(1, username, "password", "John", "Doe", "1234567890", Role.USER, null, null, null);
        AdEntity adEntity = new AdEntity(id, "image.png", 150, "Title", "Description", null, author);
        when(adRepository.findById(id)).thenReturn(Optional.of(adEntity));
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(author));
        adService.deleteAdv(id, username);
        verify(adRepository, times(1)).findById(id);
        verify(adRepository, times(1)).deleteById(id);
    }


    @Test
    void deleteAdv_ShouldDeleteAd_WhenUserIsAdmin() {
        int id = 1;
        String username = "admin@example.com";

        UserEntity admin = new UserEntity(2, username, "password", "Admin", "User", "1234567890", Role.ADMIN, null, null, null);
        UserEntity author = new UserEntity(1, "author@example.com", "password", "John", "Doe", "1234567890", Role.USER, null, null, null);
        AdEntity adEntity = new AdEntity(id, "image.png", 150, "Title", "Description", null, author);
        when(adRepository.findById(id)).thenReturn(Optional.of(adEntity));
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(admin));
        adService.deleteAdv(id, username);
        verify(adRepository, times(1)).findById(id);
        verify(adRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteAdv_ShouldThrowException_WhenUserIsNotAuthorOrAdmin() {
        // Arrange
        int id = 1;
        String username = "user@example.com";
        UserEntity user = new UserEntity(2, "user@example.com", "password", "John", "Doe", "1234567890", Role.USER, null, null, null);
        UserEntity author = new UserEntity(1, "author@example.com", "password", "John", "Doe", "1234567890", Role.USER, null, null, null);
        AdEntity adEntity = new AdEntity(id, "image.png", 150, "Title", "Description", null, author);
        when(adRepository.findById(id)).thenReturn(Optional.of(adEntity));
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));
        ForbiddenActionException exception = assertThrows(
                ForbiddenActionException.class,
                () -> adService.deleteAdv(id, username)
        );
        assertEquals("Пользователь user@example.com не имеет прав удалять чужие объявления.", exception.getMessage());
        verify(adRepository, times(1)).findById(id);
        verify(adRepository, times(0)).deleteById(anyInt());
        verify(userRepository, times(1)).findByEmail(username);
    }


    @Test
    void deleteAdv_ShouldThrowException_WhenAdNotFound() {
        int id = 1;
        String username = "author@example.com";
        when(adRepository.findById(id)).thenReturn(Optional.empty());
        AdNotFoundException exception = assertThrows(
                AdNotFoundException.class,
                () -> adService.deleteAdv(id, username)
        );
        assertEquals("Ошибка! Реклама с данным id не найдена", exception.getMessage());

        verify(adRepository, times(1)).findById(id);
        verify(adRepository, times(0)).deleteById(anyInt());
    }

}
