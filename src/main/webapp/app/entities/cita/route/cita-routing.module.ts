import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CitaComponent } from '../list/cita.component';
import { CitaDetailComponent } from '../detail/cita-detail.component';
import { CitaUpdateComponent } from '../update/cita-update.component';
import { CitaRoutingResolveService } from './cita-routing-resolve.service';

const citaRoute: Routes = [
  {
    path: '',
    component: CitaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CitaDetailComponent,
    resolve: {
      cita: CitaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CitaUpdateComponent,
    resolve: {
      cita: CitaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CitaUpdateComponent,
    resolve: {
      cita: CitaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(citaRoute)],
  exports: [RouterModule],
})
export class CitaRoutingModule {}
