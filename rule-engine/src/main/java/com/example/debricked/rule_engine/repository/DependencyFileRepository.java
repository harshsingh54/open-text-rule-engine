package com.example.debricked.rule_engine.repository;

import com.example.debricked.rule_engine.model.DependencyFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DependencyFileRepository extends JpaRepository<DependencyFile, Integer> {

    public List<DependencyFile> findAllByCiUploadId(String ciUploadId);

    public List<DependencyFile> findAllByScanStatus(String status);
}
