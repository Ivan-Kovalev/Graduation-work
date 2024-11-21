package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.util.FileService;

import javax.transaction.Transactional;

/**
 * Реализация сервиса для работы с пользователями.
 * Предоставляет функциональность для управления пользователями, включая изменение пароля, обновление информации и изображения пользователя.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FileService fileService;
    private final UserMapper mapper;

    /**
     * Устанавливает новый пароль для пользователя.
     * Если пользователь с указанным username не найден, выбрасывается исключение.
     *
     * @param newPassword объект, содержащий новый пароль.
     * @param username    имя пользователя, для которого устанавливается новый пароль.
     * @throws UsernameNotFoundException если пользователь с указанным username не найден.
     */
    @Transactional
    @Override
    public void setPassword(NewPassword newPassword, String username) {
        UserEntity user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Ошибка! Пользователь не найден!"));
        user.setPassword(newPassword.getNewPassword());
        userRepository.save(user);
    }

    /**
     * Получает информацию о текущем пользователе.
     * Если пользователь с указанным username не найден, выбрасывается исключение.
     *
     * @param username имя пользователя, информацию о котором нужно получить.
     * @return объект User, содержащий информацию о пользователе.
     * @throws UsernameNotFoundException если пользователь с указанным username не найден.
     */
    @Override
    public User getCurrentUserInfo(String username) {
        return mapper.mapUserEntityToUser(userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Ошибка! Пользователь не найден!")));
    }

    /**
     * Обновляет информацию о текущем пользователе.
     * Если пользователь с указанным username не найден, выбрасывается исключение.
     *
     * @param updateUser объект, содержащий обновленную информацию о пользователе.
     * @param username   имя пользователя, информацию которого нужно обновить.
     * @return объект UpdateUser, содержащий обновленную информацию о пользователе.
     * @throws UsernameNotFoundException если пользователь с указанным username не найден.
     */
    @Override
    public UpdateUser updateCurrentUserInfo(UpdateUser updateUser, String username) {
        UserEntity user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Ошибка! Пользователь не найден!"));
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPhone(updateUser.getPhone());
        userRepository.save(user);
        return mapper.mapUserEntityToUpdateUser(user);
    }

    /**
     * Обновляет изображение пользователя.
     * Если пользователь с указанным username не найден, выбрасывается исключение.
     *
     * @param file     изображение пользователя, которое нужно обновить.
     * @param username имя пользователя, изображение которого нужно обновить.
     * @throws UsernameNotFoundException если пользователь с указанным username не найден.
     */
    @Override
    public void updateCurrentUserImage(MultipartFile file, String username) {
        UserEntity user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Ошибка! Пользователь не найден!"));
        String filePath = fileService.updateFile(user.getImage(), file);
        user.setImage(filePath);
        userRepository.save(user);
    }
}
