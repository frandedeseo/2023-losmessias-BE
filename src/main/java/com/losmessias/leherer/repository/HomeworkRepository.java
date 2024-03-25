package com.losmessias.leherer.repository;

import com.losmessias.leherer.domain.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {

    List<Homework> findByClassReservation_Id(Long id);

    @Query("SELECT h FROM Homework h WHERE h.classReservation.student.id = ?1")
    List<Homework> findByStudentId(Long id);
}
