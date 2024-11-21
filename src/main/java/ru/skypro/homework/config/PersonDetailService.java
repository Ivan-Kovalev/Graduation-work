package ru.skypro.homework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.repository.UserRepository;

/**
 * Сервис, реализующий интерфейс {@link UserDetailsService} для загрузки данных пользователя
 * на основе его имени пользователя (email). Этот сервис используется для аутентификации
 * пользователей в Spring Security.
 * <p>
 * Метод {@link #loadUserByUsername(String)} находит пользователя по его email и возвращает
 * объект {@link UserDetails}, который содержит информацию для аутентификации и авторизации.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class PersonDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Загружает пользователя по его имени пользователя (email).
     *
     * @param username имя пользователя (email), по которому выполняется поиск.
     * @return объект {@link UserDetails}, содержащий информацию о пользователе.
     * @throws UsernameNotFoundException если пользователь с указанным именем не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new PersonDetails(userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Bad credentional")));
    }
}
