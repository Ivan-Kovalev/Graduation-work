package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

import java.io.IOException;

public interface AdService {

    Ads getAllAdv();

    Ad addAdv(MultipartFile file, CreateOrUpdateAd createOrUpdateAd, String username) throws IOException;

    ExtendedAd getAdvInfo(Integer id);

    void deleteAdv(Integer id, String username);

    Ad updateAdvInfo(Integer id, CreateOrUpdateAd createOrUpdateAd, String username);

    Ads getAdvCurrentUser(String username);

    byte[] updateAdvImage(Integer id, MultipartFile file, String username) throws IOException;
}
