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

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    @Override
    public boolean login(String userName, String password) {
        return userRepository.findByEmail(userName)
                .map(user -> encoder.matches(password, user.getPassword()))
                .orElse(false);
    }

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
