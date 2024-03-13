package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.File;
import com.losmessias.leherer.domain.LoadedData;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
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

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
@CrossOrigin
public class FileController {

    private final FileService fileService;
    private final LoadedDataService loadedDataService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam MultipartFile file, @RequestParam Long classReservation, @RequestParam Long associatedId, @RequestParam String role, @RequestParam Long homeworkId) throws JsonProcessingException {
        File fileReturned = fileService.storeFile(file);

        UploadInformationDto info = new UploadInformationDto();
        info.setIdFile(fileReturned.getId());
        info.setClassReservation(classReservation);
        info.setAssociatedId(associatedId);
        info.setRole(AppUserRole.valueOf(role.toUpperCase()));
        info.setHomeworkId(homeworkId > 0 ? homeworkId : null);
        info.setUploadedDateTime(java.time.LocalDateTime.now());

        LoadedData loadedData = fileService.setUploadInformation(info);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(loadedData));
    }


    @GetMapping("/get-uploaded-data")
    public ResponseEntity<String> getUploadedData(@RequestParam("id") Long Id) throws JsonProcessingException {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        System.out.println(loadedDataService.getUploadedData(Id));
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

//    public List<LoadedData> convertUploadedDataToDto(List<LoadedData> loadedDataList) {
//        List<UploadFileResponseDto> loadedDataDtoList = new ArrayList<>();
//        for (LoadedData loadedData : loadedDataList) {
//            loadedDataDtoList.add(new UploadFileResponseDto(
//            ));
//        }
//        return loadedDataList;
//    }
}
