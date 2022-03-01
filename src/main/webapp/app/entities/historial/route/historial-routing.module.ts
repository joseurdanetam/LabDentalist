import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HistorialComponent } from '../list/historial.component';
import { HistorialDetailComponent } from '../detail/historial-detail.component';
import { HistorialUpdateComponent } from '../update/historial-update.component';
import { HistorialRoutingResolveService } from './historial-routing-resolve.service';

const historialRoute: Routes = [
  {
    path: '',
    component: HistorialComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HistorialDetailComponent,
    resolve: {
      historial: HistorialRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HistorialUpdateComponent,
    resolve: {
      historial: HistorialRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HistorialUpdateComponent,
    resolve: {
      historial: HistorialRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(historialRoute)],
  exports: [RouterModule],
})
export class HistorialRoutingModule {}
