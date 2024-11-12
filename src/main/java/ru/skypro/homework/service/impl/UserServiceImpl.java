package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final FileService fileService;
    private UserMapper mapper;

    @Override
    public void setPassword(NewPassword newPassword, UserDetails userDetails) {
        UserEntity user = repository.findUserEntitiesByEmail(userDetails.getUsername());
        user.setPassword(newPassword.getNewPassword());
        repository.updateUserEntityByEmail(userDetails.getUsername(), user);
    }

    @Override
    public User getCurrentUserInfo(UserDetails userDetails) {
        return mapper.mapUserEntityToUser(repository.findUserEntitiesByEmail(userDetails.getUsername()));
    }

    @Override
    public UpdateUser patchCurrentUserInfo(UpdateUser updateUser, UserDetails userDetails) {
        UserEntity user = repository.findUserEntitiesByEmail(userDetails.getUsername());
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPhone(updateUser.getPhone());
        repository.updateUserEntityByEmail(userDetails.getUsername(), user);
        return mapper.mapUserEntityToUpdateUser(user);
    }

    @Override
    public void patchCurrentUserImage(MultipartFile file, UserDetails userDetails) {
        String filePath = fileService.saveFile(file);
        UserEntity user = repository.findUserEntitiesByEmail(userDetails.getUsername());
        user.setImage(filePath);
        repository.updateUserEntityByEmail(userDetails.getUsername(), user);
    }
}
