package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.Comment;
import com.losmessias.leherer.dto.CommentDto;
import com.losmessias.leherer.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ClassReservationService classReservationService;

    public Comment upload(CommentDto commentDto) {
        Comment comment = new Comment(
                commentDto.getText(),
                classReservationService.getReservationById(commentDto.getClassReservation()),
                commentDto.getUploadedDateTime(),
                commentDto.getRole(),
                commentDto.getAssociatedId(),
                commentDto.isBelongsToHomework()
        );
        return commentRepository.save(comment);
    }
}
