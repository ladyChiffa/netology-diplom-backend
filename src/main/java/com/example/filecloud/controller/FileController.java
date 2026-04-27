package com.example.filecloud.controller;

import com.example.filecloud.model.CloudFile;
import com.example.filecloud.service.CloudFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final CloudFileService fileService;

    @GetMapping("/list")
    public ResponseEntity<List<CloudFile>> getList(@RequestHeader Map<String, String> headers, @RequestParam Integer limit) {
        String authToken = headers.get("auth-token").replace("Bearer ", "");
        List<CloudFile> files = fileService.getList(limit, authToken);
        return ResponseEntity.ok(files);
    }

    @PostMapping("/file")
    public ResponseEntity<CloudFile> createFile(@RequestHeader Map<String, String> headers, @RequestParam MultipartFile file) throws IOException {
        String authToken = headers.get("auth-token").replace("Bearer ", "");
        return ResponseEntity.ok(fileService.createFile(file, authToken));
    }

    @DeleteMapping("/file")
    public void deleteFile(@RequestHeader Map<String, String> headers, @RequestParam String filename) throws IOException {
        String authToken = headers.get("auth-token").replace("Bearer ", "");
        fileService.deleteFile(filename, authToken);
    }
}

