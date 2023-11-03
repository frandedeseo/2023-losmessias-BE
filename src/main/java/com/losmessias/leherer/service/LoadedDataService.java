package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.LoadedData;
import com.losmessias.leherer.repository.LoadedDataRepository;
import com.losmessias.leherer.repository.interfaces.ProfessorDailySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadedDataService {

    private final LoadedDataRepository loadedDataRepository;
    private final ClassReservationService classReservationService;

    public List<LoadedData> getUploadedData(Long id){
        return loadedDataRepository.getUploadedData(id);
    }
}
