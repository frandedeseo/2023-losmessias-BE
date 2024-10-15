package com.losmessias.leherer.repository;

import com.losmessias.leherer.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("SELECT COALESCE(AVG(f.rating), 0) FROM Feedback f WHERE f.receiver.id = ?1")
    Double getAvgRating(Long receiverId);
}
