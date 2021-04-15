package com.library.database_system.service;

import com.library.database_system.domain.*;
import com.library.database_system.dtos.AuthorDTO;
import com.library.database_system.dtos.BookCopyDTO;
import com.library.database_system.dtos.BookDetailsDTO;
import com.library.database_system.projections.BookCopyProj;
import com.library.database_system.projections.CollectionProj;
import com.library.database_system.repository.*;
import com.library.database_system.resulttransformer.BookTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final PublishingHouseRepository publishingHouseRepository;
    private final CategoryRepository categoryRepository;
    private final ShelfRepository shelfRepository;
    private final CopyrightRepository copyrightRepository;
    private final AuthorRepository authorRepository;
    private final BorrowRepository borrowRepository;
    private final BookCopyRepository bookCopyRepository;

    @Autowired
    public BookService(BookRepository bookRepository, PublishingHouseRepository publishingHouseRepository, CategoryRepository categoryRepository, ShelfRepository shelfRepository, CopyrightRepository copyrightRepository, BookRepository bookRepository1, AuthorRepository authorRepository, BorrowRepository borrowRepository, BookCopyRepository bookCopyRepository) {
        this.bookRepository = bookRepository;
        this.publishingHouseRepository = publishingHouseRepository;
        this.categoryRepository = categoryRepository;
        this.shelfRepository = shelfRepository;
        this.copyrightRepository = copyrightRepository;
        this.authorRepository = authorRepository;
        this.borrowRepository = borrowRepository;
        this.bookCopyRepository = bookCopyRepository;
    }

   public List<Book> getBooks() {
        return this.bookRepository.findAll();
    }

    public Collection<CollectionProj> getBooksCollection() {
        return this.bookRepository.getBooksCollection();
    }

    public Book getBookById(Long id) {
        Book bk = this.bookRepository.findById(id).orElseThrow(
                () -> { return new IllegalStateException("BookID:" + id + " does not exist");});
        return bk;
    }

    public Collection<BookTransformer> getBooksDetails() {
        return this.bookRepository.getBooksDetails();
    }

    public BookTransformer getBookDetails(Long bookId) {
        return this.bookRepository.getBookDetails(bookId);
    }

    public Collection<BookTransformer> getBooksDetailsPagination(
            int pageNum, int pageSize, String sortBy, String searchKey,
            String filterDateAdded, String filterAuthor, Integer filterFirstPublicationYear,
            Integer filterLastPublicationYear, String filterClassification,
            String filterPublisher, String filterIsbn, String filterLanguage) {

        return this.bookRepository.getBooksDetailsPagination(
                pageNum, pageSize, sortBy, searchKey, filterDateAdded, filterAuthor,
                filterFirstPublicationYear, filterLastPublicationYear, filterClassification,
                filterPublisher, filterIsbn, filterLanguage);
    }

    public Long getNumberOfBooks(String searchKey) {
        if (searchKey == null)
            return this.bookRepository.getNumberOfBooks();
        else
            return this.bookRepository.getNumberOfBooks(searchKey);
    }


    public void addNewBook(Book book) {
        this.bookRepository.findById(book.getId()).orElseThrow(
                () -> {throw  new IllegalStateException("BookId:" + book.getId() + " already exist");});

        this.bookRepository.findByISBN(book.getISBN()).orElseThrow(
                () -> {throw  new IllegalStateException("ISBN:" + book.getISBN() + " already exist");});

        this.bookRepository.findByTitleEdition(book.getTitle(), book.getEdition()).orElseThrow(
                () -> {throw  new IllegalStateException("Tile:" + book.getTitle() +
                        " Edition:" + book.getEdition()  + " already exist");} );
        this.bookRepository.save(book);
    }


    public Long addNewBook(BookDetailsDTO bkDetailsPOJO)  {
        Book newBk = new Book();

        // regular
        Optional<Book> bkOpt = this.bookRepository.findByISBN(bkDetailsPOJO.getIsbn());  // check if there exist same isbn
        if (bkOpt.isPresent())
            throw new IllegalStateException("Book with ISB:" + bkDetailsPOJO.getIsbn() + " already exist");
        else
            newBk.setISBN(bkDetailsPOJO.getIsbn());
        newBk.setTitle(bkDetailsPOJO.getTitle());
        newBk.setLanguage(bkDetailsPOJO.getLanguage());
        newBk.setSummary(bkDetailsPOJO.getSummary());
        newBk.setEdition(bkDetailsPOJO.getEdition());
        newBk.setDateAdded(LocalDate.now());
        newBk.setTimeAdded(LocalTime.now());


        // copies
        newBk.setQuantity(bkDetailsPOJO.getQuantity());
        newBk.setNumAvailable(bkDetailsPOJO.getQuantity());
        Collection<BookCopy> bookCopies = new ArrayList<>();
        for (BookCopyDTO bkCpyDTO : bkDetailsPOJO.getCopies()) {
            bookCopies.add(new BookCopy(bkCpyDTO.getCopy_num(), bkCpyDTO.getStatus()));
        }
        this.bookCopyRepository.saveAll(bookCopies);

        // published date
        if (bkDetailsPOJO.getPublishedDate() != null)
            newBk.setPublished(bkDetailsPOJO.getPublishedDate());

        // category
        Category category = this.categoryRepository.findById(bkDetailsPOJO.getCategoryId()).orElseThrow(
                () -> {throw new IllegalStateException("CategoryID:" + bkDetailsPOJO.getCategoryId() + " does not exist");} );

        // publisher
        PublishingHouse publisher = this.getPublisherWithNameAndAddress(newBk, bkDetailsPOJO.getPublisherName(), bkDetailsPOJO.getPublisherAddress());

        // copyright
        Copyright copyright = this.getCopyrightWithNameAndYear(newBk, bkDetailsPOJO.getCopyrightName(), bkDetailsPOJO.getCopyrightYear());

        // shelf
        Shelf shelf =  getShelfWithName(newBk, bkDetailsPOJO.getShelfName());


        // authors
        this.bookRepository.save(newBk);

        if (!bkDetailsPOJO.getAuthors().isEmpty()) {
            for (AuthorDTO authorDTO : bkDetailsPOJO.getAuthors()) {
                Optional<Author> authorOpt = this.authorRepository.findAuthorByFullName(authorDTO.getf_name(), authorDTO.getm_name(), authorDTO.getl_name());
                Author author;

                if (authorOpt.isPresent()) {
                    // transactional
                    author = authorOpt.get();
                } else {
                    // new
                    author = new Author();
                    author.setf_name(authorDTO.getf_name());
                    author.setm_name(authorDTO.getm_name());
                    author.setl_name(authorDTO.getl_name());
                    this.authorRepository.save(author);
                }
                author.getBooksAuthored().add(newBk);
                newBk.getAuthors().add(author);
            }
        } else {
            Optional<Author> authorOpt = this.authorRepository.findAuthorEmpty();
            Author author;
            if (authorOpt.isPresent()) {
                author = authorOpt.get();
            } else{
                author = new Author(null, null, null);
                this.authorRepository.save(author);
            }
            author.getBooksAuthored().add(newBk);
            newBk.getAuthors().add(author);
            this.authorRepository.save(author);
        }

        if(publisher.getId() == null)
            this.publishingHouseRepository.save(publisher);

        if(copyright.getId() == null)
            this.copyrightRepository.save(copyright);

        if(shelf.getId() == null)
            this.shelfRepository.save(shelf);

        for (BookCopy bkCpy :bookCopies )
            bkCpy.setBook(newBk);
        newBk.setBookCopy(new HashSet<BookCopy>(bookCopies));

        String img = bkDetailsPOJO.getImageName();
        if(!Objects.equals(img, "empty")) {
            String ext = img.substring(img.lastIndexOf("."), img.length());
            newBk.setImageName(newBk.getId().toString() + ext);
        } else {
            newBk.setImageName(img);
        }

        updateShelf(newBk, shelf);
        updateCategory(newBk, category);
        updatePublisher(newBk, publisher);
        updateCopyright(newBk, copyright);
        this.bookRepository.save(newBk);
        return newBk.getId();
    }

    public void deleteBook(Long bookId){
        boolean doesExist = this.bookRepository.existsById(bookId);
        if (doesExist) {
            this.bookRepository.deleteById(bookId);
        } else {
            throw new IllegalStateException("BookId:" + bookId + " already exist");
        }
    }

    @Transactional
    public void updateBook(Long bookId, HashMap<String, String> attrs) {
        Book bk = this.bookRepository.findById(bookId).orElseThrow(
                () -> {throw new IllegalStateException("BookID:" + bookId + " does not exist");} );
        boolean publisherDone = false;
        boolean copyrightDone = false;
        PublishingHouse publisher;
        Copyright copyright;
        for (String key: attrs.keySet()) {
            String value = attrs.get(key);
            int parseValInt;
            LocalDate parseValLocalDate;
            switch (key){
                case "title":
                    if (value != null && !Objects.equals(bk.getTitle(), value))
                        bk.setTitle(value);
                    break;
                case "isbn":
                    if (value != null && !Objects.equals(bk.getISBN(), value))
                        bk.setISBN(value);
                    break;
                case "language":
                    if (!Objects.equals(bk.getLanguage(), value))
                        bk.setLanguage(value);
                    break;
                case "summary":
                    if (!Objects.equals(bk.getSummary(), value))
                        bk.setSummary(value);
                    break;
                case "image":
                    if (!Objects.equals(bk.getImageName(), value))
                        bk.setImageName(value);
                    break;
                case "edition":
                    parseValInt = value != null && value != "" ? Integer.parseInt(value) : 1;
                    if (0 < parseValInt && bk.getEdition() != parseValInt)
                        bk.setEdition(parseValInt);
                    break;
                case "dateAdded":
                    parseValLocalDate = value != null && value != "" ? LocalDate.parse(value) : null;
                    bk.setDateAdded(parseValLocalDate);
                    break;
                case "publishedDate":
                    parseValLocalDate = value != null && value != "" ? LocalDate.parse(value) : null;
                    bk.setPublished(parseValLocalDate);
                    break;
                case "category":
                    Category cat = this.categoryRepository.findById(Long.parseLong(value)).orElseThrow(
                            () -> {throw new IllegalStateException("CategoryID:"+ value + " does not exist");});
                    this.updateCategory(bk, cat);
                    break;
                case "shelfName":
                    Shelf shelf = this.getShelfWithName(bk, value);
                    if (shelf.getId() == null || !this.shelfRepository.existsById(shelf.getId()))
                        this.shelfRepository.save(shelf);
                    this.updateShelf(bk, shelf);
                    break;
                case "publisherAddress":
                case "publisherName":
                    if (!publisherDone) {
                        String publisherAddr = attrs.containsKey("publisherAddress") ?  attrs.get("publisherAddress") : null;
                        String publisherName = attrs.containsKey("publisherName") ?  attrs.get("publisherName") : null;
                        publisher = this.getPublisherWithNameAndAddress(bk, publisherName, publisherAddr);
                        if (publisher.getId() == null || !this.publishingHouseRepository.existsById(publisher.getId()))
                            this.publishingHouseRepository.save(publisher);
                        this.updatePublisher(bk, publisher);
                        publisherDone = true;
                    }
                    break;
                case "copyrightYear":
                case "copyrightName":
                    if (!copyrightDone) {
                        String copyrightName = attrs.containsKey("copyrightName") ?  attrs.get("copyrightName") : null;
                        int copyrightYear = 0;
                        if (attrs.containsKey("copyrightYear")) {
                            if (attrs.get("copyrightYear") != "")
                                copyrightYear = Integer.parseInt(attrs.get("copyrightYear"));
                            else
                                copyrightYear =  0;
                        } else {
                            copyrightYear =  -1;
                        }
                        copyright = this.getCopyrightWithNameAndYear(bk, copyrightName, copyrightYear);
                        if (copyright.getId() == null || !this.copyrightRepository.existsById(copyright.getId()))
                            this.copyrightRepository.save(copyright);
                        this.updateCopyright(bk, copyright);
                        copyrightDone = true;
                    }
                    break;
            }
        }
    }

    @Transactional
    public void updateBookAuthor(Long bookId, List<Author> authors) {
        Book bk = this.bookRepository.findById(bookId).orElseThrow(
                () -> {throw new IllegalStateException("BookID:"+ bookId + " does not exist");} );
        Set<Author> authorSet = new HashSet<>();
        Set<Author> prevAuthorSet = bk.getAuthors();
        Author author = null;
        for (Author authorDTO: authors) {
            if (authorDTO.getId() == -1) {
                Optional<Author> authorOpt = this.authorRepository.findAuthorByFullName(authorDTO.getf_name(), authorDTO.getm_name(), authorDTO.getl_name());
                if (authorOpt.isPresent()) {
                    author = authorOpt.get();
                } else {
                    author = new Author();
                    author.setf_name(authorDTO.getf_name());
                    author.setm_name(authorDTO.getm_name());
                    author.setl_name(authorDTO.getl_name());
                    this.authorRepository.save(author);
                }
            } else {
                author = this.authorRepository.findById(authorDTO.getId()).orElseThrow(
                        () -> {throw new IllegalStateException("AuthorID:"+ authorDTO.getId() + " does not exist");} );
            }
            authorSet.add(author);
        }
        if (prevAuthorSet != null) {
            for( Author prevAuthor: prevAuthorSet) {
                if (!authorSet.contains(prevAuthor))
                    removeAuthorFromBook(bk, prevAuthor);
                else
                    authorSet.remove(prevAuthor);
            }
        }
        for (Author newAuthor : authorSet)
            addAuthorToBook(bk, newAuthor);
    }

    @Transactional
    public void addAuthorToBook(Book bk, Author author) {
        bk.getAuthors().add(author);
        author.getBooksAuthored().add(bk);
    }

    @Transactional
    public void removeAuthorFromBook(Book bk, Author author) {
        bk.getAuthors().remove(author);
        author.getBooksAuthored().remove(bk);
        // an author shouldn't exist if he/she doesn't have any authored book in the database except if he is the null all
        if(author.getBooksAuthored().isEmpty() &&
                author.getf_name() != null &&
                author.getm_name() != null &&
                author.getl_name() != null)
            this.authorRepository.delete(author);
    }

