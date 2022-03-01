import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHistorial, getHistorialIdentifier } from '../historial.model';
import { IReceta } from 'app/entities/receta/receta.model';
import { IFactura } from 'app/entities/factura/factura.model';

import { ICita } from 'app/entities/cita/cita.model';

export type EntityResponseType = HttpResponse<IHistorial>;
export type EntityArrayResponseType = HttpResponse<IHistorial[]>;

@Injectable({ providedIn: 'root' })
export class HistorialService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/historials');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(historial: IHistorial): Observable<EntityResponseType> {
    return this.http.post<IHistorial>(this.resourceUrl, historial, { observe: 'response' });
  }

  update(historial: IHistorial): Observable<EntityResponseType> {
    return this.http.put<IHistorial>(`${this.resourceUrl}/${getHistorialIdentifier(historial) as number}`, historial, {
      observe: 'response',
    });
  }

  partialUpdate(historial: IHistorial): Observable<EntityResponseType> {
    return this.http.patch<IHistorial>(`${this.resourceUrl}/${getHistorialIdentifier(historial) as number}`, historial, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHistorial>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHistorial[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  findAllByClienteIdFactura(id: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption({ id, req });
    return this.http.get<IFactura[]>(`${this.resourceUrl}/getfactura`, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  searchRecetaById(id: number, req: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption({ id, req });
    return this.http.get<IReceta[]>(`${this.resourceUrl}/getrecetas`, { params: options, observe: 'response' });
  }

  addHistorialToCollectionIfMissing(
    historialCollection: IHistorial[],
    ...historialsToCheck: (IHistorial | null | undefined)[]
  ): IHistorial[] {
    const historials: IHistorial[] = historialsToCheck.filter(isPresent);
    if (historials.length > 0) {
      const historialCollectionIdentifiers = historialCollection.map(historialItem => getHistorialIdentifier(historialItem)!);
      const historialsToAdd = historials.filter(historialItem => {
        const historialIdentifier = getHistorialIdentifier(historialItem);
        if (historialIdentifier == null || historialCollectionIdentifiers.includes(historialIdentifier)) {
          return false;
        }
        historialCollectionIdentifiers.push(historialIdentifier);
        return true;
      });
      return [...historialsToAdd, ...historialCollection];
    }
    return historialCollection;
  }

  findAllByClienteId(id: number, pageable: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption({ id, pageable });
    return this.http.get<ICita[]>(`${this.resourceUrl}/getcitas`, { params: options, observe: 'response' });
  }
}
