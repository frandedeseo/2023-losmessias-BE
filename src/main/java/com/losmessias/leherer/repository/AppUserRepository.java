package com.losmessias.leherer.repository;

import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository
        extends JpaRepository<AppUser, Long> {
    AppUser findByEmail(String email);
    AppUser findByAssociationIdAndAppUserRole(Long associationId, AppUserRole appUserRole);
}