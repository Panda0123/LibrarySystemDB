package com.library.database_system.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Author {
    @Id
    @SequenceGenerator(
            name = "sequence_author",
            sequenceName = "sequence_author",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_author"
    )
    private Long id;

    private  String f_name;
    private  String m_name;
    private String l_name;

    @ManyToMany
    @JoinTable(
            name = "Author_Book",
            joinColumns = {@JoinColumn(name="author_id")},
            inverseJoinColumns = {@JoinColumn(name = "book_id")})
    private Set<Book> booksAuthored = new HashSet<>();

    public Author() {
    }


    public Author(String f_name, String m_name, String l_name) {
        this.f_name = f_name;
        this.m_name = m_name;
        this.l_name = l_name;
    }

    public Author(Long authorId, String f_name, String m_name, String l_name) {
        this.id = authorId;
        this.f_name = f_name;
        this.m_name = m_name;
        this.l_name = l_name;
    }

    public Long getId() {
        return this.id;
    }

    public String getf_name() {
        return f_name;
    }

    public void setf_name(String f_name) {
        this.f_name = f_name;
    }

    public String getm_name() {
        return m_name;
    }

    public void setm_name(String m_name) {
        this.m_name = m_name;
    }

    public String getl_name() {
        return l_name;
    }

    public void setl_name(String l_name) {
        this.l_name = l_name;
    }

    public Set<Book> getBooksAuthored() {
        return this.booksAuthored;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        return id == author.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", f_name='" + f_name + '\'' +
                ", m_name='" + m_name + '\'' +
                ", l_name='" + l_name + '\'' +
                ", booksAuthored=" + booksAuthored +
                '}';
    }
}
