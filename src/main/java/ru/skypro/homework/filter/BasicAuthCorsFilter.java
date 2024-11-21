package ru.skypro.homework.filter;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр для настройки CORS (Cross-Origin Resource Sharing) с поддержкой авторизации.
 * Добавляет заголовок {@code Access-Control-Allow-Credentials} в ответ для разрешения отправки cookies.
 * Этот фильтр используется для обеспечения работы с CORS в приложениях, которые требуют авторизации.
 */
@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    /**
     * Обрабатывает запрос, добавляя заголовок {@code Access-Control-Allow-Credentials} в ответ.
     * Этот заголовок разрешает браузеру отправлять cookies на сервер при межсайтовых запросах.
     *
     * @param httpServletRequest  объект запроса.
     * @param httpServletResponse объект ответа.
     * @param filterChain         цепочка фильтров для дальнейшей обработки запроса.
     * @throws ServletException если возникает ошибка во время обработки запроса.
     * @throws IOException      если возникает ошибка ввода-вывода.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
