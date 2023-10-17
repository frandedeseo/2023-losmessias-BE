package com.losmessias.leherer.controller;

import com.losmessias.leherer.domain.DatabaseFile;
import com.losmessias.leherer.dto.UploadFileResponseDto;
import com.losmessias.leherer.service.DatabaseFileService;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class DatabaseFileController {

    private final DatabaseFileService databaseFileService;
    @PostMapping("/uploadFile")
    public UploadFileResponseDto uploadFile(@RequestParam("file") MultipartFile file) {

        DatabaseFile fileReturned = databaseFileService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileReturned.getFileName())
                .toUriString();

        return new UploadFileResponseDto(fileReturned.getFileName(), fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List< UploadFileResponseDto > uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {

        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@RequestParam("id") String Id) {
        // Load file as Resource
        DatabaseFile databaseFile = databaseFileService.getFile(Id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(databaseFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + databaseFile.getFileName() + "\"")
                .body(new ByteArrayResource(databaseFile.getData()));
    }
}
