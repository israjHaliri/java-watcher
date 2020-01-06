package com.haliri.israj.javawatcher.service;

import com.haliri.israj.javawatcher.App;
import com.haliri.israj.javawatcher.entity.IncomingFile;
import com.haliri.israj.javawatcher.entity.Type;
import com.haliri.israj.javawatcher.repository.IncomingFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class IncomingFileService {

    @Autowired
    private IncomingFileRepository incomingFileRepository;

    public void processFile(String filename){
        IncomingFile incomingFile = new IncomingFile();
        incomingFile.setFilename(filename);
        incomingFile.setDateCreated(new Date());
        incomingFile.setStatus(Type.PROCESSED);

        //handle by pesimistic locking
        try {
            incomingFileRepository.save(incomingFile);

            processDetailFile();
        }catch (Exception e){
            App.getLogger(this).info("Can't process file because already processed");
        }
    }

    public void processDetailFile(){
        App.getLogger(this).info("PROCESSING DETAIL");
    }
}
