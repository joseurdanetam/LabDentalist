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
 * A Factura.
 */
@Entity
@Table(name = "factura")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero_factura")
    private Integer numeroFactura;

    @Column(name = "fecha_emision")
    private Instant fechaEmision;

    @Column(name = "tipo_pago")
    private String tipoPago;

    @Column(name = "fecha_vencimiento")
    private Instant fechaVencimiento;

    @Column(name = "decripcion")
    private String decripcion;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "total")
    private Double total;

    @Column(name = "importe_pagado")
    private Double importePagado;

    @Column(name = "importe_a_pagar")
    private Double importeAPagar;

    @OneToMany(mappedBy = "factura")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cita", "factura", "cliente" }, allowSetters = true)
    private Set<Intervencion> operaciones = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "operaciones", "facturas", "citas", "recetas" }, allowSetters = true)
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Factura id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroFactura() {
        return this.numeroFactura;
    }

    public Factura numeroFactura(Integer numeroFactura) {
        this.setNumeroFactura(numeroFactura);
        return this;
    }

    public void setNumeroFactura(Integer numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Instant getFechaEmision() {
        return this.fechaEmision;
    }

    public Factura fechaEmision(Instant fechaEmision) {
        this.setFechaEmision(fechaEmision);
        return this;
    }

    public void setFechaEmision(Instant fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getTipoPago() {
        return this.tipoPago;
    }

    public Factura tipoPago(String tipoPago) {
        this.setTipoPago(tipoPago);
        return this;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Instant getFechaVencimiento() {
        return this.fechaVencimiento;
    }

    public Factura fechaVencimiento(Instant fechaVencimiento) {
        this.setFechaVencimiento(fechaVencimiento);
        return this;
    }

    public void setFechaVencimiento(Instant fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getDecripcion() {
        return this.decripcion;
    }

    public Factura decripcion(String decripcion) {
        this.setDecripcion(decripcion);
        return this;
    }

    public void setDecripcion(String decripcion) {
        this.decripcion = decripcion;
    }

    public Double getSubtotal() {
        return this.subtotal;
    }

    public Factura subtotal(Double subtotal) {
        this.setSubtotal(subtotal);
        return this;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getTotal() {
        return this.total;
    }

    public Factura total(Double total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getImportePagado() {
        return this.importePagado;
    }

    public Factura importePagado(Double importePagado) {
        this.setImportePagado(importePagado);
        return this;
    }

    public void setImportePagado(Double importePagado) {
        this.importePagado = importePagado;
    }

    public Double getImporteAPagar() {
        return this.importeAPagar;
    }

    public Factura importeAPagar(Double importeAPagar) {
        this.setImporteAPagar(importeAPagar);
        return this;
    }

    public void setImporteAPagar(Double importeAPagar) {
        this.importeAPagar = importeAPagar;
    }

    public Set<Intervencion> getOperaciones() {
        return this.operaciones;
    }

    public void setOperaciones(Set<Intervencion> intervencions) {
        if (this.operaciones != null) {
            this.operaciones.forEach(i -> i.setFactura(null));
        }
        if (intervencions != null) {
            intervencions.forEach(i -> i.setFactura(this));
        }
        this.operaciones = intervencions;
    }

    public Factura operaciones(Set<Intervencion> intervencions) {
        this.setOperaciones(intervencions);
        return this;
    }

    public Factura addOperaciones(Intervencion intervencion) {
        this.operaciones.add(intervencion);
        intervencion.setFactura(this);
        return this;
    }

    public Factura removeOperaciones(Intervencion intervencion) {
        this.operaciones.remove(intervencion);
        intervencion.setFactura(null);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Factura cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Factura)) {
            return false;
        }
        return id != null && id.equals(((Factura) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Factura{" +
            "id=" + getId() +
            ", numeroFactura=" + getNumeroFactura() +
            ", fechaEmision='" + getFechaEmision() + "'" +
            ", tipoPago='" + getTipoPago() + "'" +
            ", fechaVencimiento='" + getFechaVencimiento() + "'" +
            ", decripcion='" + getDecripcion() + "'" +
            ", subtotal=" + getSubtotal() +
            ", total=" + getTotal() +
            ", importePagado=" + getImportePagado() +
            ", importeAPagar=" + getImporteAPagar() +
            "}";
    }
}
