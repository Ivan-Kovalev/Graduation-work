package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.model.UserEntity;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    User mapUserEntityToUser(UserEntity userEntity);

    UserEntity mapUserToUserEntity(User user);

    UpdateUser mapUserEntityToUpdateUser(UserEntity userEntity);

    UserEntity mapUpdateUserToUserEntity(UpdateUser updateUser);

    Register mapUserEntityToRegister(UserEntity userEntity);

    UserEntity mapRegisterToUserEntity(Register register);

    Login mapUserEntityToLogin(UserEntity userEntity);

    UserEntity mapLoginToUserEntity(Login login);

}
