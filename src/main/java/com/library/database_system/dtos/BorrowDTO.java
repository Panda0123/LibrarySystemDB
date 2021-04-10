package com.library.database_system.dtos;

import java.time.LocalDate;

public class BorrowDTO {
    private Long borrowId;
    private Long bkCpyId;
    // private BookCopyDTO bookCopyDTO;
    private UserDTO userDTO;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnedDate;

    public BorrowDTO() {

    }
    public BorrowDTO(Long borrowId, Long bkCpyId, UserDTO userDTO, LocalDate issueDate, LocalDate dueDate) {
        this.borrowId = borrowId;
        this.bkCpyId = bkCpyId;
        this.userDTO = userDTO;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    public BorrowDTO(Long borrowId, Long bkCpyId, UserDTO userDTO, LocalDate issueDate, LocalDate dueDate, LocalDate returnedDate) {
        this.borrowId = borrowId;
        this.bkCpyId = bkCpyId;
        this.userDTO = userDTO;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnedDate = returnedDate;
    }


    public Long getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Long borrowId) {
        this.borrowId = borrowId;
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

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }

}
