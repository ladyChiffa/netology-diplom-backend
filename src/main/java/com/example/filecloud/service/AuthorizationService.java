package com.example.filecloud.service;

import com.example.filecloud.exception.BadUserException;
import com.example.filecloud.model.Token;
import com.example.filecloud.model.User;
import com.example.filecloud.repository.CloudFileRepository;
import com.example.filecloud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    private final UserRepository userRepository;
    private static final Logger logger = LogManager.getLogger(AuthorizationService.class);


    public Token getAuthorities(String login, String password) {
        Optional<User> u = userRepository.findByNameAndPassphrase(login, password);
        if(u.isEmpty()) {
            throw new BadUserException("Не найдена комбинация пользователь/пароль");
        }
        User user = u.get();

        logger.info("Успешный вход");
        return new Token(user.getAuthToken());
    }

    public int getUserIdByToken(String authToken) {
        Optional<User> user = userRepository.findByAuthToken(authToken);
        if (user.isEmpty()) throw new BadUserException("Пользователь с таким токеном отсутствует");

        User u = user.get();
        return u.getId();
    }
}
