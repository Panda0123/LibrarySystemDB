package com.library.database_system.dtos;

import java.time.LocalDate;

public class ReservationDTO {
    private Long reservationId;
    private Long bkCpyId;
    private UserDTO userDTO;
    private LocalDate reservedDate;

    public ReservationDTO(Long reservationId, Long bkCpyId, UserDTO userDTO, LocalDate reservedDate) {
        this.reservationId = reservationId;
        this.bkCpyId = bkCpyId;
        this.userDTO = userDTO;
        this.reservedDate = reservedDate;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getBkCpyId() {
        return bkCpyId;
    }

    public void setBkCpyId(Long bkCpyId) {
        this.bkCpyId = bkCpyId;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public LocalDate getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(LocalDate reservedDate) {
        this.reservedDate = reservedDate;
    }
}
