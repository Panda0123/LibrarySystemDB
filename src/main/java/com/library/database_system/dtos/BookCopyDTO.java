package com.library.database_system.dtos;

public class BookCopyDTO {
    private Long id;
    private  int copy_num;
    private  String status;

    public BookCopyDTO(Long id, int copy_num, String status) {
        this.id = id;
        this.copy_num = copy_num;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCopy_num() {
        return copy_num;
    }

    public void setCopy_num(int copy_num) {
        this.copy_num = copy_num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
