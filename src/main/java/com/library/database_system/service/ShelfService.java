package com.library.database_system.service;

import com.library.database_system.domain.Author;
import com.library.database_system.domain.Category;
import com.library.database_system.domain.Shelf;
import com.library.database_system.repository.ShelfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ShelfService {

    private final ShelfRepository shelfRepository;

    @Autowired
    public ShelfService(ShelfRepository shelfRepository) {
        this.shelfRepository = shelfRepository;
    }

    public List<Shelf> getShelves() {
        return this.shelfRepository.findAll();
    }

    public Shelf getShelf(Long id) {
        Shelf shelf = this.shelfRepository.findById(id).orElseThrow(
                () -> { return new IllegalStateException("ShelfID:" + id + " does not exist");});
        return shelf;
    }

    public void addNewShelf(Shelf shelf) {
        Optional<Shelf> shelfOptional = this.shelfRepository.findByName(shelf.getName());

        if(shelfOptional.isPresent()) {
            throw new IllegalStateException("Shelf adding already exist");
        }
        this.shelfRepository.save(shelf);
    }

    public void deleteShelf(Long shelfId){
        boolean doesExist = this.shelfRepository.existsById(shelfId);

        if (doesExist) {
            this.shelfRepository.deleteById(shelfId);
        } else {
            throw new IllegalStateException("Shelf does not exist");
        }
    }

    @Transactional
    public void updateShelf(Long shelfId, String name) {
        Shelf shelf = this.shelfRepository.findById(shelfId).orElseThrow(
                () -> { throw new IllegalStateException("no shelf");});

        if (name != null && !Objects.equals(shelf.getName(), name)) {
            shelf.setName(name);
        }
    }
}
