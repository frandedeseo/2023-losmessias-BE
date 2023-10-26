package com.losmessias.leherer.repository;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Subject;
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

    @Query("SELECT  c.professor AS professor, c.subject AS subject,  SUM(c.price) AS totalIncome, SUM(c.duration) AS totalHours " +
            "FROM ClassReservation c " +
            "WHERE  c.date = ?1 " +
            "AND (c.status = 'CONFIRMED' OR c.status = 'CONCLUDED' OR c.status = 'CANCELLED') " +
            "GROUP BY c.professor.id, c.subject.id")
    List<ProfessorDailySummary> getProfessorDailySummaryByDay(LocalDate day);

    @Query("SELECT c FROM ClassReservation c WHERE c.professor.id = ?1 ORDER BY c.date ASC ")
    List <ClassReservation> getClassReservationByProfessorAndOrderByDate(Long id);


    List<ClassReservation> findByProfessorAndSubject(Professor professor, Subject subject);

}
