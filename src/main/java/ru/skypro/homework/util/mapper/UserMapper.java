package ru.skypro.homework.util.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.UserEntity;

@Component
public class UserMapper {

    public User mapUserEntityToUser(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getPhone(),
                userEntity.getRole().name(),
                userEntity.getImage()
        );
    }

    public UserEntity mapUserToUserEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                Role.valueOf(user.getRole()),
                user.getImage()
        );
    }

    public UpdateUser mapUserEntityToUpdateUser(UserEntity userEntity) {
        return new UpdateUser(
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getPhone()
        );
    }

    public UserEntity mapUpdateUserToUserEntity(UpdateUser updateUser) {
        return new UserEntity(
                updateUser.getFirstName(),
                updateUser.getFirstName(),
                updateUser.getPhone()
        );
    }

    public Register mapUserEntityToRegister(UserEntity userEntity) {
        return new Register(
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getPhone(),
                userEntity.getRole()
        );
    }

    public UserEntity mapRegisterToUserEntity(Register register) {
        return new UserEntity(
          register.getUsername(),
          register.getPassword(),
          register.getFirstName(),
          register.getLastName(),
          register.getPhone(),
          register.getRole()
        );
    }

    public Login mapUserEntityToLogin(UserEntity userEntity) {
        return new Login(userEntity.getEmail(), userEntity.getPassword());
    }

    public UserEntity mapLoginToUserEntity(Login login) {
        return new UserEntity(
                login.getUsername(),
                login.getPassword()
        );
    }

}
