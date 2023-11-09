package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.Valorization;
import com.losmessias.leherer.dto.ValorizationDto;
import com.losmessias.leherer.service.ValorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/valorization")
@RequiredArgsConstructor
@CrossOrigin
public class ValorizationController {

    private final ValorizationService valorizationService;

    @PostMapping("/giveValorization")
    public Valorization giveValorization(@RequestBody ValorizationDto valorizationDto){
        return valorizationService.giveValorization(valorizationDto);

    }

    @GetMapping("/getAllValorizations")
    public ResponseEntity<String> getAllValorizations() throws JsonProcessingException {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(valorizationService.getAllValorizations()));
    }
}
