package com.labdentalist.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.labdentalist.app.domain.Receta} entity.
 */
public class RecetaDTO implements Serializable {

    private Long id;

    private Integer numeroReceta;

    private Instant fechaEmision;

    private Instant fechaVencimiento;

    private String descripcion;

    private ClienteDTO cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroReceta() {
        return numeroReceta;
    }

    public void setNumeroReceta(Integer numeroReceta) {
        this.numeroReceta = numeroReceta;
    }

    public Instant getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Instant fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Instant getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Instant fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecetaDTO)) {
            return false;
        }

        RecetaDTO recetaDTO = (RecetaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recetaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecetaDTO{" +
            "id=" + getId() +
            ", numeroReceta=" + getNumeroReceta() +
            ", fechaEmision='" + getFechaEmision() + "'" +
            ", fechaVencimiento='" + getFechaVencimiento() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", cliente=" + getCliente() +
            "}";
    }
}
