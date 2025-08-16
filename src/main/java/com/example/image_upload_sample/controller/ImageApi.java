package com.example.image_upload_sample.controller;

import com.example.image_upload_sample.service.LocalFileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageApi {

    private final LocalFileStorageService localFileStorageService;

    @PostMapping("/images/upload")
    @ResponseStatus(HttpStatus.OK)
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        String storedFileName = localFileStorageService.store(file);
        return localFileStorageService.getFileUrl(storedFileName);
    }

    @PostMapping("images/upload-multiple")
    public List<String> uploadMultipleImages(@RequestParam("files") List<MultipartFile> files) {
        List<String> storedFileNames = localFileStorageService.storeFiles(files);
        return localFileStorageService.getFileUrls(storedFileNames);
    }
}
