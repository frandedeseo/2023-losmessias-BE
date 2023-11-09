package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.Valorization;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.Feedback;
import com.losmessias.leherer.dto.ClassReservationResponseDto;
import com.losmessias.leherer.dto.ValorizationDto;
import com.losmessias.leherer.repository.ValorizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ValorizationService {

    private final StudentService studentService;
    private final ProfessorService professorService;
    private final ValorizationRepository valorizationRepository;

    public Valorization giveValorization(ValorizationDto valorizationDto){
        Student student = studentService.getStudentById(valorizationDto.getStudentId());
        Professor professor = professorService.getProfessorById(valorizationDto.getProfessorId());
        if (valorizationDto.getRoleReceptor() == AppUserRole.STUDENT){
            studentService.setFeedback(student, valorizationDto.getRating(), valorizationDto.getMaterial(), valorizationDto.getPunctuality(), valorizationDto.getEducated());
        }else{
            professorService.setFeedback(professor, valorizationDto.getRating(), valorizationDto.getMaterial(), valorizationDto.getPunctuality(), valorizationDto.getEducated());
        }
        Set<Feedback> feedbacks = new HashSet<>();
        if (valorizationDto.getEducated()){feedbacks.add(Feedback.EDUCATED);}
        if (valorizationDto.getPunctuality()){feedbacks.add(Feedback.PUNCTUALITY);}
        if (valorizationDto.getMaterial()){feedbacks.add(Feedback.MATERIAL);}
        Valorization valorization =  new Valorization(student, professor, valorizationDto.getRoleReceptor(), feedbacks, valorizationDto.getRating() );
        valorizationRepository.save(valorization);
        return valorization;
    }

    public List<Valorization> getAllValorizations() {
        return valorizationRepository.findAll();
    }
}
