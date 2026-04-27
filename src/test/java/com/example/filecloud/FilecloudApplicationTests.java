package com.example.filecloud;

import com.example.filecloud.exception.BadUserException;
import com.example.filecloud.model.Token;
import com.example.filecloud.model.User;
import com.example.filecloud.repository.UserRepository;
import com.example.filecloud.service.AuthorizationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class FilecloudApplicationTests {

	@Test
	void shouldReturnToken() {
		// Arrange
		Token expected = new Token("1111-1111-1111-1111");

		// мокируем вспомогательные классы для User
		UserRepository userRepository = Mockito.mock(UserRepository.class);
		Mockito.when(userRepository.findByNameAndPassphrase("kes", "123456"))
				.thenReturn(Optional.of(new User(1, "kes", "123456", "1111-1111-1111-1111")));

		// создаем объект теструемого класса
		AuthorizationService authService = new AuthorizationService(userRepository);

		// Act
		Token result = authService.getAuthorities("kes", "123456");

		// Assert
		Assertions.assertEquals(expected, result);
	}

	@Test
	void shouldThrow() {
		// Arrange
		// мокируем вспомогательные классы для User
		UserRepository userRepository = Mockito.mock(UserRepository.class);
		Mockito.when(userRepository.findByNameAndPassphrase("max", "123456"))
				.thenReturn(Optional.empty());

		// создаем объект теструемого класса
		AuthorizationService authService = new AuthorizationService(userRepository);

		// Act
		BadUserException exception = Assertions.assertThrows(BadUserException.class, () -> {
			Token result = authService.getAuthorities("max", "123456");
		});

		// Assert
		Assertions.assertEquals("Не найдена комбинация пользователь/пароль", exception.getMessage());

	}

}
