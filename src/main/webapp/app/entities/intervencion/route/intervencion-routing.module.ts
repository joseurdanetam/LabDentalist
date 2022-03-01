import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IntervencionComponent } from '../list/intervencion.component';
import { IntervencionDetailComponent } from '../detail/intervencion-detail.component';
import { IntervencionUpdateComponent } from '../update/intervencion-update.component';
import { IntervencionRoutingResolveService } from './intervencion-routing-resolve.service';

const intervencionRoute: Routes = [
  {
    path: '',
    component: IntervencionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IntervencionDetailComponent,
    resolve: {
      intervencion: IntervencionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IntervencionUpdateComponent,
    resolve: {
      intervencion: IntervencionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IntervencionUpdateComponent,
    resolve: {
      intervencion: IntervencionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(intervencionRoute)],
  exports: [RouterModule],
})
export class IntervencionRoutingModule {}
