package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.File;
import com.losmessias.leherer.domain.LoadedData;
import com.losmessias.leherer.dto.UploadInformationDto;
import com.losmessias.leherer.exception.FileNotFoundException;
import com.losmessias.leherer.exception.FileStorageException;
import com.losmessias.leherer.repository.FileRepository;
import com.losmessias.leherer.repository.LoadedDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final ClassReservationService classReservationService;
    private final LoadedDataRepository loadedDataRepository;

    public File storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            File dbFile = new File(
                    fileName,
                    file.getContentType(),
                    file.getBytes()
            );

            return fileRepository.save(dbFile);

        } catch (IOException exception) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", exception);
        }
    }

    public LoadedData setUploadInformation(UploadInformationDto info) {
        LoadedData file = getFile(info.getIdFile());

        file.setAssociatedId(info.getAssociatedId());
        file.setRole(info.getRole());
        file.setClassReservation(classReservationService.getReservationById(info.getClassReservation()));
        file.setUploadedDateTime(info.getUploadedDateTime());
        file.setBelongsToHomework(info.getHomeworkId() != null);

        loadedDataRepository.save(file);
        return file;
    }

    public File getFile(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
    }

    public File setBelongingToHomework(Long fileId) {
        File file = getFile(fileId);
        file.setBelongsToHomework(true);
        return fileRepository.save(file);
    }
}
