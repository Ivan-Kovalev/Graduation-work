package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

/**
 * Реализация сервиса для работы с объявлениями.
 * Предоставляет функциональность для создания, чтения, обновления и удаления объявлений, а также для работы с изображениями объявлений.
 */
@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private final FileService fileService;

    /**
     * Получить все объявления.
     *
     * @return объект Ads, содержащий список всех объявлений.
     */
    @Override
    public Ads getAllAdv() {
        List<Ad> adsList = adRepository.findAll().stream()
                .map(this::mapAdEntityToAd)
                .collect(Collectors.toList());

        return new Ads(adsList.size(), adsList.toArray(new Ad[0]));
    }

    /**
     * Преобразует сущность объявления в объект Ad.
     *
     * @param adEntity сущность объявления.
     * @return объект Ad, полученный из сущности.
     */
    private Ad mapAdEntityToAd(AdEntity adEntity) {
        return adMapper.mapAdEntityToAd(adEntity);
    }

    /**
     * Добавляет новое объявление.
     * Сохраняет файл изображения и создает новое объявление с предоставленными данными.
     *
     * @param file             файл изображения объявления.
     * @param createOrUpdateAd объект, содержащий данные для создания нового объявления.
     * @param username         имя пользователя, добавляющего объявление.
     * @return объект Ad, представляющий только что созданное объявление.
     * @throws UsernameNotFoundException если пользователь не найден.
     */
    @Override
    public Ad addAdv(MultipartFile file, CreateOrUpdateAd createOrUpdateAd, String username) {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Ошибка! Пользователь не найден!"));
        String filePath = fileService.saveFile(file);
        AdEntity adEntity = AdEntity.builder()
                .image(filePath)
                .price(createOrUpdateAd.getPrice())
                .title(createOrUpdateAd.getTitle())
                .description(createOrUpdateAd.getDescription())
                .author(user)
                .build();
        AdEntity savedEntity = adRepository.save(adEntity);
        return adMapper.mapAdEntityToAd(savedEntity);
    }

    /**
     * Получить полную информацию о конкретном объявлении по его id.
     *
     * @param id уникальный идентификатор объявления.
     * @return объект ExtendedAd, содержащий подробную информацию о найденном объявлении.
     * @throws AdNotFoundException если объявление с таким id не найдено.
     */
    @Override
    public ExtendedAd getAdvInfo(Integer id) {
        AdEntity adEntity = adRepository.findById(id).orElseThrow(() -> new AdNotFoundException("Ошибка! Реклама с данным id не найдена"));
        return adMapper.mapAdEntityToExtendedAd(adEntity);
    }

    /**
     * Удалить объявление по его id, если пользователь является автором или администратором.
     *
     * @param id       уникальный идентификатор объявления.
     * @param username имя пользователя, пытающегося удалить объявление.
     * @throws AdNotFoundException      если объявление с таким id не найдено.
     * @throws ForbiddenActionException если пользователь не имеет прав на удаление объявления.
     */
    @Override
    public void deleteAdv(Integer id, String username) {
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException("Ошибка! Реклама с данным id не найдена"));
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Ошибка! Пользователь не найден"));

        boolean isAuthor = username.equals(adEntity.getAuthor().getEmail());
        boolean isAdmin = user.getRole().equals(Role.ADMIN);

        if (!isAuthor && !isAdmin) {
            throw new ForbiddenActionException("Пользователь " + username + " не имеет прав удалять чужие объявления.");
        }

        adRepository.deleteById(id);
    }

    /**
     * Обновить информацию о существующем объявлении.
     *
     * @param id               уникальный идентификатор объявления.
     * @param createOrUpdateAd объект с новыми данными для обновления объявления.
     * @param username         имя пользователя, который обновляет объявление.
     * @return обновленный объект Ad.
     * @throws AdNotFoundException      если объявление с таким id не найдено.
     * @throws ForbiddenActionException если пользователь не имеет прав на обновление объявления.
     */
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

    /**
     * Получить объявления текущего пользователя.
     *
     * @param username имя пользователя.
     * @return объект Ads, содержащий список объявлений этого пользователя.
     * @throws UsernameNotFoundException если пользователь не найден.
     */
    @Override
    public Ads getAdvCurrentUser(String username) {
        UserEntity author = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Ошибка! Пользователь не найден!"));
        List<Ad> adsList = adRepository.findAdEntitiesByAuthorId(author.getId()).stream()
                .map(this::mapAdEntityToAd)
                .collect(Collectors.toList());

        return new Ads(adsList.size(), adsList.toArray(new Ad[0]));
    }

    /**
     * Обновить изображение объявления.
     *
     * @param id       уникальный идентификатор объявления.
     * @param file     новый файл изображения.
     * @param username имя пользователя, который обновляет изображение.
     * @return байтовый массив с данными нового изображения.
     * @throws AdNotFoundException      если объявление с таким id не найдено.
     * @throws ForbiddenActionException если пользователь не имеет прав на изменение изображения объявления.
     * @throws IOException              если произошла ошибка при обработке файла.
     */
    @Override
    public byte[] updateAdvImage(Integer id, MultipartFile file, String username) throws IOException, ForbiddenActionException {
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
