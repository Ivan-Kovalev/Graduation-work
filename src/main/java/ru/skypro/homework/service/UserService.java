package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

public interface UserService {
    void setPassword(NewPassword newPassword, UserDetails userDetails);

    User getCurrentUserInfo(UserDetails userDetails);

    UpdateUser patchCurrentUserInfo(UpdateUser updateUser, UserDetails userDetails);

    void patchCurrentUserImage(MultipartFile file, UserDetails userDetails);
}
