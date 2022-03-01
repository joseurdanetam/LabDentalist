package com.labdentalist.app.service.dto;

import com.labdentalist.app.domain.User;

/**
 * A DTO representing a user, with only the public attributes.
 */
public class UserDTO {

    private Long id;

    private String dni;

    private String firstName;

    private String lastName;

    private Integer telefono;

    private String categoria;

    private String login;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.dni = user.getDni();
        this.firstName = user.getFirstName();
        this.telefono = user.getTelefono();
        this.lastName = user.getLastName();
        this.categoria = user.getCategoria();
        // Customize it here if you need, or not, firstName/lastName/etc
        this.login = user.getLogin();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return (
            "UserDTO [categoria=" +
            categoria +
            ", dni=" +
            dni +
            ", firstName=" +
            firstName +
            ", id=" +
            id +
            ", lastName=" +
            lastName +
            ", login=" +
            login +
            ", telefono=" +
            telefono +
            "]"
        );
    }
}
