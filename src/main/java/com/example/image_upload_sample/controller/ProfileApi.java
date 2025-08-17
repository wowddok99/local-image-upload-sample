package com.example.image_upload_sample.controller;

import com.example.image_upload_sample.service.LocalFileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileApi {

    private final LocalFileStorageService localFileStorageService;

    /**
     * 사용자 프로필 이미지를 업로드합니다.
     */
    @PostMapping("/images")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        String storedFileName = localFileStorageService.store(file, "profile");
        return localFileStorageService.getFileUrl(storedFileName, "profile");
    }

    /**
     * 사용자 프로필에 사용될 여러 이미지를 한 번에 업로드합니다.
     */
    @PostMapping("/images/multiple")
    @ResponseStatus(HttpStatus.CREATED)
    public List<String> uploadMultipleImages(@RequestParam("files") List<MultipartFile> files) {
        List<String> storedFileNames = localFileStorageService.storeFiles(files, "profile");
        return localFileStorageService.getFileUrls(storedFileNames, "profile");
    }
}