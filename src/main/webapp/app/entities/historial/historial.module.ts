import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HistorialComponent } from './list/historial.component';
import { HistorialDetailComponent } from './detail/historial-detail.component';
import { HistorialUpdateComponent } from './update/historial-update.component';
import { HistorialDeleteDialogComponent } from './delete/historial-delete-dialog.component';
import { HistorialRoutingModule } from './route/historial-routing.module';

@NgModule({
  imports: [SharedModule, HistorialRoutingModule],
  declarations: [HistorialComponent, HistorialDetailComponent, HistorialUpdateComponent, HistorialDeleteDialogComponent],
  entryComponents: [HistorialDeleteDialogComponent],
})
export class HistorialModule {}
