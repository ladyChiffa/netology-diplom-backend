package com.example.filecloud;

import com.example.filecloud.exception.BadUserException;
import com.example.filecloud.exception.DeleteFileException;
import com.example.filecloud.model.CloudFile;
import com.example.filecloud.model.CloudFileDescription;
import com.example.filecloud.model.Token;
import com.example.filecloud.model.User;
import com.example.filecloud.repository.CloudFileRepository;
import com.example.filecloud.repository.UserRepository;
import com.example.filecloud.service.AuthorizationService;
import com.example.filecloud.service.CloudFileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class FilecloudApplicationTests {

	//----------------------------------------------------------------------------------------------------
	// AuthorizationService
	@Test
	void authorizationService_shouldReturnToken() {
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
	void authorizationService_shouldThrow() {
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

	@Test
	void authorizationService_shouldReturnUserId() {
		// Arrange
		int expected = 1;

		// мокируем вспомогательные классы для User
		UserRepository userRepository = Mockito.mock(UserRepository.class);
		Mockito.when(userRepository.findByAuthToken("1111-1111-1111-1111"))
				.thenReturn(Optional.of(new User(1, "kes", "123456", "1111-1111-1111-1111")));

		// создаем объект теструемого класса
		AuthorizationService authService = new AuthorizationService(userRepository);

		// Act
		int result = authService.getUserIdByToken("1111-1111-1111-1111");

		// Assert
		Assertions.assertEquals(expected, result);
	}

	@Test
	void authorizationService_shouldThrowOnUserId() {
		// Arrange
		// мокируем вспомогательные классы для User
		UserRepository userRepository = Mockito.mock(UserRepository.class);
		Mockito.when(userRepository.findByAuthToken("1111-1111-1111-1111"))
				.thenReturn(Optional.empty());

		// создаем объект теструемого класса
		AuthorizationService authService = new AuthorizationService(userRepository);

		// Act
		BadUserException exception = Assertions.assertThrows(BadUserException.class, () -> {
			int result = authService.getUserIdByToken("1111-1111-1111-1111");
		});

		// Assert
		Assertions.assertEquals("Пользователь с таким токеном отсутствует", exception.getMessage());
	}

	//----------------------------------------------------------------------------------------------------
	// FileCloudService
	@Test
	void fileCloudService_shouldReturnList() {
		// Arrange
		List<CloudFile> expected = List.of();

		// мокируем вспомогательные классы для User
		CloudFileRepository fileRepository = Mockito.mock(CloudFileRepository.class);
		Mockito.when(fileRepository.findByUserId(1, Sort.by("lastChange").descending(), Limit.of(3)))
				.thenReturn(
						List.of()
				);

		// создаем объект теструемого класса
		CloudFileService fileService = new CloudFileService(fileRepository);

		// Act
		List<CloudFile> result = fileService.getList(3, 1);

		// Assert
		Assertions.assertEquals(expected, result);
	}

	@Test
	void fileCloudService_shouldDeleteThrow() {
		// Arrange

		// мокируем вспомогательные классы для User
		CloudFileRepository fileRepository = Mockito.mock(CloudFileRepository.class);
		Mockito.when(fileRepository.findByFilenameAndUserId("1.png", 1))
				.thenReturn(
						Optional.empty()
				);

		// создаем объект теструемого класса
		CloudFileService fileService = new CloudFileService(fileRepository);

		// Act
		DeleteFileException exception = Assertions.assertThrows(DeleteFileException.class, () -> {
			fileService.deleteFile("1.png", 1);
		});

		// Assert
		Assertions.assertEquals("Нет у пользователя такого файла", exception.getMessage());
	}

	@Test
	void fileCloudService_shouldRenameThrow() {
		// Arrange

		// мокируем вспомогательные классы для User
		CloudFileRepository fileRepository = Mockito.mock(CloudFileRepository.class);
		Mockito.when(fileRepository.findByFilenameAndUserId("1.png", 1))
				.thenReturn(
						Optional.empty()
				);

		// создаем объект теструемого класса
		CloudFileService fileService = new CloudFileService(fileRepository);

		// Act
		DeleteFileException exception = Assertions.assertThrows(DeleteFileException.class, () -> {
			fileService.renameFile("1.png", new CloudFileDescription("2.png"), 1);
		});

		// Assert
		Assertions.assertEquals("Нет у пользователя такого файла", exception.getMessage());
	}

}
