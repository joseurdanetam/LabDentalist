package com.labdentalist.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.labdentalist.app.domain.Factura} entity.
 */
public class FacturaDTO implements Serializable {

    private Long id;

    private Integer numeroFactura;

    private Instant fechaEmision;

    private String tipoPago;

    private Instant fechaVencimiento;

    private String decripcion;

    private Double subtotal;

    private Double total;

    private Double importePagado;

    private Double importeAPagar;

    private ClienteDTO cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(Integer numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Instant getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Instant fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Instant getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Instant fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getDecripcion() {
        return decripcion;
    }

    public void setDecripcion(String decripcion) {
        this.decripcion = decripcion;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getImportePagado() {
        return importePagado;
    }

    public void setImportePagado(Double importePagado) {
        this.importePagado = importePagado;
    }

    public Double getImporteAPagar() {
        return importeAPagar;
    }

    public void setImporteAPagar(Double importeAPagar) {
        this.importeAPagar = importeAPagar;
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
        if (!(o instanceof FacturaDTO)) {
            return false;
        }

        FacturaDTO facturaDTO = (FacturaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, facturaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturaDTO{" +
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
            ", cliente=" + getCliente() +
            "}";
    }
}
