package com.library.database_system;

import com.library.database_system.repository.BookRepository;
import com.library.database_system.repository.CategoryRepository;
import com.library.database_system.service.BookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SanityCheckTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BookService bookService;

    @Test
    public void contextLoad() {

    }

    @Test
    public void isRepositoryLoaded() {
        assertThat(bookRepository).isNotNull();
    }

    @Test
    public void isServiceLoaded() {
        assertThat(bookService).isNotNull();
    }

    @Test
    public void isCategoryRepositoryLoaded() {
        assertThat(categoryRepository).isNotNull();
    }

    @Test
    public void shouldNotBeEmpty() {
        assertThat(categoryRepository.findAll().isEmpty()).isFalse();
    }

    @Test
    public void shouldBe21Categories() {
        assertThat(categoryRepository.findAll().size()).isEqualTo(21);
    }
}
