package com.losmessias.leherer.repository;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.NotificationProfessor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationProfessorRepository extends JpaRepository<NotificationProfessor, Long> {
    List<NotificationProfessor> findByProfessorId(Long id);

}
