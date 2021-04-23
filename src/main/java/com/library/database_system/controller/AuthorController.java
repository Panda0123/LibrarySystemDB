package com.library.database_system.controller;

import com.library.database_system.projections.IdFNameMNameLName;
import com.library.database_system.domain.Author;
import com.library.database_system.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping(path = "api/v1/author")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(path = "all")
    public Collection<IdFNameMNameLName> getAuthors() { return this.authorService.getAllAuthors(); }

    @GetMapping(path = "all/{authorId}")
    public IdFNameMNameLName getAuthor(@PathVariable("authorId") Long authorId) {
        return this.authorService.getAuthorFullNameOnly(authorId);
    }

    @GetMapping( path = "all/q")
    public IdFNameMNameLName getAuthorByName(
            @RequestParam(required = false) String fName,
            @RequestParam(required = false) String mName,
            @RequestParam(required = false) String lName) {
        HashMap<String, String> attrs = new HashMap<>();
        if (fName != null)
            attrs.put("fName", fName);

        if (mName != null)
            attrs.put("mName", mName);

        if (lName != null)
            attrs.put("lName", lName);
        return this.authorService.findAuthorByName(attrs);
    }

    @PostMapping(path = "admin")
    public void addAuthor(@RequestBody Author author) { this.authorService.addNewAuthor(author); }

    @PutMapping(path = "admin/{authorId}")
    public void updateAuthor(
            @PathVariable("authorId") Long authorId,
            @RequestParam(required = false) String fName,
            @RequestParam(required = false) String mName,
            @RequestParam(required = false) String lName
    ) {
        HashMap<String, String> attrs = new HashMap<>();
        if (fName != null)
            attrs.put("fName", fName);

        if (mName != null)
            attrs.put("mName", mName);

        if (lName != null)
            attrs.put("lName", lName);

        this.authorService.updateAuthor(authorId, attrs);
    }

    @DeleteMapping(path = "admin/{authorId}")
    public void deleteAuthor(@PathVariable Long authorId){
        this.authorService.deleteAuthor(authorId);
    }
}
