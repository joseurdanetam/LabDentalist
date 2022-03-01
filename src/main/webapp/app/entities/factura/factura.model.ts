import dayjs from 'dayjs/esm';
import { IIntervencion } from 'app/entities/intervencion/intervencion.model';
import { ICliente } from 'app/entities/cliente/cliente.model';

export interface IFactura {
  id?: number;
  numeroFactura?: number | null;
  fechaEmision?: dayjs.Dayjs | null;
  tipoPago?: string | null;
  fechaVencimiento?: dayjs.Dayjs | null;
  decripcion?: string | null;
  subtotal?: number | null;
  total?: number | null;
  importePagado?: number | null;
  importeAPagar?: number | null;
  operaciones?: IIntervencion[] | null;
  cliente?: ICliente | null;
}

export class Factura implements IFactura {
  constructor(
    public id?: number,
    public numeroFactura?: number | null,
    public fechaEmision?: dayjs.Dayjs | null,
    public tipoPago?: string | null,
    public fechaVencimiento?: dayjs.Dayjs | null,
    public decripcion?: string | null,
    public subtotal?: number | null,
    public total?: number | null,
    public importePagado?: number | null,
    public importeAPagar?: number | null,
    public operaciones?: IIntervencion[] | null,
    public cliente?: ICliente | null
  ) {}
}

export function getFacturaIdentifier(factura: IFactura): number | undefined {
  return factura.id;
}
