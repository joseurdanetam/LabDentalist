import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFactura, getFacturaIdentifier } from '../factura.model';

export type EntityResponseType = HttpResponse<IFactura>;
export type EntityArrayResponseType = HttpResponse<IFactura[]>;

@Injectable({ providedIn: 'root' })
export class FacturaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/facturas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(factura: IFactura): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(factura);
    return this.http
      .post<IFactura>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(factura: IFactura): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(factura);
    return this.http
      .put<IFactura>(`${this.resourceUrl}/${getFacturaIdentifier(factura) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(factura: IFactura): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(factura);
    return this.http
      .patch<IFactura>(`${this.resourceUrl}/${getFacturaIdentifier(factura) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFactura>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFactura[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFacturaToCollectionIfMissing(facturaCollection: IFactura[], ...facturasToCheck: (IFactura | null | undefined)[]): IFactura[] {
    const facturas: IFactura[] = facturasToCheck.filter(isPresent);
    if (facturas.length > 0) {
      const facturaCollectionIdentifiers = facturaCollection.map(facturaItem => getFacturaIdentifier(facturaItem)!);
      const facturasToAdd = facturas.filter(facturaItem => {
        const facturaIdentifier = getFacturaIdentifier(facturaItem);
        if (facturaIdentifier == null || facturaCollectionIdentifiers.includes(facturaIdentifier)) {
          return false;
        }
        facturaCollectionIdentifiers.push(facturaIdentifier);
        return true;
      });
      return [...facturasToAdd, ...facturaCollection];
    }
    return facturaCollection;
  }

  busquedaFacturas(filtro: string, pageable: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption({ filtro, ...pageable });
    return this.http.get<IFactura[]>(`${this.resourceUrl}/busqueda-facturas`, { params: options, observe: 'response' });
  }

  protected convertDateFromClient(factura: IFactura): IFactura {
    return Object.assign({}, factura, {
      fechaEmision: factura.fechaEmision?.isValid() ? factura.fechaEmision.toJSON() : undefined,
      fechaVencimiento: factura.fechaVencimiento?.isValid() ? factura.fechaVencimiento.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaEmision = res.body.fechaEmision ? dayjs(res.body.fechaEmision) : undefined;
      res.body.fechaVencimiento = res.body.fechaVencimiento ? dayjs(res.body.fechaVencimiento) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((factura: IFactura) => {
        factura.fechaEmision = factura.fechaEmision ? dayjs(factura.fechaEmision) : undefined;
        factura.fechaVencimiento = factura.fechaVencimiento ? dayjs(factura.fechaVencimiento) : undefined;
      });
    }
    return res;
  }
}
