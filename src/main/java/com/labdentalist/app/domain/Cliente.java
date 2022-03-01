package com.labdentalist.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe contener entre 3 y 50 carácteres")
    @Column(name = "nombre")
    private String nombre;

    @NotNull(message = "El apellido no puede estar vacío")
    @Size(min = 3, max = 50, message = "El apellido debe contener entre 3 y 50 carácteres")
    @Column(name = "apellido")
    private String apellido;

    @NotNull(message = "El DNI no puede estar vacío")
    @Size(min = 9, max = 9, message = "El dni debe contener 9 carácteres")
    @Column(name = "dni", unique = true)
    private String dni;

    @NotNull(message = "El sexo no puede estar vacío")
    @Column(name = "sexo")
    private String sexo;

    @NotNull(message = "La edad no puede estar vacía")
    @Column(name = "edad")
    private Instant edad;

    @Column(name = "email")
    private String email;

    @NotNull(message = "El telefono no puede estar vacío")
    @Min(value = 9, message = "El telefono debe contener 9 carácteres")
    @Column(name = "telefono")
    private Integer telefono;

    @NotNull(message = "La direccion no puede estar vacía")
    @Size(min = 5, max = 60, message = "El nombre debe contener entre 5 y 60 carácteres")
    @Column(name = "direccion")
    private String direccion;

    @Column(name = "observacion")
    private String observacion;

    @OneToMany(mappedBy = "cliente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cita", "factura", "cliente" }, allowSetters = true)
    private Set<Intervencion> operaciones = new HashSet<>();

    @OneToMany(mappedBy = "cliente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "operaciones", "cliente" }, allowSetters = true)
    private Set<Factura> facturas = new HashSet<>();

    @OneToMany(mappedBy = "cliente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "operaciones", "cliente" }, allowSetters = true)
    private Set<Cita> citas = new HashSet<>();

    @OneToMany(mappedBy = "cliente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cliente" }, allowSetters = true)
    private Set<Receta> recetas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cliente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Cliente nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Cliente apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return this.dni;
    }

    public Cliente dni(String dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getSexo() {
        return this.sexo;
    }

    public Cliente sexo(String sexo) {
        this.setSexo(sexo);
        return this;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Instant getEdad() {
        return this.edad;
    }

    public Cliente edad(Instant edad) {
        this.setEdad(edad);
        return this;
    }

    public void setEdad(Instant edad) {
        this.edad = edad;
    }

    public String getEmail() {
        return this.email;
    }

    public Cliente email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTelefono() {
        return this.telefono;
    }

    public Cliente telefono(Integer telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Cliente direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getObservacion() {
        return this.observacion;
    }

    public Cliente observacion(String observacion) {
        this.setObservacion(observacion);
        return this;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Set<Intervencion> getOperaciones() {
        return this.operaciones;
    }

    public void setOperaciones(Set<Intervencion> intervencions) {
        if (this.operaciones != null) {
            this.operaciones.forEach(i -> i.setCliente(null));
        }
        if (intervencions != null) {
            intervencions.forEach(i -> i.setCliente(this));
        }
        this.operaciones = intervencions;
    }

    public Cliente operaciones(Set<Intervencion> intervencions) {
        this.setOperaciones(intervencions);
        return this;
    }

    public Cliente addOperaciones(Intervencion intervencion) {
        this.operaciones.add(intervencion);
        intervencion.setCliente(this);
        return this;
    }

    public Cliente removeOperaciones(Intervencion intervencion) {
        this.operaciones.remove(intervencion);
        intervencion.setCliente(null);
        return this;
    }

    public Set<Factura> getFacturas() {
        return this.facturas;
    }

    public void setFacturas(Set<Factura> facturas) {
        if (this.facturas != null) {
            this.facturas.forEach(i -> i.setCliente(null));
        }
        if (facturas != null) {
            facturas.forEach(i -> i.setCliente(this));
        }
        this.facturas = facturas;
    }

    public Cliente facturas(Set<Factura> facturas) {
        this.setFacturas(facturas);
        return this;
    }

    public Cliente addFacturas(Factura factura) {
        this.facturas.add(factura);
        factura.setCliente(this);
        return this;
    }

    public Cliente removeFacturas(Factura factura) {
        this.facturas.remove(factura);
        factura.setCliente(null);
        return this;
    }

    public Set<Cita> getCitas() {
        return this.citas;
    }

    public void setCitas(Set<Cita> citas) {
        if (this.citas != null) {
            this.citas.forEach(i -> i.setCliente(null));
        }
        if (citas != null) {
            citas.forEach(i -> i.setCliente(this));
        }
        this.citas = citas;
    }

    public Cliente citas(Set<Cita> citas) {
        this.setCitas(citas);
        return this;
    }

    public Cliente addCitas(Cita cita) {
        this.citas.add(cita);
        cita.setCliente(this);
        return this;
    }

    public Cliente removeCitas(Cita cita) {
        this.citas.remove(cita);
        cita.setCliente(null);
        return this;
    }

    public Set<Receta> getRecetas() {
        return this.recetas;
    }

    public void setRecetas(Set<Receta> recetas) {
        if (this.recetas != null) {
            this.recetas.forEach(i -> i.setCliente(null));
        }
        if (recetas != null) {
            recetas.forEach(i -> i.setCliente(this));
        }
        this.recetas = recetas;
    }

    public Cliente recetas(Set<Receta> recetas) {
        this.setRecetas(recetas);
        return this;
    }

    public Cliente addRecetas(Receta receta) {
        this.recetas.add(receta);
        receta.setCliente(this);
        return this;
    }

    public Cliente removeRecetas(Receta receta) {
        this.recetas.remove(receta);
        receta.setCliente(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return id != null && id.equals(((Cliente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", dni='" + getDni() + "'" +
            ", sexo='" + getSexo() + "'" +
            ", edad='" + getEdad() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefono=" + getTelefono() +
            ", direccion='" + getDireccion() + "'" +
            ", observacion='" + getObservacion() + "'" +
            "}";
    }
}
