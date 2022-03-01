import dayjs from 'dayjs/esm';
import { IIntervencion } from 'app/entities/intervencion/intervencion.model';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { IUser } from '../user/user.model';

export interface ICita {
  id?: number;
  fechaEmison?: dayjs.Dayjs | null;
  fechaCita?: dayjs.Dayjs | null;
  descripcion?: string | null;
  operaciones?: IIntervencion[] | null;
  user?: IUser | null;
  cliente?: ICliente | null;
}

export class Cita implements ICita {
  constructor(
    public id?: number,
    public fechaEmison?: dayjs.Dayjs | null,
    public fechaCita?: dayjs.Dayjs | null,
    public descripcion?: string | null,
    public operaciones?: IIntervencion[] | null,
    public user?: IUser | null,
    public cliente?: ICliente | null
  ) {}
}

export function getCitaIdentifier(cita: ICita): number | undefined {
  return cita.id;
}
