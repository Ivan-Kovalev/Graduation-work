package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.exception.BadPasswordException;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.util.FileService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FileService fileService;
    private UserMapper mapper;

    @Override
    public void setPassword(NewPassword newPassword, String username) {
        UserEntity user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Ошибка! Пользователь не найден!"));
        if (user.getPassword().equals(newPassword.getCurrentPassword())) {
            user.setPassword(newPassword.getNewPassword());
            userRepository.save(user);
        }
        throw new BadPasswordException("Ошибка! Пароль не соответствует установленному");
    }

    @Override
    public User getCurrentUserInfo(String username) {
        return mapper.mapUserEntityToUser(userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Ошибка! Пользователь не найден!")));
    }

    @Override
    public UpdateUser patchCurrentUserInfo(UpdateUser updateUser, String username) {
        UserEntity user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Ошибка! Пользователь не найден!"));
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPhone(updateUser.getPhone());
        userRepository.save(user);
        return mapper.mapUserEntityToUpdateUser(user);
    }

    @Override
    public void patchCurrentUserImage(MultipartFile file, String username) {
        String filePath = fileService.saveFile(file);
        UserEntity user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Ошибка! Пользователь не найден!"));
        user.setImage(filePath);
        userRepository.save(user);
    }
}
