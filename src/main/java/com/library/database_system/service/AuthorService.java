package com.library.database_system.service;

import com.library.database_system.projections.IdFNameMNameLName;
import com.library.database_system.domain.Author;
import com.library.database_system.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Collection<IdFNameMNameLName> getAllAuthors() {
        return this.authorRepository.findAllAuthors();
    }

    public Author getAuthor(Long authorId) {
        Author author = this.authorRepository.findById(authorId).orElseThrow(
                () -> { return new IllegalStateException("AuthorID:" + authorId + " does not exist");});
        return author;
    }

    public IdFNameMNameLName getAuthorFullNameOnly(Long authorId) {
        Optional<IdFNameMNameLName> author = this.authorRepository.findByIdFullNameOnly(authorId);
        if (!author.isPresent()) {
            throw new IllegalStateException("AuthorID:" + authorId + " does not exist");
        }
        return author.get();
    }

    public IdFNameMNameLName findAuthorByName(HashMap<String, String> attrs) {
        String fName = attrs.get("fName");
        String mName = attrs.get("mName");
        String lName = attrs.get("lName");
        Optional<IdFNameMNameLName> authorOpt = null;
        IdFNameMNameLName author = null;

        // 1st case contains all
        if (fName != null &&  mName != null && lName != null){
           authorOpt = this.authorRepository.findAuthorDTOByFullName(fName, mName, lName);
        }

        if (authorOpt != null && authorOpt.isPresent()) {
            author = authorOpt.get();
        } else {
            author = new IdFNameMNameLName() {
                @Override
                public Long getid() {
                    return -1L;
                }
                @Override
                public String getf_name() {
                    return null;
                }
                @Override
                public String getm_name() {
                    return null;
                }
                @Override
                public String getl_name() {
                    return null;
                }
            };
        }
        return author;
    }

    public void addNewAuthor(Author author) {
        Optional<Author> optionalAuthor = this.authorRepository
                .findAuthorByFullName(
                        author.getf_name(), author.getm_name(), author.getl_name());

        if (optionalAuthor.isPresent()) {
            throw new IllegalStateException("Author already exist");
        } else {
            this.authorRepository.save(author);
        }
    }

    public void deleteAuthor(Long authorId) {
        if (!this.authorRepository.existsById(authorId)) {
            throw new IllegalStateException("AuthorID:" + authorId + " does not exist");
        }
        this.authorRepository.deleteById(authorId);
    }

    @Transactional
    public void updateAuthor(Long authorId, HashMap<String, String> attrs) {
        Author author = this.authorRepository.findById(authorId).orElseThrow(
                () -> { throw new IllegalStateException("no author");});

        for (String key: attrs.keySet()) {
            String value = attrs.get(key);
            switch (key) {
                case "fName":
                    if (value != null && !Objects.equals(author.getf_name(), value))
                        author.setf_name(value);
                    break;
                case "mName":
                    if (!Objects.equals(author.getm_name(), value))
                        author.setm_name(value);
                    break;
                case "lName":
                    if (!Objects.equals(author.getl_name(), value))
                        author.setl_name(value);
                    break;
            }
        }
    }
}
