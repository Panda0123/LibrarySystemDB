package com.library.database_system.service;

import com.library.database_system.domain.GradeLevel;
import com.library.database_system.domain.Section;
import com.library.database_system.repository.GradeLevelRepository;
import com.library.database_system.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GradeLevelService {

    private final GradeLevelRepository gradeLevelRepository;
    private final SectionRepository sectionRepository;

    @Autowired
    public GradeLevelService(GradeLevelRepository gradeLevelRepository, SectionRepository sectionRepository) {
        this.gradeLevelRepository = gradeLevelRepository;
        this.sectionRepository = sectionRepository;
    }

    public List<GradeLevel> getGradeLevels() {
        return this.gradeLevelRepository.findAll();
    }

    public GradeLevel getGradeLevel(Long id) {
        GradeLevel gl = this.gradeLevelRepository.findById(id).orElseThrow(
                () -> { return new IllegalStateException("GradeLevelID:" + id + " does not exist");});
        return gl;
    }

    public void addNewGradeLevel(GradeLevel gradeLevel) {
        Optional<GradeLevel> glOptional = this.gradeLevelRepository.findByLevel(gradeLevel.getLevel());

        if(glOptional.isPresent()) {
            throw new IllegalStateException("GradeLevel:" + gradeLevel.getLevel() + " exist already");
        }
        this.gradeLevelRepository.save(gradeLevel);
    }

    public void deleteGradeLevel(Long gradeLevelId){
        if (this.gradeLevelRepository.existsById(gradeLevelId)) {
            this.gradeLevelRepository.deleteById(gradeLevelId);
        } else {
            throw new IllegalStateException("GradeLevelID:" + gradeLevelId + " does not exist");
        }
    }

    public boolean isEmpty(){
        return this.gradeLevelRepository.findAll().size() == 0;
    }

    @Transactional
    public void updateGradeLevel(Long glId, int level) {
        GradeLevel gl = this.gradeLevelRepository.findById(glId).orElseThrow(
                () -> { throw new IllegalStateException("GradeLevelID:" + glId + " does not exist");});
        if (level > 0 && !Objects.equals(gl.getLevel(), level)) {
            gl.setLevel(level);
        }
    }

    @Transactional
    public void addSectionToGradeLevel(Long glId, Long sectionId) {
        GradeLevel gl = this.gradeLevelRepository.findById(glId).orElseThrow(
                () -> { throw new IllegalStateException("GradeLevelID:" + glId + " does not exist");});
        Section section = this.sectionRepository.findById(sectionId).orElseThrow(
                () -> { throw new IllegalStateException("SectionID:" + sectionId + " does not exist");});

        if(!gl.getSections().contains(section)) {
            gl.getSections().add(section);
        }

        if(!Objects.equals(section.getGradeLevel(), gl) ) {
            section.setGradeLevel(gl);
        }
    }

//    @Transactional
//    public void removeSectionFromGradeLevel(Long glId, Long sectionId) {
//        GradeLevel gl = this.gradeLevelRepository.findById(glId).orElseThrow(
//                () -> { throw new IllegalStateException("GradeLevelID:" + glId + " does not exist");});
//        Section section = this.sectionRepository.findById(sectionId).orElseThrow(
//                () -> { throw new IllegalStateException("SectionID:" + sectionId + " does not exist");});
//
//        if(gl.getSections().contains(section)) {
//            gl.getSections().remove(section);
//        }
//
//        if(Objects.equals(section.getGradeLevel(), gl)) {
//            section.setGradeLevel(null);
//        }
//    }
}
