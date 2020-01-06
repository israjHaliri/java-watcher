package com.haliri.israj.javawatcher.repository;

import com.haliri.israj.javawatcher.entity.IncomingFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomingFileRepository extends JpaRepository<IncomingFile, String> {

    IncomingFile findFirstByFilename(String filename);
}
