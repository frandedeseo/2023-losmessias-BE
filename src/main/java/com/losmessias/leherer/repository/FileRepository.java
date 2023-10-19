package com.losmessias.leherer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.losmessias.leherer.domain.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

}

