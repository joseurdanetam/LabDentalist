import dayjs from 'dayjs/esm';
import { ICliente } from 'app/entities/cliente/cliente.model';

export interface IReceta {
  id?: number;
  numeroReceta?: number | null;
  fechaEmision?: dayjs.Dayjs | null;
  fechaVencimiento?: dayjs.Dayjs | null;
  descripcion?: string | null;
  cliente?: ICliente | null;
}

export class Receta implements IReceta {
  constructor(
    public id?: number,
    public numeroReceta?: number | null,
    public fechaEmision?: dayjs.Dayjs | null,
    public fechaVencimiento?: dayjs.Dayjs | null,
    public descripcion?: string | null,
    public cliente?: ICliente | null
  ) {}
}

export function getRecetaIdentifier(receta: IReceta): number | undefined {
  return receta.id;
}
