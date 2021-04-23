package com.library.database_system;

import com.library.database_system.domain.Book;
import com.library.database_system.domain.BookCopy;
import com.library.database_system.domain.Borrow;
import com.library.database_system.domain.User;
import com.library.database_system.dtos.AuthorDTO;
import com.library.database_system.dtos.BookCopyDTO;
import com.library.database_system.dtos.BookDetailsDTO;
import com.library.database_system.repository.BookCopyRepository;
import com.library.database_system.repository.BorrowRepository;
import com.library.database_system.repository.UserRepository;
import com.library.database_system.service.BookService;
import com.library.database_system.service.BorrowService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseSystemApplicationTest {
    @Autowired
    BookService underTest;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BorrowService borrowService;

    @Autowired
    BorrowRepository borrowRepository;

    @Autowired
    BookCopyRepository bookCopyRepository;

    // dependency for other test
    String userId = "1234567891011";
    Long bookTestId;

    @Test
    @Order(1)
    public void shouldAddNewBookAndRetrieveBook() {
        // given
        String title = "TestBook";
        LocalDate publishedDate = LocalDate.now();
        String language = "English";
        String isbn = "1234567891011";
        Integer quantity = 1;
        String imageName = "empty";
        String publisherName = "Pearson";
        String publisherAddress = "VDO";
        Long categoryId = 1L;
        Short copyrightYear = 2017;
        Long shelfId = 1L;
        Set<AuthorDTO> authors = new HashSet<>();
        authors.add(new AuthorDTO(-1L, "Russel", "M.", "Harry"));
        Set<BookCopyDTO> copies = new HashSet<>();
        copies.add(new BookCopyDTO(-1L, 1, "Available"));

        BookDetailsDTO bkDetailsDTO = new BookDetailsDTO();
        bkDetailsDTO.setTitle(title);
        bkDetailsDTO.setPublishedDate(publishedDate);
        bkDetailsDTO.setLanguage(language);
        bkDetailsDTO.setIsbn(isbn);
        bkDetailsDTO.setQuantity(quantity);
        bkDetailsDTO.setImageName(imageName);

        bkDetailsDTO.setPublisherName(publisherName);
        bkDetailsDTO.setPublisherAddress(publisherAddress);
        bkDetailsDTO.setCategoryId(categoryId);
        bkDetailsDTO.setCopyrightYear(copyrightYear);
        bkDetailsDTO.setShelfId(shelfId);
        bkDetailsDTO.setAuthors(authors);
        bkDetailsDTO.setCopies(copies);

        // when
        this.bookTestId = underTest.addNewBook(bkDetailsDTO);

        // then
        Book bk = underTest.findBookByID(this.bookTestId);
        assertThat(underTest.doesIsbnExist(isbn)).isTrue();
        assertThat(bk.getISBN()).isEqualTo(isbn);
        assertThat(bk.getTitle()).isEqualTo(title);
        assertThat(bk.getLanguage()).isEqualTo(language);
    }

    @Test
    @Order(2)
    void shouldUpdateBook() {
        // given
        Book bk = underTest.findBookByID(this.bookTestId);
        assertThat(bk.getTitle()).isEqualTo("TestBook");

        // when
        String newTitle = "New Title TestBook";
        HashMap<String, String> updateAttrs = new HashMap<>();
        updateAttrs.put("title", newTitle);
        underTest.updateBook(this.bookTestId, updateAttrs);

        // then
        bk = underTest.findBookByID(this.bookTestId);
        assertThat(bk.getTitle()).isEqualTo(newTitle);
    }

    @Test
    @Order(3)
    void shouldDeleteBook() {
        // given
        Book bk = underTest.findBookByID(this.bookTestId);
        Boolean isbnExist = underTest.doesIsbnExist(bk.getISBN());
        assertThat(isbnExist).isTrue();

        // when
        underTest.deleteBook(this.bookTestId);

        // then
        isbnExist = underTest.doesIsbnExist(bk.getISBN());
        assertThat(isbnExist).isFalse();
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(
                () -> {underTest.findBookByID(this.bookTestId);})
                .withMessageContaining(String.format("BookID: %d does not exist", this.bookTestId));
    }

    @Test
    @Order(4)
    public void shouldAddUser() {
        // given
        String fName = "Robert";
        String mName = "Maks";
        String lName = "Raul";
        String address = "CDO";
        String userType  = "Student";
        User sampleUser = new User(userId, fName, mName, lName, address, userType);
        assertThat(userRepository.existsById(userId)).isFalse();

        // when
        userRepository.save(sampleUser);

        // then
        assertThat(userRepository.existsById(userId)).isTrue();
    }

    @Test
    @Order(5)
    public void shouldAddBorrow() {

        // given
        User sampleUser = userRepository.findById(userId).get();
        BookCopy sampleBookToBorrow = bookCopyRepository.findById(1L).get();
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = LocalDate.now().plusDays(5);
        Borrow newBorrow = new Borrow(sampleBookToBorrow, sampleUser, issueDate, dueDate);

        Long numOfBorrow = borrowRepository.count();


        // when
        borrowService.addBorrow(newBorrow);

        // then
        assertThat(borrowRepository.count()).isEqualTo(numOfBorrow + 1);
    }
}
