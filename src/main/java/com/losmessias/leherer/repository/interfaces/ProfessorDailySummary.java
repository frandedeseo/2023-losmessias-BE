package com.losmessias.leherer.repository.interfaces;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Subject;

public interface ProfessorDailySummary {
    Professor getProfessor();
    Subject getSubject();
    Double getTotalHours();
    Integer getTotalIncome();
}
