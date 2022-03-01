import { ICita } from 'app/entities/cita/cita.model';
import { IFactura } from 'app/entities/factura/factura.model';
import { ICliente } from 'app/entities/cliente/cliente.model';

export interface IIntervencion {
  id?: number;
  titulo?: string | null;
  precioUnitario?: number | null;
  cita?: ICita | null;
  factura?: IFactura | null;
  cliente?: ICliente | null;
}

export class Intervencion implements IIntervencion {
  constructor(
    public id?: number,
    public titulo?: string | null,
    public precioUnitario?: number | null,
    public cita?: ICita | null,
    public factura?: IFactura | null,
    public cliente?: ICliente | null
  ) {}
}

export function getIntervencionIdentifier(intervencion: IIntervencion): number | undefined {
  return intervencion.id;
}
