package com.library.database_system.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        uniqueConstraints = {@UniqueConstraint(name = "user_id_unique", columnNames = "id")}
)
public class User {

    @Id
    @Column(nullable = false, unique = true)
    public String id;
    public String f_name;
    public String m_name;
    public String l_name;
    // public String status;
    public String address;
    public String userType;

    @OneToMany( mappedBy = "user")
    public Set<Borrow> borrowed = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "section_id")
    public Section section;

    @OneToMany( mappedBy = "user")
    public Set<Reservation> reservations = new HashSet<>();

    // CONSTRUCTORS
    public User() {
    }

    public User(String id, String fName, String mName, String lName, String address, String userType) {
        this.id = id;
        this.f_name = fName;
        this.m_name = mName;
        this.l_name = lName;
        // this.status = status;
        this.address = address;
        this.userType = userType;
    }

    // GETTERS and SETTERS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getfName() {
        return f_name;
    }

    public void setfName(String fName) {
        this.f_name = fName;
    }

    public String getmName() {
        return m_name;
    }

    public void setmName(String mName) {
        this.m_name = mName;
    }

    public String getlName() {
        return l_name;
    }

    public void setlName(String lName) {
        this.l_name = lName;
    }

//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Set<Borrow> getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(Set<Borrow> borrowed) {
        this.borrowed = borrowed;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }


    // OTHER METHODS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + f_name.hashCode();
        result = 31 * result + m_name.hashCode();
        result = 31 * result + l_name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fName='" + f_name + '\'' +
                ", mName='" + m_name + '\'' +
                ", lName='" + l_name + '\'' +
                //", status=" + status +
                '}';
    }
}
