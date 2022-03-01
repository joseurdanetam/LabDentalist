package com.labdentalist.app.service.dto;

import com.labdentalist.app.domain.Factura;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.labdentalist.app.domain.Cliente} entity.
 */
public class ClienteDTO implements Serializable {

    private Long id;

    private String nombre;

    private String apellido;

    private String dni;

    private String sexo;

    private Instant edad;

    private String email;

    private Integer telefono;

    private String direccion;

    private String observacion;

    private List<Factura> factura;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Instant getEdad() {
        return edad;
    }

    public void setEdad(Instant edad) {
        this.edad = edad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClienteDTO)) {
            return false;
        }

        ClienteDTO clienteDTO = (ClienteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clienteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "ClienteDTO [apellido=" +
            apellido +
            ", direccion=" +
            direccion +
            ", dni=" +
            dni +
            ", edad=" +
            edad +
            ", email=" +
            email +
            ", id=" +
            id +
            ", nombre=" +
            nombre +
            ", observacion=" +
            observacion +
            ", sexo=" +
            sexo +
            ", telefono=" +
            telefono +
            "]"
        );
    }
}
