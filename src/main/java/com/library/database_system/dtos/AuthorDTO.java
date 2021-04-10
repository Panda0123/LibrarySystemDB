package com.library.database_system.dtos;

public class AuthorDTO {

    private Long id;
    private  String f_name;
    private  String m_name;
    private String l_name;

    public AuthorDTO(Long id, String f_name, String m_name, String l_name) {
        this.id = id;
        this.f_name = f_name;
        this.m_name = m_name;
        this.l_name = l_name;
    }

    public Long getId() {
        return id;
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
}
