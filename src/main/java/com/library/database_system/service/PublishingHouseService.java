package com.library.database_system.service;

import com.library.database_system.domain.PublishingHouse;
import com.library.database_system.repository.PublishingHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PublishingHouseService {

    private final PublishingHouseRepository publishingHouseRepository;

    @Autowired
    public PublishingHouseService(PublishingHouseRepository publishingHouseRepository) {
        this.publishingHouseRepository = publishingHouseRepository;
    }

    public List<PublishingHouse> getPublishers() {
        return this.publishingHouseRepository.findAll();
    }

    public PublishingHouse getPublishingHouse(Long id) {
        PublishingHouse publisher = this.publishingHouseRepository.findById(id).orElseThrow(
                () -> { return new IllegalStateException("PublishingHouseID:" + id + " does not exist");});
        return publisher;
    }

    public void addNewPublishingHouse(PublishingHouse publisher) {
        Optional<PublishingHouse> publisherOptional = this.publishingHouseRepository.findByName(publisher.getName());

        if(publisherOptional.isPresent()) {
            throw new IllegalStateException("Publishing House already exist");
        }
        this.publishingHouseRepository.save(publisher);
    }

    public void deletePublishingHouse(Long id){
        boolean doesExist = this.publishingHouseRepository.existsById(id);

        if (doesExist) {
            this.publishingHouseRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Book does not exist");
        }
    }

    @Transactional
    public void updatePublishingHouse(Long publisherId, String name, String address) {
        PublishingHouse publisher = this.publishingHouseRepository.findById(publisherId).orElseThrow(
                () -> { throw new IllegalStateException("no publisher");});

        if (name != null && !Objects.equals(publisher.getName(), name)) {
            publisher.setName(name);
        }
        if (address != null && !Objects.equals(publisher.getAddress(), address)) {
            publisher.setAddress(address);
        }
    }
}
