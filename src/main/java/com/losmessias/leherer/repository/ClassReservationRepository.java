package com.losmessias.leherer.repository;

import com.losmessias.leherer.domain.ClassReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassReservationRepository extends JpaRepository<ClassReservation, Long> {
    List<ClassReservation> findByProfessorId(Long id);
    List<ClassReservation> findByStudentId(Long id);
    List<ClassReservation> findBySubjectId(Long id);
}