//    @Transactional
//    public void updateCopies(Book bk, int quantity) {
//        int size = bk.getBookCopy().size();
//        int difference = quantity - size;
//
//        if (difference > 0) {
//            // add new copies
//            BookCopy bookCopy;
//            for (int i=0; i < difference; i++) {
//                bookCopy = new BookCopy(size + i + 1, "Available");
//                this.bookCopyRepository.save(bookCopy);
//                bk.getBookCopy().add(bookCopy);
//                bookCopy.setBook(bk);
//                //this.bookCopyRepository.save(bookCopy);
//            }
//        } else {
//            // remove copies
//            for (int i = 0; i < Math.abs(difference); i++) {
//                int finalI = i;
//                bk.getBookCopy().removeIf(cpy -> {
//                    if (cpy.getCopy_num() == (size - finalI)) {
//                        this.bookCopyRepository.delete(cpy);
//                        return true;
//                    }
//                    return false;
//            });
//            }
//        }
//    }

    @Transactional
    public void updateBookCopies(Long bookId, List<BookCopy> bookCopies) {
        Book bk = this.bookRepository.findById(bookId).orElseThrow(
                () -> {throw new IllegalStateException("BookID:"+ bookId + " does not exist");} );
        Set<BookCopy> bookCopySet = new HashSet<>();
        ArrayList<BookCopy> copyToRemoveSet = new ArrayList<>();
        Set<BookCopy> prevCopySet = bk.getBookCopy();
        BookCopy bkCpy = null;
        for (BookCopy bookCopy: bookCopies) {
            Optional<BookCopy> bkCpyOpt = prevCopySet.stream().filter(orgCpy -> orgCpy.getCopy_num() == bookCopy.getCopy_num()).findFirst();
            if (bkCpyOpt.isPresent()) {
                bkCpy = bkCpyOpt.get();
                if (bkCpy.getCopy_num() != bookCopy.getCopy_num())
                    bkCpy.setCopy_num(bookCopy.getCopy_num());
                if (!bkCpy.getStatus().equals(bookCopy.getStatus()))
                    bkCpy.setStatus(bookCopy.getStatus());
            } else {
                bkCpy = new BookCopy(bookCopy.getCopy_num(), bookCopy.getStatus());
                this.bookCopyRepository.save(bkCpy);
            }
            bookCopySet.add(bkCpy);
        }
//        prevCopySet.removeIf(cpy -> !bookCopySet.contains(cpy));
        bk.setQuantity(bookCopySet.size());
        int newNumAvailable = 0;
        if (prevCopySet != null )
            for(BookCopy prevBookCopy: prevCopySet) {
                if (!bookCopySet.contains(prevBookCopy)) {
                    copyToRemoveSet.add(prevBookCopy);
                } else {
                    bookCopySet.remove(prevBookCopy);
                    newNumAvailable = prevBookCopy.getStatus().equals("Available") ? newNumAvailable + 1 : newNumAvailable;
                }
            }
        // TODO: find better solution for this
        for (int i = 0; i < copyToRemoveSet.size(); i++) {
            bkCpy = copyToRemoveSet.get(i);
            bk.getBookCopy().remove(bkCpy);
            this.bookCopyRepository.delete(bkCpy);
        }
        for (BookCopy newBookCopy : bookCopySet) {
            bk.getBookCopy().add(newBookCopy);
            newBookCopy.setBook(bk);
            newNumAvailable = newBookCopy.getStatus().equals("Available") ? newNumAvailable + 1 : newNumAvailable;
        }
        bk.setNumAvailable(newNumAvailable);
    }

