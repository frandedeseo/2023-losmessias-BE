package com.losmessias.leherer.repository;

import com.losmessias.leherer.domain.Comment;
import com.losmessias.leherer.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
