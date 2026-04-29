package com.example.filecloud;

import com.example.filecloud.exception.DeleteFileException;
import com.example.filecloud.model.CloudFile;
import com.example.filecloud.model.CloudFileDescription;
import com.example.filecloud.repository.CloudFileRepository;
import com.example.filecloud.service.CloudFileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CloudFileServiceTests {
    @Test
    void shouldReturnList() {
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
    void shouldDeleteThrow() {
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
    void shouldRenameThrow() {
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