//    @Transactional
//    public void addBorrowToBook(Long bookId, Long borrowId) {
//        Book bk = this.bookRepository.findById(bookId).orElseThrow(
//                () -> {throw new IllegalStateException("BookID:"+ bookId + " does not exist");} );
//
//        Borrow borrow = this.borrowRepository.findById(borrowId).orElseThrow(
//                () -> {throw new IllegalStateException("BorrowID:"+ borrowId + " does not exist");} );
//
//        if (!bk.getBorrower().contains(borrow))
//            bk.getBorrower().add(borrow);
//
//        if (!Objects.equals(borrow.getBook(), bk))
//            borrow.setBook(bk);
//    }

//    @Transactional
//    public void removeBorrowFromBook(Long bookId, Long borrowId) {
//        Book bk = this.bookRepository.findById(bookId).orElseThrow(
//                () -> {throw new IllegalStateException("BookID:"+ bookId + " does not exist");} );
//        Borrow borrow = this.borrowRepository.findById(borrowId).orElseThrow(
//                () -> {throw new IllegalStateException("BorrowID:"+ borrowId + " does not exist");} );
//
//        if (bk.getBorrower().contains(borrow)) {
//            bk.getBorrower().remove(borrow);
//        }
//    }

    @Transactional
    private void updateCategory(Book bk, Category newCategory){
        Category prevCategory = bk.getCategory();
        if (prevCategory != null ) {
            if (!Objects.equals(prevCategory, newCategory)) {
                prevCategory.getBooks().remove(bk);
                bk.setCategory(newCategory);
            }
        } else {
            newCategory.getBooks().add(bk);
            bk.setCategory(newCategory);
        }

    }


    @Transactional
    private void updatePublisher(Book bk, PublishingHouse newPublisher){
        PublishingHouse prevPublisher = bk.getPublisher();
        if (prevPublisher != null ) {
            if (!Objects.equals(prevPublisher, newPublisher)) {
                prevPublisher.getBooks().remove(bk);
                if (prevPublisher.getBooks().isEmpty() &&
                        prevPublisher.getName() != null &&
                        prevPublisher.getAddress() != null)
                    this.publishingHouseRepository.delete(prevPublisher);
            }
            bk.setPublisher(newPublisher);
            newPublisher.getBooks().add(bk);
        } else {
            bk.setPublisher(newPublisher);
            newPublisher.getBooks().add(bk);
        }

    }

    @Transactional
    private void updateCopyright(Book bk, Copyright newCopyright){
        Copyright prevCopyright = bk.getCopyright();
        if (prevCopyright != null) {
            if (!Objects.equals(prevCopyright, newCopyright)){
                prevCopyright.getBooks().remove(bk);
                if (prevCopyright.getBooks().isEmpty() && prevCopyright.getName() != null && prevCopyright.getYear() != 0)
                        this.copyrightRepository.delete(prevCopyright);
                bk.setCopyright(newCopyright);
                newCopyright.getBooks().add(bk);
            }
        } else {
            bk.setCopyright(newCopyright);
            newCopyright.getBooks().add(bk);
        }

    }

    @Transactional
    private void updateShelf(Book bk, Shelf newShelf){
        Shelf prevShelf = bk.getShelf();
        if (prevShelf != null) {
            if (!Objects.equals(prevShelf, newShelf)) {
                prevShelf.getBooks().remove(bk);
                if (prevShelf.getBooks().isEmpty() && prevShelf.getName() != null)
                    this.shelfRepository.delete(prevShelf);
                bk.setShelf(newShelf);
                newShelf.getBooks().add(bk);
            }
        } else {
                bk.setShelf(newShelf);
                newShelf.getBooks().add(bk);
            }
    }

    private PublishingHouse getPublisherWithNameAndAddress(Book bk, String publisherName, String publisherAddress) {
        PublishingHouse publisher = null;
        Optional<PublishingHouse> pubOpt = null;

        if (publisherName != null && publisherAddress != null) {
            pubOpt = this.publishingHouseRepository.findByNameAndAddress(publisherName, publisherAddress);
        } else if (publisherName != null && publisherAddress == null) {
            publisherAddress = bk.getPublisher() != null ? bk.getPublisher().getAddress() : null;
            pubOpt = this.publishingHouseRepository.findByNameAndAddress(publisherName, publisherAddress);
        } else if (publisherName == null && publisherAddress != null) {
            publisherName = bk.getPublisher() != null ? bk.getPublisher().getName() : null;
            pubOpt = this.publishingHouseRepository.findByNameAndAddress(publisherName, publisherAddress);
        } else {
            pubOpt = this.publishingHouseRepository.findByNameAndAddress(null, null);
        }

        if (pubOpt != null) {
            if (pubOpt.isPresent()) {
                publisher = pubOpt.get();
            } else {
                // new
                publisher = new PublishingHouse();
                publisher.setName(publisherName);
                publisher.setAddress(publisherAddress);
            }
        }
        return publisher;
    }

    private Copyright getCopyrightWithNameAndYear(Book bk, String copyrightName, int copyrightYear) {
        Copyright copyright = null;
        Optional<Copyright> copyrightOpt = null;

        if (copyrightName != null && copyrightYear != -1) {
            copyrightOpt = this.copyrightRepository.findByNameAndYear(copyrightName, copyrightYear);
        } else if (copyrightName != null && copyrightYear == -1) {
            copyrightYear = bk.getCopyright() != null ? bk.getCopyright().getYear(): null;
            copyrightOpt = this.copyrightRepository.findByNameAndYear(copyrightName, copyrightYear);
        } else if (copyrightName == null && copyrightYear != -1) {
            copyrightName = bk.getCopyright() != null ? bk.getCopyright().getName() : null;
            copyrightOpt = this.copyrightRepository.findByNameAndYear(copyrightName, copyrightYear);
        } else {
            copyrightOpt = this.copyrightRepository.findByNameAndYear(null, 0);
        }

        if (copyrightOpt != null) {
            if (copyrightOpt.isPresent()) {
                copyright = copyrightOpt.get();
            } else {
                // new
                copyright = new Copyright();
                copyright.setName(copyrightName);
                copyright.setYear(copyrightYear);
            }
        }
        return copyright;
    }

    private Shelf getShelfWithName(Book bk, String shelfName) {
        Shelf prevShelf = bk.getShelf();
        Shelf shelf = null;

        Optional<Shelf> shelfOpt = this.shelfRepository.findByName(shelfName);

        if (shelfOpt.isPresent())
            shelf = shelfOpt.get();
         else {
             shelf = new Shelf();
             shelf.setName(shelfName);
        }
         return shelf;
    }


    public Collection<BookCopyProj> getCopies(Long bookId) {
        Book bk = this.bookRepository.findById(bookId).orElseThrow(
                () -> {throw new IllegalStateException("BookID:"+ bookId + " does not exist");} );
        return this.bookCopyRepository.getCopyProj(bookId);
        // return bk.getBookCopy();
    }

}