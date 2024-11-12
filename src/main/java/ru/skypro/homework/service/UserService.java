package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

public interface UserService {
    void setPassword(NewPassword newPassword, String username);

    User getCurrentUserInfo(String username);

    UpdateUser patchCurrentUserInfo(UpdateUser updateUser, String username);

    void patchCurrentUserImage(MultipartFile file, String username);
}
