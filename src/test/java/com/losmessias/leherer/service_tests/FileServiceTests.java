package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.*;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.dto.UploadInformationDto;
import com.losmessias.leherer.repository.FileRepository;
import com.losmessias.leherer.repository.HomeworkRepository;
import com.losmessias.leherer.repository.LoadedDataRepository;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.service.FileService;
import com.losmessias.leherer.service.HomeworkService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileServiceTests {

    @Mock
    private FileRepository dbFileRepository;
    @Mock
    private LoadedDataRepository loadedDataRepository;
    @Mock
    private ClassReservationService classReservationService;
    @Mock
    private HomeworkService homeworkService;
    @Mock
    private HomeworkRepository homeworkRepository;

    @InjectMocks
    private FileService fileService;

    @Test
    @DisplayName("Files stores successfully")
    void setUploadInformation() {
        Professor professor = new Professor();
        Subject subject = new Subject("Biology");
        Student student = new Student();
        ClassReservation class1 = new ClassReservation(
                professor,
                subject,
                student,
                LocalDate.of(2023, 10, 1),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                0.0,
                100
        );

        UploadInformationDto info = new UploadInformationDto(
                1L,
                1L,
                LocalDateTime.of(2023, 10, 1, 10, 10, 10),
                AppUserRole.PROFESSOR,
                1L,
                1L
        );
        File file = new File("hola.pdf", "content/pdf", null);
        File resultingFile = new File(class1, LocalDateTime.of(2023, 10, 1, 10, 10, 10), AppUserRole.PROFESSOR, 1L, "hola.pdf", "content/pdf", null);

        when(classReservationService.getReservationById(any())).thenReturn(class1);
        when(dbFileRepository.findById(any())).thenReturn(Optional.of(file));
        assertEquals(resultingFile, fileService.setUploadInformation(info));
    }

    @Test
    @DisplayName("Files stores successfully")
    void testStoreFile() {
        byte[] inputArray = "Test String".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("tempFileName",inputArray);
        when(dbFileRepository.save(any())).thenReturn(new File());
        assertEquals(new File(), fileService.storeFile(mockMultipartFile));
    }

}
