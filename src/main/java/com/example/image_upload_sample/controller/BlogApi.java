package com.example.image_upload_sample.controller;

import com.example.image_upload_sample.service.LocalFileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
public class BlogApi {

    private final LocalFileStorageService localFileStorageService;

    /**
     * 블로그 게시물에 사용될 단일 이미지를 업로드합니다.
     */
    @PostMapping("/images")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        String storedFileName = localFileStorageService.store(file, "blog");
        return localFileStorageService.getFileUrl(storedFileName, "blog");
    }

    /**
     * 블로그 게시물에 사용될 여러 이미지를 한 번에 업로드합니다.
     */
    @PostMapping("/images/multiple")
    @ResponseStatus(HttpStatus.CREATED)
    public List<String> uploadMultipleImages(@RequestParam("files") List<MultipartFile> files) {
        List<String> storedFileNames = localFileStorageService.storeFiles(files, "blog");
        return localFileStorageService.getFileUrls(storedFileNames, "blog");
    }
}