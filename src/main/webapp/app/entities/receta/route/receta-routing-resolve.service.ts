import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReceta, Receta } from '../receta.model';
import { RecetaService } from '../service/receta.service';

@Injectable({ providedIn: 'root' })
export class RecetaRoutingResolveService implements Resolve<IReceta> {
  constructor(protected service: RecetaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReceta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((receta: HttpResponse<Receta>) => {
          if (receta.body) {
            return of(receta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Receta());
  }
}
