import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHistorial, Historial } from '../historial.model';
import { HistorialService } from '../service/historial.service';

@Injectable({ providedIn: 'root' })
export class HistorialRoutingResolveService implements Resolve<IHistorial> {
  constructor(protected service: HistorialService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHistorial> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((historial: HttpResponse<Historial>) => {
          if (historial.body) {
            return of(historial.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Historial());
  }
}
