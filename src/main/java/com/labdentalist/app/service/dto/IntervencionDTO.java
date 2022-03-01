package com.labdentalist.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.labdentalist.app.domain.Intervencion} entity.
 */
public class IntervencionDTO implements Serializable {

    private Long id;

    private String titulo;

    private Double precioUnitario;

    private CitaDTO cita;

    private FacturaDTO factura;

    private ClienteDTO cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public CitaDTO getCita() {
        return cita;
    }

    public void setCita(CitaDTO cita) {
        this.cita = cita;
    }

    public FacturaDTO getFactura() {
        return factura;
    }

    public void setFactura(FacturaDTO factura) {
        this.factura = factura;
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
        if (!(o instanceof IntervencionDTO)) {
            return false;
        }

        IntervencionDTO intervencionDTO = (IntervencionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, intervencionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IntervencionDTO{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", precioUnitario=" + getPrecioUnitario() +
            ", cita=" + getCita() +
            ", factura=" + getFactura() +
            ", cliente=" + getCliente() +
            "}";
    }
}
