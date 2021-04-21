package com.library.database_system.service;

import com.library.database_system.domain.GradeLevel;
import com.library.database_system.domain.Section;
import com.library.database_system.domain.User;
import com.library.database_system.dtos.SectionDTO;
import com.library.database_system.dtos.UserDTO;
import com.library.database_system.projections.UserProj;
import com.library.database_system.repository.BookRepository;
import com.library.database_system.repository.GradeLevelRepository;
import com.library.database_system.repository.SectionRepository;
import com.library.database_system.repository.UserRepository;
import com.library.database_system.resulttransformer.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SectionRepository sectionRepository;
    private final GradeLevelRepository gradeLevelRepository;
    private final BookRepository bookRepository;

    @Autowired
    public UserService(UserRepository userRepository, SectionRepository sectionRepository, GradeLevelRepository gradeLevelRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.sectionRepository = sectionRepository;
        this.gradeLevelRepository = gradeLevelRepository;
        this.bookRepository = bookRepository;
    }

    public List<User> getUsers() {
        List<User> users = this.userRepository.findAll();
        return users;
    }

    public List<UserTransformer> getUsersDetails() {
        return this.userRepository.getUsersDetails();
    }

    public User getUser(String id) {
        User user = this.userRepository.findById(id).orElseThrow(
                () -> {throw  new IllegalStateException("UserID:" + id + " does not exist");});
        return user;
    }

    public User findUser(String id) {
        User user = this.userRepository.findById(id).orElseThrow(
                () -> {throw  new IllegalStateException("UserID:" + id + " does not exist");});
        return user;
    }

    public UserProj findUserDetails(String id) {
        UserProj userProj = this.userRepository.findUserById(id).orElseThrow(
                () -> {throw  new IllegalStateException("UserID:" + id + " does not exist");});
        return userProj;
    }

    public void addUser(User user) {
        boolean doesExist = this.userRepository.existsById(user.getId());

        if (doesExist) {
            throw new IllegalStateException("UserID:" + user.getId() + " does already exist");
        }
        this.userRepository.save(user);
    }

    public void deleteUser(String id) {
        Optional<User> user = this.userRepository.findById(id);
        if (!user.isPresent()) {
            throw new IllegalStateException("UserID:" + id + " does not exist");
        }
        this.userRepository.deleteById(id);
    }

    public void updateUser(String userId, HashMap<String, String> attrs) {
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> { throw new IllegalStateException("UserID:" + userId + " does not exist");}
        );

        for(String key: attrs.keySet()) {
            String value = attrs.get(key);
            if (value != null) {
                switch (key) {
                    case "userId":
                        // check if new id already exist
                        Optional<User> newUser = this.userRepository.findById(value);
                        if (newUser.isPresent()) {
                            throw new IllegalStateException("UserID:" + value + " already exist");
                        }
                        user.setId(value);
                        break;
                        // check if names are equal to old
                    case "fName":
                        if (value != "" && !Objects.equals(value, user.getfName()))
                            user.setfName(value);
                        break;
                    case "mName":
                        if (value != "" && !Objects.equals(value, user.getmName()))
                            user.setmName(value);
                        break;
                    case "lName":
                        if (value != "" && !Objects.equals(value, user.getlName()))
                            user.setlName(value);
                        break;
                    case "address":
                        if (value != "" && !Objects.equals(value, user.getAddress()))
                            user.setAddress(value);
                        break;
                    case "section":
                        Section section = this.sectionRepository.findById(Long.parseLong(value)).orElseThrow(
                                () -> {throw new IllegalStateException("SectionID:" + value + " does not exist");});
                        this.updateSection(user, section);
                        break;
                }
            }
        }
    }

    private void updateSection(User user, Section newSection) {
        Section prevSection = user.getSection();
        if (!Objects.equals(prevSection, newSection)) {
            if (prevSection != null )
                prevSection.getUsers().remove(user);
            user.setSection(newSection);
        }
        if (!newSection.getUsers().contains(user))
            newSection.getUsers().add(user);
    }


    @Transactional
    public void updateUser(User user, UserDTO userDTO) {
        if (!user.getfName().equals(userDTO.getfName()))
            user.setfName(userDTO.getfName());
        if (!user.getmName().equals(userDTO.getmName()))
            user.setmName(userDTO.getmName());
        if (!user.getlName().equals(userDTO.getlName()))
            user.setlName(userDTO.getlName());
        if (!user.getUserType().equals(userDTO.getType()))
            user.setUserType(userDTO.getType());
        if (!user.getAddress().equals(userDTO.getAddress()))
            user.setAddress(userDTO.getAddress());
        if (!userDTO.getSectionDTO().getName().equals(user.getSection().getName()) ||
                userDTO.getGradeLevelDTO().getLevel() != user.getSection().getGradeLevel().getLevel()){
            updateUserSectionAndGradeLevel(user, userDTO);
        }
    }

    @Transactional
    public void updateUserSectionAndGradeLevel(User user, UserDTO userDTO) {

        Optional<GradeLevel> gradeLevelOpt = this.gradeLevelRepository.findByLevel(userDTO.getGradeLevelDTO().getLevel());
        GradeLevel gradeLevel = null;
        if (gradeLevelOpt.isPresent()) {
            gradeLevel = gradeLevelOpt.get();
        } else {
            gradeLevel = new GradeLevel();
            gradeLevel.setLevel(userDTO.getGradeLevelDTO().getLevel());
            this.gradeLevelRepository.save(gradeLevel);
        }

        // check section
        if (user.getSection() != null) {
            Section prevSection = user.getSection();
            removeSectionFromUser(prevSection, user);
        }
        SectionDTO sectionDTO = userDTO.getSectionDTO();
        List<Section> sections = gradeLevel.getSections()
                .stream()
                .filter(sectionTemp -> sectionTemp.getName().equals(sectionDTO.getName()))
                .collect(Collectors.toList());
        Section section = null;
        if (!sections.isEmpty()) {
            section = sections.get(0);
        } else {
            section = new Section();
            section.setName(sectionDTO.getName());
            section.setGradeLevel(gradeLevel);
            this.sectionRepository.save(section);
        }
        user.setSection(section);
    }

    @Transactional
    public void removeSectionFromUser(Section section, User user) {
        section.getUsers().remove(user);
        user.setSection(null);
        if (section.getUsers().isEmpty()) {
            GradeLevel gradeLevel = section.getGradeLevel();
            gradeLevel.getSections().remove(section);
            if (gradeLevel.getSections().isEmpty())
                this.gradeLevelRepository.delete(gradeLevel);
            this.sectionRepository.delete(section);
        }
    }

}
