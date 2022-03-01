import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIntervencion, Intervencion } from '../intervencion.model';
import { IntervencionService } from '../service/intervencion.service';

@Injectable({ providedIn: 'root' })
export class IntervencionRoutingResolveService implements Resolve<IIntervencion> {
  constructor(protected service: IntervencionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIntervencion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((intervencion: HttpResponse<Intervencion>) => {
          if (intervencion.body) {
            return of(intervencion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Intervencion());
  }
}
