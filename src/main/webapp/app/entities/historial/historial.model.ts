import { ICliente } from 'app/entities/cliente/cliente.model';

export interface IHistorial {
  id?: number;
  cliente?: ICliente | null;
}

export class Historial implements IHistorial {
  constructor(public id?: number, public cliente?: ICliente | null) {}
}

export function getHistorialIdentifier(historial: IHistorial): number | undefined {
  return historial.id;
}
