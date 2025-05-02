package com.example.alphasolutionsaeproject.service;
import com.example.alphasolutionsaeproject.model.Project;
import com.example.alphasolutionsaeproject.model.Subproject;
import org.springframework.stereotype.Service;
import com.example.alphasolutionsaeproject.repository.SubprojectRepository;

import java.util.List;

@Service
public class SubprojectService {

    private final SubprojectRepository subprojectRepository;

    public SubprojectService(SubprojectRepository subprojectRepository) {
        this.subprojectRepository = subprojectRepository;
    }

    public List<Subproject> getAllSubprojects() {
        return subprojectRepository.findAll();
    }

    public Subproject getSubprojectById(int id) {
        return subprojectRepository.findById(id);
    }

    public void addSubproject(Subproject subproject) {
        subprojectRepository.save(subproject);
    }

    public void updateSubproject(Subproject subproject) {
        subprojectRepository.update(subproject);
    }

    public void deleteSubproject(int id) {
        subprojectRepository.delete(id);
    }


    public List<Subproject> getAllSubprojectsByProjectId(int id){
        return subprojectRepository.getAllProjectsByProjectId(id);
    }
}
