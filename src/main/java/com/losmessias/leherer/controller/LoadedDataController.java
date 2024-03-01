package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.service.LoadedDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loadedData")
@RequiredArgsConstructor
@CrossOrigin
public class LoadedDataController {

    private final LoadedDataService loadedDataService;
    @GetMapping("/get-uploaded-data")
    public ResponseEntity<String> getUploadedData(@RequestParam("id") Long Id) throws JsonProcessingException {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(loadedDataService.getUploadedData(Id)));
    }
    
}
