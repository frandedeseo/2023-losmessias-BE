package com.losmessias.leherer.repository;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.enumeration.ReservationStatus;
import com.losmessias.leherer.repository.interfaces.ProfessorDailySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClassReservationRepository extends JpaRepository<ClassReservation, Long> {
    List<ClassReservation> findByProfessorId(Long id);

    List<ClassReservation> findByStudentId(Long id);

    List<ClassReservation> findBySubjectId(Long id);

    @Query("SELECT  c.professor.id AS professorId, c.subject.id AS subjectId,  SUM(c.price) AS totalIncome, SUM(c.duration) AS totalHours " +
            "FROM ClassReservation c " +
            "WHERE  c.date = ?1 " +
            "AND (c.status = 1 OR c.status = 2 ) " +
            "GROUP BY c.professor.id, c.subject.id")
    List<ProfessorDailySummary> getProfessorDailySummaryByDay(LocalDate day);

}
