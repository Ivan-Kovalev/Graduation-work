package ru.skypro.homework.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.model.UserEntity;

import java.util.Collection;
import java.util.Collections;

/**
 * Класс, реализующий интерфейс {@link UserDetails} для предоставления информации о пользователе.
 * Используется для аутентификации и авторизации в системе безопасности Spring Security.
 * Этот класс оборачивает сущность пользователя {@link UserEntity} и предоставляет необходимые данные,
 * такие как имя пользователя, пароль и роли.
 */
public class PersonDetails implements UserDetails {

    private final UserEntity user;

    public PersonDetails(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
