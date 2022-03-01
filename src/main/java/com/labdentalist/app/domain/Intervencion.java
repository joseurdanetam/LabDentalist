package com.labdentalist.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Intervencion.
 */
@Entity
@Table(name = "intervencion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Intervencion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "precio_unitario")
    private Double precioUnitario;

    @ManyToOne
    @JsonIgnoreProperties(value = { "operaciones", "cliente" }, allowSetters = true)
    private Cita cita;

    @ManyToOne
    @JsonIgnoreProperties(value = { "operaciones", "cliente" }, allowSetters = true)
    private Factura factura;

    @ManyToOne
    @JsonIgnoreProperties(value = { "operaciones", "facturas", "citas", "recetas" }, allowSetters = true)
    private Cliente cliente;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Intervencion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Intervencion titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getPrecioUnitario() {
        return this.precioUnitario;
    }

    public Intervencion precioUnitario(Double precioUnitario) {
        this.setPrecioUnitario(precioUnitario);
        return this;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Cita getCita() {
        return this.cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Intervencion cita(Cita cita) {
        this.setCita(cita);
        return this;
    }

    public Factura getFactura() {
        return this.factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Intervencion factura(Factura factura) {
        this.setFactura(factura);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Intervencion cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Intervencion)) {
            return false;
        }
        return id != null && id.equals(((Intervencion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Intervencion{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", precioUnitario=" + getPrecioUnitario() +
            "}";
    }
}
