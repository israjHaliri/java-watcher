package com.haliri.israj.javawatcher.repository;

import com.haliri.israj.javawatcher.entity.IncomingFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface IncomingFileRepository extends JpaRepository<IncomingFile, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @SuppressWarnings("unchecked")
    IncomingFile save(IncomingFile incomingFile);
}
