package com.example.debricked.rule_engine.services;


import com.example.debricked.rule_engine.commons.Constants;
import com.example.debricked.rule_engine.exception.EmptyFileArgumentException;
import com.example.debricked.rule_engine.model.DependencyFile;
import com.example.debricked.rule_engine.model.FileUploadTransaction;
import com.example.debricked.rule_engine.repository.DependencyFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileProcessingService {

    @Autowired
    private DependencyFileRepository repository;

    public DependencyFile processFile(MultipartFile file, FileUploadTransaction fileUploadTransaction) {
        DependencyFile dependencyFile = new DependencyFile();
        dependencyFile.setFilename(file.getOriginalFilename());
        dependencyFile.setEmail(fileUploadTransaction.getEmail());
        dependencyFile.setVersion(fileUploadTransaction.getVersion());
        dependencyFile.setBranchName(fileUploadTransaction.getBranchName());
        dependencyFile.setRepositoryName(fileUploadTransaction.getRepositoryName());
        dependencyFile.setCommitName(fileUploadTransaction.getCommitName());

        try {
            byte[] content = file.getBytes();
            if(content.length==0){
                String message = String.format("%s file name is empty", file.getOriginalFilename());
                throw new EmptyFileArgumentException(message);
            }
            dependencyFile.setFile(content);
            dependencyFile.setScanStatus(Constants.SAVED);
        } catch (IOException e) {
            dependencyFile.setScanStatus(Constants.ERROR);
        }

        return repository.save(dependencyFile);
    }


    public void updateStatusByUploadId(String uploadId, String status){
        List<DependencyFile> dependencyFiles = repository.findAllByCiUploadId(uploadId);
        for(DependencyFile dependencyFile: dependencyFiles){
            dependencyFile.setScanStatus(status);
            repository.save(dependencyFile);
        }
    }

    public List<DependencyFile> getDependencyFileByIds(List<Integer> idList){
        List<DependencyFile> dependencyFiles = repository.findAllById(idList);

        if(dependencyFiles!=null){
            return dependencyFiles;
        }else{
            return new ArrayList<>();
        }
    }

    public void updateUploadIdAndStatus(String uploadId, String status, DependencyFile file){
        file.setScanStatus(status);
        file.setCiUploadId(uploadId);
        repository.save(file);
    }

    public List<DependencyFile> getAllInProgressFiles(){
        List<DependencyFile> dependencyFiles = repository.findAllByScanStatus(Constants.IN_PROGRESS);
        return dependencyFiles;
    }

}
