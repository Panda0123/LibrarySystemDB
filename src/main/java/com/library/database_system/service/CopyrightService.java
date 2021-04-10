package com.library.database_system.service;

import com.library.database_system.domain.Copyright;
import com.library.database_system.domain.Shelf;
import com.library.database_system.repository.CopyrightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CopyrightService {

    private final CopyrightRepository copyrightRepository;

    @Autowired
    public CopyrightService(CopyrightRepository copyrightRepository) {
        this.copyrightRepository = copyrightRepository;
    }

    public List<Copyright> getCopyrights() {
        return this.copyrightRepository.findAll();
    }

    public Copyright getCopyright(Long id) {
        Copyright copyright = this.copyrightRepository.findById(id).orElseThrow(
                () -> { return new IllegalStateException("CopyrightID:" + id + " does not exist");});
        return copyright;
    }

    public void addNewCopyright(Copyright copyright) {
        Optional<Copyright> copyrightOptional = this.copyrightRepository.findByName(copyright.getName());

        if(copyrightOptional.isPresent()) {
            throw new IllegalStateException("Copyright already exist");
        }
        this.copyrightRepository.save(copyright);
    }

    public void deleteCopyright(Long copyrightId){
        boolean doesExist = this.copyrightRepository.existsById(copyrightId);

        if (doesExist) {
            this.copyrightRepository.deleteById(copyrightId);
        } else {
            throw new IllegalStateException("CopyrightID:" + copyrightId + " does not exist");
        }
    }

    @Transactional
    public void updateCopyright(Long copyrightId, String name) {
        Copyright copyright = this.copyrightRepository.findById(copyrightId).orElseThrow(
                () -> { throw new IllegalStateException("no copyright");});

        if (name != null && !Objects.equals(copyright.getName(), name)) {
            copyright.setName(name);
        }
    }
}
