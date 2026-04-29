package com.example.filecloud.controller;

import com.example.filecloud.model.CloudFile;
import com.example.filecloud.model.CloudFileDescription;
import com.example.filecloud.service.AuthorizationService;
import com.example.filecloud.service.CloudFileService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final CloudFileService fileService;
    private final AuthorizationService authService;
    private static final Logger logger = LogManager.getLogger(FileController.class);

    private int getUserId (Map<String, String> headers) {
        String authToken = headers.get("auth-token").replace("Bearer ", "");
        return authService.getUserIdByToken(authToken);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CloudFile>> getList(@RequestHeader Map<String, String> headers, @RequestParam Integer limit) {
        logger.info("=== IN CONTROLLER === GET /list");
        int userId = getUserId(headers);
        logger.info("=== IN CONTROLLER === limited " + limit + " for userId " + userId);
        List<CloudFile> files = fileService.getList(limit, userId);
        return ResponseEntity.ok(files);
    }

    @PostMapping("/file")
    public ResponseEntity<CloudFile> createFile(@RequestHeader Map<String, String> headers, @RequestParam MultipartFile file) throws IOException {
        logger.info("=== IN CONTROLLER === POST /file");
        int userId = getUserId(headers);
        logger.info("=== IN CONTROLLER === uploading file (size " + file.getSize() + ") " + file.getName() + " for userId " + userId);
        return ResponseEntity.ok(fileService.createFile(file, userId));
    }

    @DeleteMapping("/file")
    public void deleteFile(@RequestHeader Map<String, String> headers, @RequestParam String filename) throws IOException {
        logger.info("=== IN CONTROLLER === DELETE /file");
        int userId = getUserId(headers);
        logger.info("=== IN CONTROLLER === remove " + filename + " for userId " + userId);
        fileService.deleteFile(filename, userId);
    }

    @PutMapping("/file")
    public void updateFileName(@RequestHeader Map<String, String> headers, @RequestParam String filename, @RequestBody CloudFileDescription newRequisites) throws IOException{
        logger.info("=== IN CONTROLLER === PUT /file");
        int userId = getUserId(headers);
        logger.info("=== IN CONTROLLER === update from " + filename + " to " + newRequisites.getFilename() + " for userId " + userId);
        fileService.renameFile(filename, newRequisites, userId);
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestHeader Map<String, String> headers, @RequestParam String filename) throws IOException {
        int userId = getUserId(headers);
        logger.info("=== IN CONTROLLER === download " + filename + " for userId " + userId);

        Resource resource = fileService.downloadFile(filename, userId);

        String encodedFilename = URLEncoder.encode(resource.getFilename(), StandardCharsets.UTF_8.toString())
                .replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                // .contentType(MediaType.parseMediaType(resource.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                .contentLength(resource.contentLength())
                .body(resource);
    }

}


