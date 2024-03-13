package com.losmessias.leherer.repository;

import com.losmessias.leherer.domain.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {

    //#TODO: TEST METHOD
    List<Homework> findByClassReservation_Id(Long id);
}
