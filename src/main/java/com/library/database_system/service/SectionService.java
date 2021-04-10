package com.library.database_system.service;

import com.library.database_system.domain.*;
import com.library.database_system.repository.GradeLevelRepository;
import com.library.database_system.repository.SectionRepository;
import com.library.database_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;
    private final GradeLevelRepository gradeLevelRepository;

    public SectionService(SectionRepository sectionRepository, UserRepository userRepository, GradeLevelRepository gradeLevelRepository) {
        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
        this.gradeLevelRepository = gradeLevelRepository;
    }

    public List<Section> getSections() {
        return this.sectionRepository.findAll();
    }

    public Section getSection(Long id) {
        Section section = this.sectionRepository.findById(id).orElseThrow(
                () -> { return new IllegalStateException("SectionID:" + id + " does not exist");});
        return section;
    }

    public void addNewSection(Section section) {
        // assume gradelevel is not repeating
        Optional<Section> sectionOptional = this.sectionRepository.findByName(section.getName());

        if(sectionOptional.isPresent()) {
            throw new IllegalStateException("Section Name:" + section.getName() + " already exist");
        }
        this.sectionRepository.save(section);
    }

    public void deleteSection(Long sectionId){
        if (this.sectionRepository.existsById(sectionId)) {
            this.sectionRepository.deleteById(sectionId);
        } else {
            throw new IllegalStateException("SectionID:" + sectionId + " does not exist");
        }
    }

    @Transactional
    public void updateSection(Long sectionId, HashMap<String, String> attrs) {
        Section section = this.sectionRepository.findById(sectionId).orElseThrow(
                () -> {throw new IllegalStateException("SectionID:" + sectionId + " does not exist");} );

        for(String key: attrs.keySet()) {
            String value = attrs.get(key);
            if (value != null) {
                switch (key) {
                    case "name": section.setName(value); break;
                    case "gradeLevel":
                        GradeLevel gl = this.gradeLevelRepository.findById(Long.parseLong(value)).orElseThrow(
                                () -> {throw new IllegalStateException("GradeLevelID" + value + " does not exist");});
                        this.updateGradeLevel(section, gl);
                        break;
                }
            }
        }
    }

    private void updateGradeLevel(Section section, GradeLevel newGradeLevel) {
        GradeLevel prevGradeLevel = section.getGradeLevel();

        if (!Objects.equals(prevGradeLevel, newGradeLevel)) {
            if (prevGradeLevel != null )
                prevGradeLevel.getSections().remove(section);
            section.setGradeLevel(newGradeLevel);
        }

        if (!newGradeLevel.getSections().contains(section))
            newGradeLevel.getSections().add(section);
    }
//    @Transactional
//    public void addUserToSection(Long sectionId, String userId) {
//        Section section = this.sectionRepository.findById(sectionId).orElseThrow(
//                () -> {throw new IllegalStateException("Section does not exist");} );
//        User user = this.userRepository.findById(userId).orElseThrow(
//                () -> {throw new IllegalStateException("User does not exist");} );
//
//        if (!section.getUsers().contains(user)) {
//            section.getUsers().add(user);
//        }
//
//        if (!Objects.equals(user.getSection(), section)) {
//            user.setSection(section);
//        }
//    }
//
//    @Transactional
//    public void removeUserFromSection(Long sectionId, String userId) {
//        Section section = this.sectionRepository.findById(sectionId).orElseThrow(
//                () -> {throw new IllegalStateException("Section does not exist");} );
//        User user = this.userRepository.findById(userId).orElseThrow(
//                () -> {throw new IllegalStateException("User does not exist");} );
//
//        if (section.getUsers().contains(user)) {
//            section.getUsers().remove(user);
//        }
//    }
}
