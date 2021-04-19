package com.library.database_system.service;

import com.library.database_system.domain.BookCopy;
import com.library.database_system.domain.Borrow;
import com.library.database_system.domain.Reservation;
import com.library.database_system.domain.User;
import com.library.database_system.dtos.ReservationDTO;
import com.library.database_system.dtos.UserDTO;
import com.library.database_system.projections.ReservationProj;
import com.library.database_system.repository.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;
    private final SectionRepository sectionRepository;
    private final GradeLevelRepository gradeLevelRepository;
    private final UserService userService;

    public ReservationService(ReservationRepository reservationRepository,
                              BookCopyRepository bookCopyRepository,
                              UserRepository userRepository,
                              SectionRepository sectionRepository,
                              GradeLevelRepository gradeLevelRepository, UserService userService) {
        this.reservationRepository = reservationRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
        this.sectionRepository = sectionRepository;
        this.gradeLevelRepository = gradeLevelRepository;
        this.userService = userService;
    }

    public ReservationProj findReservationById(Long id) {
        return this.reservationRepository.findReservationById(id).orElseThrow(
                () -> { throw new IllegalStateException(String.format("ReservationID: %s does not exist"));} );
    }

    public Collection<ReservationProj> findAllReservation() {
        return this.reservationRepository.findAllReservation();
    }

    @Transactional
    public void deleteReservation(Long id) {
        Reservation reservation = this.reservationRepository.findById(id).orElseThrow(
                () -> {throw new IllegalStateException(String.format("BorrowID: %d does not exist", id));} );
        reservation.getUser().getBorrowed().remove(reservation);
        reservation.getBookCopy().setBorrower(null);
        reservation.getBookCopy().setStatus("Available");
        this.reservationRepository.deleteById(id);
    }
    public void addReservation(ReservationDTO reservationDTO) {
        // get book copy
        BookCopy bkCpy = this.bookCopyRepository.findById(reservationDTO.getBkCpyId()).orElseThrow(
                () -> { throw new IllegalStateException(String.format("BookCopyId: %d does not exist", reservationDTO.getBkCpyId()));});
        // get user
        Optional<User> userOpt = this.userRepository.findById(reservationDTO.getUserDTO().getId());
        UserDTO userDTO = reservationDTO.getUserDTO();
        User user = null;
        Reservation newReservation = null;
        if (userOpt.isPresent()){
            user = userOpt.get();
            this.userService.updateUser(user, userDTO);
        } else {
            user = new User();
            user.setId(userDTO.getId());
            user.setfName(userDTO.getfName());
            user.setmName(userDTO.getmName());
            user.setlName(userDTO.getlName());
            user.setUserType(userDTO.getType());
            user.setAddress(userDTO.getAddress());
            this.userService.updateUserSection(user, userDTO);
            this.userRepository.save(user);
        }
        newReservation = new Reservation(bkCpy, user, reservationDTO.getReservedDate());
        this.reservationRepository.save(newReservation);
        setUserReservation(user, newReservation);
        setBookCopyReservation(bkCpy, newReservation);
        this.reservationRepository.save(newReservation);
    }

    @Transactional
    private void setUserReservation(User user, Reservation reservation){
        user.getReservations().add(reservation);
        reservation.setUser(user);
    }

    @Transactional
    private void setBookCopyReservation(BookCopy bookCopy, Reservation reservation){
        if (reservation.getDate().equals(LocalDate.now()))
            bookCopy.setStatus("Reserved");
        bookCopy.setReserved(reservation);
        reservation.setBookCopy(bookCopy);
    }

    @Transactional
    public void updateBookCopyForReservation() {
        Collection<BookCopy> copies = this.reservationRepository.updateBookCopyForReservation();
        copies.forEach(copy -> copy.setStatus("Reserved"));
    }
}
