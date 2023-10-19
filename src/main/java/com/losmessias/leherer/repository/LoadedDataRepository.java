package com.losmessias.leherer.repository;

import com.losmessias.leherer.domain.LoadedData;
import com.losmessias.leherer.repository.interfaces.ProfessorDailySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoadedDataRepository extends JpaRepository<LoadedData, Long> {

    @Query("SELECT data FROM LoadedData data WHERE  data.classReservation.id = ?1 ORDER BY data.uploadedDateTime ASC")
    List<LoadedData> getUploadedData(Long id);
}
