package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.ForbiddenActionException;
import ru.skypro.homework.mappers.AdMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.util.FileService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private final FileService fileService;

    @Override
    public Ads getAllAdv() {
        List<Ad> adsList = adRepository.findAll().stream()
                .map(this::mapAdEntityToAd)
                .collect(Collectors.toList());

        return new Ads(adsList.size(), adsList.toArray(new Ad[0]));
    }

    private Ad mapAdEntityToAd(AdEntity adEntity) {
        return adMapper.mapAdEntityToAd(adEntity);
    }

    @Override
    public Ad addAdv(MultipartFile file, CreateOrUpdateAd createOrUpdateAd, String username) throws IOException {
        UserEntity user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Ошибка! Пользователь не найден!"));
        String filePath = fileService.saveFile(file);
        AdEntity adEntity = AdEntity.builder()
                .image(filePath)
                .price(createOrUpdateAd.getPrice())
                .title(createOrUpdateAd.getTitle())
                .description(createOrUpdateAd.getDescription())
                .author(user)
                .build();
        adRepository.save(adEntity);
        return adMapper.mapAdEntityToAd(adRepository.save(adEntity));
    }

    @Override
    public ExtendedAd getAdvInfo(Integer id) {
        AdEntity adEntity = adRepository.findById(id).orElseThrow(() -> new AdNotFoundException("Ошибка! Реклама с данным id не найдена"));
        return adMapper.mapAdEntityToExtendedAd(adEntity);
    }

    @Override
    public void deleteAdv(Integer id, String username) {
        AdEntity adEntity = adRepository.findById(id).orElseThrow(() -> new AdNotFoundException("Ошибка! Реклама с данным id не найдена"));
        boolean isAuthor = username.equals(adEntity.getAuthor().getEmail());
        boolean isAdmin = adEntity.getAuthor().getRole().equals(Role.ADMIN);
        if (!isAuthor && !isAdmin) {
            throw new ForbiddenActionException("Пользователь " + username + " не имеет прав удалять чужие объявления.");
        }
        adRepository.deleteById(id);
    }

    @Override
    public Ad updateAdvInfo(Integer id, CreateOrUpdateAd createOrUpdateAd, String username) {
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException("Реклама с id " + id + " не найдена"));
        boolean isAuthor = username.equals(adEntity.getAuthor().getEmail());
        boolean isAdmin = adEntity.getAuthor().getRole().equals(Role.ADMIN);
        if (!isAuthor && !isAdmin) {
            throw new ForbiddenActionException("Пользователь " + username + " не имеет прав менять чужие объявления.");
        }
        adEntity = adMapper.mapCreateOrUpdateAdToEntity(createOrUpdateAd);
        adEntity.setPk(id);
        return adMapper.mapAdEntityToAd(adRepository.save(adEntity));
    }

    @Override
    public Ads getAdvCurrentUser(String username) {
        UserEntity author = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Ошибка! Пользователь не найден!"));
        List<Ad> adsList = adRepository.findAdEntitiesByAuthorId(author.getId()).stream()
                .map(this::mapAdEntityToAd)
                .collect(Collectors.toList());

        return new Ads(adsList.size(), adsList.toArray(new Ad[0]));
    }

    @Override
    public byte[] updateAdvImage(Integer id, MultipartFile file, String username) throws IOException {
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException("Реклама с id " + id + " не найдена"));
        boolean isAuthor = username.equals(adEntity.getAuthor().getEmail());
        boolean isAdmin = adEntity.getAuthor().getRole().equals(Role.ADMIN);
        if (!isAuthor && !isAdmin) {
            throw new ForbiddenActionException("Пользователь " + username + " не имеет прав менять изображения чужих объявлений.");
        }
        fileService.updateFile(adEntity.getImage(), file);
        return file.getBytes();
    }

}
