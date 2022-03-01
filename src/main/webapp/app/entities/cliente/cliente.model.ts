import dayjs from 'dayjs/esm';
import { IIntervencion } from 'app/entities/intervencion/intervencion.model';
import { IFactura } from 'app/entities/factura/factura.model';
import { ICita } from 'app/entities/cita/cita.model';
import { IReceta } from 'app/entities/receta/receta.model';

export interface ICliente {
  id?: number;
  nombre?: string | null;
  apellido?: string | null;
  dni?: string | null;
  sexo?: string | null;
  edad?: dayjs.Dayjs | null;
  email?: string | null;
  telefono?: number | null;
  direccion?: string | null;
  observacion?: string | null;
  operaciones?: IIntervencion[] | null;
  facturas?: IFactura[] | null;
  citas?: ICita[] | null;
  recetas?: IReceta[] | null;
  factura?: IFactura[] | null;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public apellido?: string | null,
    public dni?: string | null,
    public sexo?: string | null,
    public edad?: dayjs.Dayjs | null,
    public email?: string | null,
    public telefono?: number | null,
    public direccion?: string | null,
    public observacion?: string | null,
    public operaciones?: IIntervencion[] | null,
    public citas?: ICita[] | null,
    public recetas?: IReceta[] | null,
    public factura?: IFactura[] | null
  ) {}
}

export function getClienteIdentifier(cliente: ICliente): number | undefined {
  return cliente.id;
}
