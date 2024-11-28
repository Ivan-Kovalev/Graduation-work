package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.service.AdService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdControllerTest {

    @Mock
    private AdService adService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AdController adController;

    @Test
    void getAllAdv_ShouldReturnAdvertisements() {
        Ads ads = new Ads(3, new Ad[]{
                new Ad(1, "image1.jpg", 1, 100, "Ad1"),
                new Ad(2, "image2.jpg", 2, 200, "Ad2"),
                new Ad(3, "image3.jpg", 3, 300, "Ad3")
        });
        when(adService.getAllAdv()).thenReturn(ads);
        ResponseEntity<Ads> response = adController.getAllAdv();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().getCount());
        assertEquals("Ad1", response.getBody().getResults()[0].getTitle());
    }

    @Test
    void addAdv_ShouldReturnCreatedAd() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[10]);
        CreateOrUpdateAd adRequest = new CreateOrUpdateAd("New Ad", 100, "Description");
        Ad mockAd = new Ad(1, "newImage.jpg", 1, 100, "New Ad");
        when(adService.addAdv(eq(mockFile), eq(adRequest), anyString())).thenReturn(mockAd);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("username");
        ResponseEntity<Ad> response = adController.addAdv(mockFile, adRequest, userDetails);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New Ad", response.getBody().getTitle());
    }

    @Test
    void deleteAdv_ShouldReturnNoContent() {
        doNothing().when(adService).deleteAdv(eq(1), anyString());
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("username");
        ResponseEntity<HttpStatus> response = adController.deleteAdv(1, userDetails);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void patchAdvInfo_ShouldReturnUpdatedAd() {
        CreateOrUpdateAd updateRequest = new CreateOrUpdateAd("Updated Ad", 150, "Updated Description");
        Ad updatedAd = new Ad(1, "updatedImage.jpg", 1, 150, "Updated Ad");
        when(adService.updateAdvInfo(1, updateRequest, "username")).thenReturn(updatedAd);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("username");
        ResponseEntity<Ad> response = adController.patchAdvInfo(1, updateRequest, userDetails);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Updated Ad", response.getBody().getTitle());
    }

    @Test
    void getAdvCurrentUser_ShouldReturnUserAds() {
        Ads userAds = new Ads(2, new Ad[]{
                new Ad(1, "image1.jpg", 1, 100, "User Ad1"),
                new Ad(2, "image2.jpg", 2, 200, "User Ad2")
        });
        when(adService.getAdvCurrentUser("username")).thenReturn(userAds);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("username");
        ResponseEntity<Ads> response = adController.getAdvCurrentUser(userDetails);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getCount());
        assertEquals("User Ad1", response.getBody().getResults()[0].getTitle());
    }

    @Test
    void patchAdvImage_ShouldReturnForbiddenIfFileTooLarge() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[100_000]);
        UserDetails userDetails = mock(UserDetails.class);
        lenient().when(userDetails.getUsername()).thenReturn("username");
        ResponseEntity<byte[]> response = adController.patchAdvImage(1, mockFile, userDetails);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void patchAdvImage_ShouldReturnUnauthorizedIfUserNotLoggedIn() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[10]);
        ResponseEntity<byte[]> response = adController.patchAdvImage(1, mockFile, null);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
