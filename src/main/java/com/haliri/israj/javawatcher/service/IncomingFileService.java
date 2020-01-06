package com.haliri.israj.javawatcher.service;

import com.haliri.israj.javawatcher.App;
import com.haliri.israj.javawatcher.entity.IncomingFile;
import com.haliri.israj.javawatcher.entity.Type;
import com.haliri.israj.javawatcher.repository.IncomingFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;

@Service
public class IncomingFileService {

    @Autowired
    private IncomingFileRepository incomingFileRepository;

    public void processFile(String filename) {
        IncomingFile incomingFile = new IncomingFile();
        incomingFile.setFilename(filename);
        incomingFile.setDateCreated(new Date());
        incomingFile.setStatus(Type.PROCESSED);

        if (incomingFileRepository.findFirstByFilename(filename) != null) return;

        try {
            incomingFileRepository.save(incomingFile);

            new Thread(() -> {
                Type type = processDetailFile(filename);

                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                App.getLogger(this).info("UPDATE STATE " + filename);
                incomingFile.setStatus(type);
                incomingFileRepository.save(incomingFile);

            }).start();
        } catch (DataIntegrityViolationException e) {
            App.getLogger(this).info("Can't process file because already processed");
        } catch (Exception e) {
            App.getLogger(this).error(e.getLocalizedMessage());
        }
    }

    public Type processDetailFile(String filename) {
        App.getLogger(this).info("PROCESSING DETAIL " + filename);

        try{
//            save content bulk/batch
//            List<Object> dataList = Arrays.asList("1", "2");
//            customerRepository.saveAll(dataList);

            return Type.DONE;
        }catch (Exception e){

            //if something wrong with content then update to FAILED
            return Type.FAILED;
        }
    }
}
