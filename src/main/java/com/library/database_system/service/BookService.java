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
    private final BookCopyRepository bookCopyRepository;
    private final ImageService imageService;

    @Autowired
    public BookService(BookRepository bookRepository, PublishingHouseRepository publishingHouseRepository, CategoryRepository categoryRepository, ShelfRepository shelfRepository, CopyrightRepository copyrightRepository, AuthorRepository authorRepository, BookCopyRepository bookCopyRepository, ImageService imageService) {
        this.bookRepository = bookRepository;
        this.publishingHouseRepository = publishingHouseRepository;
        this.categoryRepository = categoryRepository;
        this.shelfRepository = shelfRepository;
        this.copyrightRepository = copyrightRepository;
        this.authorRepository = authorRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.imageService = imageService;
    }

    public Collection<CollectionProj> getBooksCollection() {
        return this.bookRepository.getBooksCollection();
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

    public Long getNumberOfBooks(String searchKey, String filterDateAdded, String filterAuthor,
                                 Integer filterFirstPublicationYear, Integer filterLastPublicationYear, String filterClassification, String filterPublisher,
                                 String filterIsbn, String filterLanguage) {
        return this.bookRepository.getNumberOfBooks(searchKey, filterDateAdded, filterAuthor,
                filterFirstPublicationYear, filterLastPublicationYear, filterClassification, filterPublisher,
                filterIsbn, filterLanguage);
    }


    @Transactional
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

        this.bookRepository.save(newBk);

        // authors
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

    @Transactional
    public void deleteBook(Long bookId){
        Book bk = this.bookRepository.findById(bookId).orElseThrow(
                () -> { throw new IllegalStateException("BookId:" + bookId + " already exist");} );
        Set<Author> authorsTemp = new HashSet<>();

        for (Author auth: bk.getAuthors())
            authorsTemp.add(auth);

        for (Author author: authorsTemp)
            removeAuthorFromBook(bk, author);

        removeCopyrightFromBook(bk, bk.getCopyright());
        removePublisherFromBook(bk, bk.getPublisher());
        removeShelfFromBook(bk, bk.getShelf());
        if (!bk.getImageName().equals("empty"))
            this.imageService.removeImage(bk.getImageName());
        this.bookRepository.delete(bk);
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
                    if (!Objects.equals(bk.getISBN(), value))
                        if (value != "")
                            bk.setISBN(value);
                        else
                            bk.setISBN(null);
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
                    if (!Objects.equals(bk.getEdition(), value))
                        bk.setEdition(value);
                    break;
                case "dateAdded":
                    parseValLocalDate = value != null && value != "" ? LocalDate.parse(value) : null;
                    bk.setDateAdded(parseValLocalDate);
                    break;
                case "publishedDate":
                    parseValLocalDate = value != null && value != "" ? LocalDate.parse(value) : null;
                    bk.setPublished(parseValLocalDate);
                    break;
                case "categoryId":
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
                    if (publisherDone)
                        break;
                    String publisherAddr = attrs.containsKey("publisherAddress") ?  attrs.get("publisherAddress") : null;
                    String publisherName = attrs.containsKey("publisherName") ?  attrs.get("publisherName") : null;
                    publisher = this.getPublisherWithNameAndAddress(bk, publisherName, publisherAddr);
                    if (publisher.getId() == null || !this.publishingHouseRepository.existsById(publisher.getId()))
                        this.publishingHouseRepository.save(publisher);
                    this.updatePublisher(bk, publisher);
                    publisherDone = true;
                    break;
                case "copyrightYear":
                case "copyrightName":
                    if (copyrightDone)
                        break;
                    String copyrightName = attrs.containsKey("copyrightName")
                            ?  attrs.get("copyrightName").equals("") ? null : attrs.get("copyrightName")
                            : bk.getCopyright().getName();
                    Short copyrightYear = attrs.containsKey("copyrightYear")
                            ?  attrs.get("copyrightYear").equals("") ? null : Short.parseShort(attrs.get("copyrightYear"))
                            : bk.getCopyright().getYear();
                    copyright = this.getCopyrightWithNameAndYear(bk, copyrightName, copyrightYear);
                    if (copyright.getId() == null || !this.copyrightRepository.existsById(copyright.getId()))
                        this.copyrightRepository.save(copyright);
                    this.updateCopyright(bk, copyright);
                    copyrightDone = true;
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

    @Transactional
    private void updateCategory(Book bk, Category newCategory){
        Category prevCategory = bk.getCategory();
        if (prevCategory != null )
            prevCategory.getBooks().remove(bk);
        newCategory.getBooks().add(bk);
        bk.setCategory(newCategory);
    }


    @Transactional
    private void updatePublisher(Book bk, PublishingHouse newPublisher){
        PublishingHouse prevPublisher = bk.getPublisher();
        if (prevPublisher != null && !Objects.equals(prevPublisher, newPublisher))
            removePublisherFromBook(bk, prevPublisher);
        bk.setPublisher(newPublisher);
        newPublisher.getBooks().add(bk);
    }

    @Transactional
    public void removePublisherFromBook(Book bk, PublishingHouse publisher) {
        bk.setPublisher(null);
        publisher.getBooks().remove(bk);

        if(publisher.getBooks().isEmpty() &&
                publisher.getName() != null &&
                publisher.getAddress() != null)
            this.publishingHouseRepository.delete(publisher);
    }

    @Transactional
    private void updateCopyright(Book bk, Copyright newCopyright){
        Copyright prevCopyright = bk.getCopyright();
        if (prevCopyright != null && !Objects.equals(prevCopyright, newCopyright))
            removeCopyrightFromBook(bk, prevCopyright);
        bk.setCopyright(newCopyright);
        newCopyright.getBooks().add(bk);
    }

    @Transactional
    public void removeCopyrightFromBook(Book bk, Copyright copyright) {
        bk.setCopyright(null);
        copyright.getBooks().remove(bk);
        if(copyright.getBooks().isEmpty() &&
                copyright.getName() != null &&
                copyright.getYear() != 0) // year is set to Integer change this to null
            this.copyrightRepository.delete(copyright);
    }

    @Transactional
    private void updateShelf(Book bk, Shelf newShelf){
        Shelf prevShelf = bk.getShelf();
        if (prevShelf != null && !Objects.equals(prevShelf, newShelf))
            removeShelfFromBook(bk, prevShelf);
        bk.setShelf(newShelf);
        newShelf.getBooks().add(bk);
    }

    @Transactional
    public void removeShelfFromBook(Book bk, Shelf shelf) {
        bk.setShelf(null);
        shelf.getBooks().remove(bk);
        if(shelf.getBooks().isEmpty() && shelf.getName() != null)
            this.shelfRepository.delete(shelf);
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

    private Copyright getCopyrightWithNameAndYear(Book bk, String copyrightName, Short copyrightYear) {
        Optional<Copyright> copyrightOpt = this.copyrightRepository.findByNameAndYear(copyrightName, copyrightYear);
        Copyright copyright = null;
        if (copyrightOpt.isPresent()) {
            copyright = copyrightOpt.get();
        } else {
            // new
            copyright = new Copyright();
            copyright.setName(copyrightName);
            copyright.setYear(copyrightYear);
        }
        return copyright;
    }

    private Shelf getShelfWithName(Book bk, String shelfName) {
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
    }

    public boolean doesIsbnExist(String isbn) {
        Optional<Book> bkOpt = this.bookRepository.findByISBN(isbn);
        return bkOpt.isPresent();
    }

    public Book findBookByID(Long bookTestId) {
        return this.bookRepository.findById(bookTestId).orElseThrow(() ->
        { throw  new IllegalStateException(String.format("BookID: %s does not exist", bookTestId));} );
    }
}