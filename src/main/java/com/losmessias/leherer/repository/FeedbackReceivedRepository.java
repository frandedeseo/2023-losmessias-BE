package com.losmessias.leherer.repository;

import com.losmessias.leherer.domain.FeedbackReceived;
import com.losmessias.leherer.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackReceivedRepository extends JpaRepository<FeedbackReceived, Long> {
}
