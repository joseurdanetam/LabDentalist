import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIntervencion, getIntervencionIdentifier } from '../intervencion.model';

export type EntityResponseType = HttpResponse<IIntervencion>;
export type EntityArrayResponseType = HttpResponse<IIntervencion[]>;

@Injectable({ providedIn: 'root' })
export class IntervencionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/intervencions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(intervencion: IIntervencion): Observable<EntityResponseType> {
    return this.http.post<IIntervencion>(this.resourceUrl, intervencion, { observe: 'response' });
  }

  update(intervencion: IIntervencion): Observable<EntityResponseType> {
    return this.http.put<IIntervencion>(`${this.resourceUrl}/${getIntervencionIdentifier(intervencion) as number}`, intervencion, {
      observe: 'response',
    });
  }

  partialUpdate(intervencion: IIntervencion): Observable<EntityResponseType> {
    return this.http.patch<IIntervencion>(`${this.resourceUrl}/${getIntervencionIdentifier(intervencion) as number}`, intervencion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIntervencion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIntervencion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIntervencionToCollectionIfMissing(
    intervencionCollection: IIntervencion[],
    ...intervencionsToCheck: (IIntervencion | null | undefined)[]
  ): IIntervencion[] {
    const intervencions: IIntervencion[] = intervencionsToCheck.filter(isPresent);
    if (intervencions.length > 0) {
      const intervencionCollectionIdentifiers = intervencionCollection.map(
        intervencionItem => getIntervencionIdentifier(intervencionItem)!
      );
      const intervencionsToAdd = intervencions.filter(intervencionItem => {
        const intervencionIdentifier = getIntervencionIdentifier(intervencionItem);
        if (intervencionIdentifier == null || intervencionCollectionIdentifiers.includes(intervencionIdentifier)) {
          return false;
        }
        intervencionCollectionIdentifiers.push(intervencionIdentifier);
        return true;
      });
      return [...intervencionsToAdd, ...intervencionCollection];
    }
    return intervencionCollection;
  }

  getAllIntervenciones(id: number): Observable<EntityArrayResponseType> {
    const options = createRequestOption({ id });
    return this.http.get<IIntervencion[]>(`${this.resourceUrl}/get-factura-intervencion`, { params: options, observe: 'response' });
  }
}
