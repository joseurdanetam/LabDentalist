import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RecetaComponent } from '../list/receta.component';
import { RecetaDetailComponent } from '../detail/receta-detail.component';
import { RecetaUpdateComponent } from '../update/receta-update.component';
import { RecetaRoutingResolveService } from './receta-routing-resolve.service';

const recetaRoute: Routes = [
  {
    path: '',
    component: RecetaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RecetaDetailComponent,
    resolve: {
      receta: RecetaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RecetaUpdateComponent,
    resolve: {
      receta: RecetaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RecetaUpdateComponent,
    resolve: {
      receta: RecetaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(recetaRoute)],
  exports: [RouterModule],
})
export class RecetaRoutingModule {}
