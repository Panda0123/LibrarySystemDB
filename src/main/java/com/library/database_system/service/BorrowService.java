package com.library.database_system.service;

import com.library.database_system.domain.BookCopy;
import com.library.database_system.domain.Borrow;
import com.library.database_system.domain.User;
import com.library.database_system.dtos.BorrowDTO;
import com.library.database_system.dtos.UserDTO;
import com.library.database_system.projections.BorrowProj;
import com.library.database_system.projections.UserProj;
import com.library.database_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;
    private final SectionRepository sectionRepository;
    private final GradeLevelRepository gradeLevelRepository;
    private final UserService userService;

    @Autowired
    public BorrowService(BorrowRepository borrowRepository, BookCopyRepository bookCopyRepository, UserRepository userRepository, SectionRepository sectionRepository, GradeLevelRepository gradeLevelRepository, UserService userService) {
        this.borrowRepository = borrowRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
        this.sectionRepository = sectionRepository;
        this.gradeLevelRepository = gradeLevelRepository;
        this.userService = userService;
    }

    public List<Borrow> getBorrow() {
        return this.borrowRepository.findAll();
    }

    public void addBorrow(Borrow borrow) {
        // TODO: performs some check (if user is currently borrowing something)
        // if similar book and title then don't
        Optional<Borrow> borrowOptional = this.borrowRepository.findById(borrow.getId());
        if(borrowOptional.isPresent())
            throw new IllegalStateException("Borrow already exist");
        this.borrowRepository.save(borrow);
    }

    public void addBorrow(BorrowDTO borrowDTO) {
        // get book copy
        BookCopy bkCpy = this.bookCopyRepository.findById(borrowDTO.getBkCpyId()).orElseThrow(
                () -> { throw new IllegalStateException(String.format("BookCopyId: %d does not exist", borrowDTO.getBkCpyId()));});
        // get user
        Optional<User> userOpt = this.userRepository.findById(borrowDTO.getUserDTO().getId());
        UserDTO userDTO = borrowDTO.getUserDTO();
        User user;
        Borrow newBorrow = null;
        if (userOpt.isPresent()){
            user = userOpt.get();
            this.userService.updateUser(user, userDTO);
        } else {
            user = new User();
            // TODO: check if user with the same full name already exist
            // this.userRepository.findByFNameAndMNameAndLName();
            user.setId(userDTO.getId());
            user.setfName(userDTO.getfName());
            user.setmName(userDTO.getmName());
            user.setlName(userDTO.getlName());
            user.setUserType(userDTO.getType());
            user.setAddress(userDTO.getAddress());
            this.userService.updateUserSection(user, userDTO);
            this.userRepository.save(user);
        }
        newBorrow = new Borrow(bkCpy, user, borrowDTO.getIssueDate(), borrowDTO.getDueDate());
        this.borrowRepository.save(newBorrow);
        setUserBorrowed(user, newBorrow);
        setBookCopyBorrower(bkCpy, newBorrow);
        this.borrowRepository.save(newBorrow);
    }

    @Transactional
    private void setUserBorrowed(User user, Borrow borrow){
         user.getBorrowed().add(borrow);
         borrow.setUser(user);
    }

    @Transactional
    private void setBookCopyBorrower(BookCopy bookCopy, Borrow borrow){
        bookCopy.setStatus("Borrowed");
        bookCopy.setBorrower(borrow);
        borrow.setBookCopy(bookCopy);
    }

    @Transactional
    public void deleteBorrow(Long id) {
        Borrow borrow = this.borrowRepository.findById(id).orElseThrow(
                () -> {throw new IllegalStateException(String.format("BorrowID: %d does not exist", id));} );
        borrow.getUser().getBorrowed().remove(borrow);
        borrow.getBookCopy().setBorrower(null);
        borrow.getBookCopy().setStatus("Available");
        this.borrowRepository.deleteById(id);
    }

    @Transactional
    public void updateBorrow(Long id, HashMap<String, String> attrs) {
        Borrow borrow = this.borrowRepository.findById(id).orElseThrow(
                () -> { throw new IllegalStateException("BorrowID:" + id + " does not exist");} );
        for( String key: attrs.keySet()) {
            String value = attrs.get(key);
            LocalDate parseValLocalDate;
            BookCopy parseValBook;
            User parseValUser;
            if (value != null) {
                switch (key) {
                    case "bookCopy":
                        parseValBook = this.bookCopyRepository.findById(Long.parseLong(value)).orElseThrow(
                                () -> { throw new IllegalStateException("BookID:" + value + " doest not exist");} );
                        BookCopy prevBook = borrow.getBookCopy();
                        if (prevBook != null)
                            prevBook.setBorrower(null); // remove borrow from prev book
                        borrow.setBookCopy(parseValBook);
                        parseValBook.setBorrower(borrow);
                        break;
                    case "user":
                        parseValUser = this.userRepository.findById(value).orElseThrow(
                                () -> { throw new IllegalStateException("UserID:" + value + " doest not exist");} );
                        User prevUser = borrow.getUser();
                        if (prevUser != null)
                            prevUser.getBorrowed().remove(borrow);
                        borrow.setUser(parseValUser);
                        parseValUser.getBorrowed().add(borrow);
                        break;
                    case "borrowedDate":
                        parseValLocalDate = LocalDate.parse(value);
                        if (!Objects.equals(parseValLocalDate, borrow.getBorrowedDate())) {
                            borrow.setBorrowedDate(parseValLocalDate);
                        }
                    case "dueDate":
                        parseValLocalDate = LocalDate.parse(value);
                        if (!Objects.equals(parseValLocalDate, borrow.getDueDate())) {
                            borrow.setDueDate(parseValLocalDate);
                        }
                    case "returnedDate":
                        parseValLocalDate = LocalDate.parse(value);
                        if (!Objects.equals(parseValLocalDate, borrow.getReturnedDate())) {
                            borrow.setReturnedDate(parseValLocalDate);
                        }
                }
            }
        }
    }

    public BorrowProj findBorrowById(Long id) {
        return this.borrowRepository.findBorrowById(id).orElseThrow(
                () -> {throw new IllegalStateException(String.format("BorrowID: %d does not exist", id));}
        );
    }

    public Collection<BorrowProj> findAllBorrow() {
        return this.borrowRepository.findAllBorrow();
    }
}