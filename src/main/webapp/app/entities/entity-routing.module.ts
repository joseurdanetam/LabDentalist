import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cita',
        data: { pageTitle: 'labDentalistApp.cita.home.title' },
        loadChildren: () => import('./cita/cita.module').then(m => m.CitaModule),
      },
      {
        path: 'cliente',
        data: { pageTitle: 'labDentalistApp.cliente.home.title' },
        loadChildren: () => import('./cliente/cliente.module').then(m => m.ClienteModule),
      },
      {
        path: 'historial',
        data: { pageTitle: 'labDentalistApp.historial.home.title' },
        loadChildren: () => import('./historial/historial.module').then(m => m.HistorialModule),
      },
      {
        path: 'factura',
        data: { pageTitle: 'labDentalistApp.factura.home.title' },
        loadChildren: () => import('./factura/factura.module').then(m => m.FacturaModule),
      },
      {
        path: 'intervencion',
        data: { pageTitle: 'labDentalistApp.intervencion.home.title' },
        loadChildren: () => import('./intervencion/intervencion.module').then(m => m.IntervencionModule),
      },
      {
        path: 'receta',
        data: { pageTitle: 'labDentalistApp.receta.home.title' },
        loadChildren: () => import('./receta/receta.module').then(m => m.RecetaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
