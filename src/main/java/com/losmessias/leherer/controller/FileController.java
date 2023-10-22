package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.File;
import com.losmessias.leherer.domain.LoadedData;
import com.losmessias.leherer.dto.UploadFileResponseDto;
import com.losmessias.leherer.dto.UploadInformationDto;
import com.losmessias.leherer.service.FileService;

import com.losmessias.leherer.service.LoadedDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;


@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
@CrossOrigin
public class FileController {

    private final FileService fileService;
    private final LoadedDataService loadedDataService;

    @PostMapping("/uploadFile")
    public UploadFileResponseDto uploadFile(@RequestParam MultipartFile file) {
        File fileReturned = fileService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileReturned.getFileName())
                .toUriString();

        return new UploadFileResponseDto(fileReturned.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }
    @PostMapping("/setUploadInformation")
    public void setUploadInformation(@RequestBody UploadInformationDto info) {
        fileService.setUploadInformation(info);
    }

//    @PostMapping("/uploadMultipleFiles")
//    public List< UploadFileResponseDto > uploadMultipleFiles(@RequestBody() MultipartFile[] files) {
//
//        return Arrays.stream(files)
//                .map(this::uploadFile)
//                .collect(Collectors.toList());
//    }

    @GetMapping("/get-uploaded-data")
    public ResponseEntity<String> getUploadedData(@RequestParam("id") Long Id) throws JsonProcessingException {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(loadedDataService.getUploadedData(Id)));
    }


    @GetMapping("/downloadFile")
    public ResponseEntity<Resource> downloadFile(@RequestParam("id") Long Id) {
        // Load file as Resource
        File file = fileService.getFile(Id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getData()));
    }
}
