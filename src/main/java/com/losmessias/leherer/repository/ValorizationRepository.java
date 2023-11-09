package com.losmessias.leherer.repository;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Valorization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValorizationRepository  extends JpaRepository<Valorization, Long> {

}
