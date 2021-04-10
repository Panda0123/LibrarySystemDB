package com.library.database_system.dtos;

public class UserDTO {
    private String id;
    private String fName;
    private String mName;
    private String lName;
    private String type;
    private String address;
    private SectionDTO sectionDTO;

    public UserDTO(String id, String fName, String mName, String lName, String type, String address, SectionDTO sectionDTO) {
        this.id = id;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.type = type;
        this.address = address;
        this.sectionDTO = sectionDTO;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SectionDTO getSectionDTO() {
        return sectionDTO;
    }

    public void setSectionDTO(SectionDTO sectionDTO) {
        this.sectionDTO = sectionDTO;
    }
}
