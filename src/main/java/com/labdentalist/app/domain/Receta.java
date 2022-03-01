package com.labdentalist.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Receta.
 */
@Entity
@Table(name = "receta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Receta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero_receta")
    private Integer numeroReceta;

    @Column(name = "fecha_emision")
    private Instant fechaEmision;

    @Column(name = "fecha_vencimiento")
    private Instant fechaVencimiento;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToOne
    @JsonIgnoreProperties(value = { "operaciones", "facturas", "citas", "recetas" }, allowSetters = true)
    private Cliente cliente;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Receta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroReceta() {
        return this.numeroReceta;
    }

    public Receta numeroReceta(Integer numeroReceta) {
        this.setNumeroReceta(numeroReceta);
        return this;
    }

    public void setNumeroReceta(Integer numeroReceta) {
        this.numeroReceta = numeroReceta;
    }

    public Instant getFechaEmision() {
        return this.fechaEmision;
    }

    public Receta fechaEmision(Instant fechaEmision) {
        this.setFechaEmision(fechaEmision);
        return this;
    }

    public void setFechaEmision(Instant fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Instant getFechaVencimiento() {
        return this.fechaVencimiento;
    }

    public Receta fechaVencimiento(Instant fechaVencimiento) {
        this.setFechaVencimiento(fechaVencimiento);
        return this;
    }

    public void setFechaVencimiento(Instant fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Receta descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Receta cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Receta)) {
            return false;
        }
        return id != null && id.equals(((Receta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Receta{" +
            "id=" + getId() +
            ", numeroReceta=" + getNumeroReceta() +
            ", fechaEmision='" + getFechaEmision() + "'" +
            ", fechaVencimiento='" + getFechaVencimiento() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
