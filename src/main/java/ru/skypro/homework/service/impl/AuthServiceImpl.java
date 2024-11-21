package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.exception.UserIsAlreadyExistException;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

/**
 * Реализация сервиса для аутентификации и регистрации пользователей.
 * Предоставляет функциональность для входа в систему и регистрации новых пользователей.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    /**
     * Выполняет аутентификацию пользователя.
     * Проверяет, соответствует ли введенный пароль сохраненному в базе данных.
     *
     * @param userName имя пользователя (email).
     * @param password пароль пользователя.
     * @return true, если пользователь найден и пароль совпадает, иначе false.
     */
    @Override
    public boolean login(String userName, String password) {
        return userRepository.findByEmail(userName)
                .map(user -> encoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    /**
     * Регистрирует нового пользователя.
     * Если пользователь с таким email уже существует, выбрасывается исключение.
     * Пароль пользователя шифруется перед сохранением.
     *
     * @param register объект, содержащий данные для регистрации пользователя.
     * @return true, если регистрация прошла успешно.
     * @throws UserIsAlreadyExistException если пользователь с таким email уже существует.
     */
    @Override
    public boolean register(Register register) {
        if (userRepository.findByEmail(register.getUsername()).isPresent()) {
            throw new UserIsAlreadyExistException("User is already exist");
        } else {
            register.setPassword(encoder.encode(register.getPassword()));
            userRepository.save(userMapper.mapRegisterToUserEntity(register));
            return true;
        }
    }
}
