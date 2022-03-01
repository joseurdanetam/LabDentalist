package com.labdentalist.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.labdentalist.app.domain.Historial} entity.
 */
public class HistorialDTO implements Serializable {

    private Long id;

    private ClienteDTO cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(o instanceof HistorialDTO)) {
            return false;
        }

        HistorialDTO historialDTO = (HistorialDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historialDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistorialDTO{" +
            "id=" + getId() +
            ", cliente=" + getCliente() +
            "}";
    }
}
