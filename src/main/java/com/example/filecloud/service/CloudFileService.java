package com.example.filecloud.service;

import com.example.filecloud.exception.DeleteFileException;
import com.example.filecloud.exception.SaveFileException;
import com.example.filecloud.model.CloudFile;
import com.example.filecloud.repository.CloudFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    public List<CloudFile> getList(Integer limit, String authToken) {
        return fileRepository.findByAuthToken(authToken, Sort.by("lastChange").descending(), Limit.of(limit));
    }

    public CloudFile createFile(MultipartFile file, String authToken) throws IOException {
        String filepath = System.getProperty("user.dir") + path + "/" + authToken + "-" + file.getOriginalFilename();
        File f = new File(filepath);

        try {
            file.transferTo(f);
        } catch (IOException e) {
            throw new SaveFileException("Ошибка сохранения");
        }

        CloudFile dbFile = new CloudFile();
        dbFile.setFilename(file.getOriginalFilename());
        dbFile.setFilepath(filepath);
        dbFile.setAuthToken(authToken);
        dbFile.setLastChange(LocalDateTime.now().toString());
        return fileRepository.saveAndFlush(dbFile);
    }
    public void deleteFile (String filename, String authToken) throws IOException {
        Optional<CloudFile> file = fileRepository.findByFilenameAndAuthToken(filename, authToken);
        if (file.isEmpty()) throw new DeleteFileException("Нет у пользователя такого файла");

        CloudFile cloudFile = file.get();
        File f = new File(cloudFile.getFilepath());
        f.delete();

        fileRepository.delete(cloudFile);
    }
}
