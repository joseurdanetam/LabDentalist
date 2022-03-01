import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReceta, getRecetaIdentifier } from '../receta.model';

export type EntityResponseType = HttpResponse<IReceta>;
export type EntityArrayResponseType = HttpResponse<IReceta[]>;

@Injectable({ providedIn: 'root' })
export class RecetaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/recetas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(receta: IReceta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(receta);
    return this.http
      .post<IReceta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(receta: IReceta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(receta);
    return this.http
      .put<IReceta>(`${this.resourceUrl}/${getRecetaIdentifier(receta) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(receta: IReceta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(receta);
    return this.http
      .patch<IReceta>(`${this.resourceUrl}/${getRecetaIdentifier(receta) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IReceta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReceta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRecetaToCollectionIfMissing(recetaCollection: IReceta[], ...recetasToCheck: (IReceta | null | undefined)[]): IReceta[] {
    const recetas: IReceta[] = recetasToCheck.filter(isPresent);
    if (recetas.length > 0) {
      const recetaCollectionIdentifiers = recetaCollection.map(recetaItem => getRecetaIdentifier(recetaItem)!);
      const recetasToAdd = recetas.filter(recetaItem => {
        const recetaIdentifier = getRecetaIdentifier(recetaItem);
        if (recetaIdentifier == null || recetaCollectionIdentifiers.includes(recetaIdentifier)) {
          return false;
        }
        recetaCollectionIdentifiers.push(recetaIdentifier);
        return true;
      });
      return [...recetasToAdd, ...recetaCollection];
    }
    return recetaCollection;
  }

  busquedaRecetas(filtro: string, pageable: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption({ filtro, ...pageable });
    return this.http.get<IReceta[]>(`${this.resourceUrl}/busqueda-recetas`, { params: options, observe: 'response' });
  }

  protected convertDateFromClient(receta: IReceta): IReceta {
    return Object.assign({}, receta, {
      fechaEmision: receta.fechaEmision?.isValid() ? receta.fechaEmision.toJSON() : undefined,
      fechaVencimiento: receta.fechaVencimiento?.isValid() ? receta.fechaVencimiento.toJSON() : undefined,
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
      res.body.forEach((receta: IReceta) => {
        receta.fechaEmision = receta.fechaEmision ? dayjs(receta.fechaEmision) : undefined;
        receta.fechaVencimiento = receta.fechaVencimiento ? dayjs(receta.fechaVencimiento) : undefined;
      });
    }
    return res;
  }
}
