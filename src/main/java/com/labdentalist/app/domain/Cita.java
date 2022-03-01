package com.labdentalist.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cita.
 */
@Entity
@Table(name = "cita")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_emison")
    private Instant fechaEmison;

    @Column(name = "fecha_cita")
    private Instant fechaCita;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "cita")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cita", "factura", "cliente" }, allowSetters = true)
    private Set<Intervencion> operaciones = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "operaciones", "facturas", "citas", "recetas" }, allowSetters = true)
    private Cliente cliente;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cita id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaEmison() {
        return this.fechaEmison;
    }

    public Cita fechaEmison(Instant fechaEmison) {
        this.setFechaEmison(fechaEmison);
        return this;
    }

    public void setFechaEmison(Instant fechaEmison) {
        this.fechaEmison = fechaEmison;
    }

    public Instant getFechaCita() {
        return this.fechaCita;
    }

    public Cita fechaCita(Instant fechaCita) {
        this.setFechaCita(fechaCita);
        return this;
    }

    public void setFechaCita(Instant fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Cita descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Intervencion> getOperaciones() {
        return this.operaciones;
    }

    public void setOperaciones(Set<Intervencion> intervencions) {
        if (this.operaciones != null) {
            this.operaciones.forEach(i -> i.setCita(null));
        }
        if (intervencions != null) {
            intervencions.forEach(i -> i.setCita(this));
        }
        this.operaciones = intervencions;
    }

    public Cita operaciones(Set<Intervencion> intervencions) {
        this.setOperaciones(intervencions);
        return this;
    }

    public Cita addOperaciones(Intervencion intervencion) {
        this.operaciones.add(intervencion);
        intervencion.setCita(this);
        return this;
    }

    public Cita removeOperaciones(Intervencion intervencion) {
        this.operaciones.remove(intervencion);
        intervencion.setCita(null);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cita cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cita)) {
            return false;
        }
        return id != null && id.equals(((Cita) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cita{" +
            "id=" + getId() +
            ", fechaEmison='" + getFechaEmison() + "'" +
            ", fechaCita='" + getFechaCita() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
