package com.losmessias.leherer.repository;

import com.losmessias.leherer.domain.NotificationStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationStudentRepository extends JpaRepository<NotificationStudent, Long> {
    List<NotificationStudent> findByStudentId(Long id);

}
