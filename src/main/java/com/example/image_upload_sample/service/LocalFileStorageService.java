package com.example.image_upload_sample.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LocalFileStorageService {

    @Value("${storage.local.base-path}")
    private String basePath; // 실제 파일이 저장될 경로

    @Value("${storage.local.base-url}")
    private String baseUrl; // 외부에서 파일에 접근할 URL

    private Path fileStorageLocation;

    /**
     * 서비스 초기화 시 실행
     * - basePath에 해당하는 디렉토리가 없으면 생성
     */
    @PostConstruct
    public void init() {
        this.fileStorageLocation = Paths.get(basePath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("업로드 디렉토리를 생성할 수 없습니다.", ex);
        }
    }

    /**
     * 단일 파일을 저장하는 메소드
     */
    public String store(MultipartFile file) {
        Objects.requireNonNull(file, "파일이 비어있습니다.");

        String originalFileName = file.getOriginalFilename();
        if (originalFileName.isBlank()) {
            throw new RuntimeException("원본 파일 이름이 없습니다.");
        }

        String extension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < originalFileName.length() - 1) {
            extension = originalFileName.substring(dotIndex);
        }

        String storedFileName = UUID.randomUUID() + extension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return storedFileName; // 저장된 파일 이름만 반환
        } catch (IOException ex) {
            throw new RuntimeException("파일을 저장할 수 없습니다. 파일 이름: " + storedFileName, ex);
        }
    }

    /**
     * 여러 파일을 한 번에 저장하는 메소드
     */
    public List<String> storeFiles(List<MultipartFile> files) {
        return files.stream()
                .map(this::store) // 각 파일을 store 메소드를 이용해 저장
                .collect(Collectors.toList());
    }

    /**
     * 저장된 파일 이름으로 접근 가능한 URL을 반환하는 메소드
     */
    public String getFileUrl(String storedFileName) {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .pathSegment(storedFileName)
                .toUriString();
    }

    /**
     * 여러 개의 저장된 파일 이름으로 접근 가능한 URL 리스트를 반환하는 메소드
     */
    public List<String> getFileUrls(List<String> storedFileNames) {
        return storedFileNames.stream()
                .map(this::getFileUrl)
                .collect(Collectors.toList());
    }
}