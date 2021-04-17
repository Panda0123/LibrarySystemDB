package com.library.database_system.controller;


import com.library.database_system.dtos.BorrowDTO;
import com.library.database_system.dtos.ReservationDTO;
import com.library.database_system.projections.BookCopyProj;
import com.library.database_system.projections.BorrowProj;
import com.library.database_system.projections.CollectionProj;
import com.library.database_system.projections.ReservationProj;
import com.library.database_system.resulttransformer.BookTransformer;
import com.library.database_system.domain.*;
import com.library.database_system.dtos.BookDetailsDTO;
import com.library.database_system.service.BookService;
import com.library.database_system.service.BorrowService;
import com.library.database_system.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping(path = "api/v1/book")
public class BookController {

    private final BookService bookService;
    private final BorrowService borrowService;
    private final ReservationService reservationService;

    @Autowired
    public BookController(BookService bookService, BorrowService borrowService, ReservationService reservationService) {
        this.bookService = bookService;
        this.borrowService = borrowService;
        this.reservationService = reservationService;
    }

    @GetMapping(path = "all")
    public Collection<BookTransformer> getBooksDetails() {
        return this.bookService.getBooksDetails();
    }

    @GetMapping(path = "all/collection")
    public Collection<CollectionProj> getBooksCollection() {
        return this.bookService.getBooksCollection();
    }

    @GetMapping(path = "all/{bookId}")
    public BookTransformer getBookDetails(@PathVariable("bookId") Long bookId) {
        return this.bookService.getBookDetails(bookId);
    }

    @GetMapping(path = "all/numBooks")
    public Long getNumbOfBooks(
            @RequestParam(required = false) String searchKey,
            @RequestParam(required = false) String filterDateAdded,
            @RequestParam(required = false) String filterAuthor,
            @RequestParam(required = false) Integer filterFirstPublicationYear,
            @RequestParam(required = false) Integer filterLastPublicationYear,
            @RequestParam(required = false) String filterClassification,
            @RequestParam(required = false) String filterPublisher,
            @RequestParam(required = false) String filterIsbn,
            @RequestParam(required = false) String filterLanguage) {
        return this.bookService.getNumberOfBooks(searchKey, filterDateAdded, filterAuthor,
                filterFirstPublicationYear, filterLastPublicationYear, filterClassification, filterPublisher,
                filterIsbn, filterLanguage);
    }

    @GetMapping(path = "all/copies/{bookId}")
    public Collection<BookCopyProj> getCopies(@PathVariable("bookId") Long bookId) {
        return this.bookService.getCopies(bookId);
    }

    @GetMapping(path = "all/pagination")
    public Collection<BookTransformer> getBooksDetailsPaginationSortFilter(
            @RequestParam(required = true) int pageNum,
            @RequestParam(required = true) int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String searchKey,
            @RequestParam(required = false) String filterDateAdded,
            @RequestParam(required = false) String filterAuthor,
            @RequestParam(required = false) Integer filterFirstPublicationYear,
            @RequestParam(required = false) Integer filterLastPublicationYear,
            @RequestParam(required = false) String filterClassification,
            @RequestParam(required = false) String filterPublisher,
            @RequestParam(required = false) String filterIsbn,
            @RequestParam(required = false) String filterLanguage ) {
        return this.bookService.getBooksDetailsPagination(pageNum, pageSize, sortBy, searchKey,
                filterDateAdded, filterAuthor, filterFirstPublicationYear, filterLastPublicationYear, filterClassification,
                filterPublisher, filterIsbn, filterLanguage);
    }

    @PostMapping(path = "admin")
    public Long addBook(@RequestBody BookDetailsDTO bookDetailsDTO) {
        return this.bookService.addNewBook(bookDetailsDTO);
    }

    @PutMapping(path = "admin/{bookId}/authors")
    public void updateBookAuthor(@PathVariable("bookId") Long bookId, @RequestBody List<Author> authors) {
        this.bookService.updateBookAuthor(bookId, authors);
    }

    // TODO: get also the quantity (?) and set the numAvailable when updating book copies
    @PutMapping(path = "admin/{bookId}/copies")
    public void updateBookCopies(@PathVariable("bookId") Long bookId, @RequestBody List<BookCopy> bookCopies) {
        this.bookService.updateBookCopies(bookId, bookCopies);
    }

    @PutMapping(path = "admin/{bookId}")
    public void updateBook(
            @PathVariable("bookId") Long bookId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String edition,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String summary,
            @RequestParam(required = false) String publishedDate,
            @RequestParam(required = false) String imageName,

            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String categoryName,

            @RequestParam(required = false) String shelfName,

            @RequestParam(required = false) String publisherName,
            @RequestParam(required = false) String publisherAddress,

            @RequestParam(required = false) String copyrightName,
            @RequestParam(required = false) String copyrightYear
    ) {
        HashMap<String, String> attrs = new HashMap<>();

        // standard
        if (title != null)
            attrs.put("title", title);
        if (isbn != null)
            attrs.put("isbn", isbn);
        if (language != null)
            attrs.put("language", language);
        if (summary != null)
            attrs.put("summary", summary);
        if (edition != null)
            attrs.put("edition", edition);
        if (publishedDate != null)
            attrs.put("publishedDate", publishedDate);
        if (imageName != null)
            attrs.put("image", imageName);

        // copyright
        if (categoryId != null)
            attrs.put("categoryId", categoryId);
        if (categoryName != null)
            attrs.put("categoryName", categoryName);

        // publisher
        if (publisherName != null)
            attrs.put("publisherName", publisherName);
        if (publisherAddress != null)
            attrs.put("publisherAddress", publisherAddress);

        // copyright
        if (copyrightName != null)
            attrs.put("copyrightName", copyrightName);
        if (copyrightYear != null)
            attrs.put("copyrightYear", copyrightYear);

        // shelf
        if (shelfName != null)
            attrs.put("shelfName", shelfName);

        this.bookService.updateBook(bookId, attrs);
    }

    @PostMapping("admin/borrow")
    public void newBorrow(@RequestBody BorrowDTO borrowDTO) {
        this.borrowService.addBorrow(borrowDTO);
    }

    @GetMapping("all/borrow/{borrowId}")
    public BorrowProj findBorrowById(@PathVariable("borrowId") Long id ){
        return this.borrowService.findBorrowById(id);
    }

    @GetMapping("all/borrow")
    public Collection<BorrowProj> findAllBorrow() {
        return this.borrowService.findAllBorrow();
    }

    @DeleteMapping("admin/borrow/{borrowId}")
    public void returnBorrow(@PathVariable("borrowId") Long id) {
        this.borrowService.returnBorrow(id);
    }

    @GetMapping("all/return")
    public Collection<BorrowProj> findAllReturn() {
        return this.borrowService.findAllReturn();
    }

    @PostMapping("admin/reservation")
    public void newReserve(@RequestBody ReservationDTO reservationDTO) {
        this.reservationService.addReservation(reservationDTO);
    }

    @GetMapping("all/reservation/{reservationId}")
    public ReservationProj findAllReservationById(@PathVariable("reservationId") Long id ){
        return this.reservationService.findReservationById(id);
    }

    @GetMapping("all/reservation")
    public Collection<ReservationProj> findAllReservations() {
        return this.reservationService.findAllReservation();
    }

    @DeleteMapping("admin/reservation/{reservationId}")
    public void deleteReservation(@PathVariable("reservationId") Long id) {
        this.reservationService.deleteReservation(id);
    }
}
