package com.example.filecloud.service;

import com.example.filecloud.exception.DeleteFileException;
import com.example.filecloud.exception.SaveFileException;
import com.example.filecloud.model.CloudFile;
import com.example.filecloud.model.CloudFileDescription;
import com.example.filecloud.repository.CloudFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CloudFileService {
    private final CloudFileRepository fileRepository;

    @Value("${filecloud.filedatapath}")
    private String path;

    public List<CloudFile> getList(Integer limit, int userId) {
        return fileRepository.findByUserId(userId, Sort.by("lastChange").descending(), Limit.of(limit));
    }

    public CloudFile createFile(MultipartFile file, int userId) throws IOException {
        String filepath = System.getProperty("user.dir") + path + "/" + userId + "-" + file.getOriginalFilename();
        File f = new File(filepath);

        try {
            file.transferTo(f);
        } catch (IOException e) {
            throw new SaveFileException("Ошибка сохранения");
        }

        CloudFile dbFile = new CloudFile();
        dbFile.setFilename(file.getOriginalFilename());
        dbFile.setFilepath(filepath);
        dbFile.setUserId(userId);
        dbFile.setLastChange(LocalDateTime.now().toString());
        return fileRepository.saveAndFlush(dbFile);
    }
    public void deleteFile (String filename, int userId) throws IOException {
        Optional<CloudFile> file = fileRepository.findByFilenameAndUserId(filename, userId);
        if (file.isEmpty()) throw new DeleteFileException("Нет у пользователя такого файла");

        CloudFile cloudFile = file.get();
        File f = new File(cloudFile.getFilepath());
        f.delete();

        fileRepository.delete(cloudFile);
    }
    public void renameFile (String filename, CloudFileDescription newRequisites, int userId) throws IOException {
        Optional<CloudFile> file = fileRepository.findByFilenameAndUserId(filename, userId);
        if (file.isEmpty()) throw new DeleteFileException("Нет у пользователя такого файла");

        CloudFile cloudFile = file.get();
        cloudFile.setFilename(newRequisites.getFilename());
        fileRepository.saveAndFlush(cloudFile);
    }

    public Resource downloadFile (String filename, int userId) throws IOException {
        Optional<CloudFile> file = fileRepository.findByFilenameAndUserId(filename, userId);
        if (file.isEmpty()) throw new DeleteFileException("Нет у пользователя такого файла");

        CloudFile cloudFile = file.get();
        Resource resource = new FileSystemResource(cloudFile.getFilepath());
        return resource;
    }

}
