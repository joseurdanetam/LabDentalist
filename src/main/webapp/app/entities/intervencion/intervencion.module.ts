import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IntervencionComponent } from './list/intervencion.component';
import { IntervencionDetailComponent } from './detail/intervencion-detail.component';
import { IntervencionUpdateComponent } from './update/intervencion-update.component';
import { IntervencionDeleteDialogComponent } from './delete/intervencion-delete-dialog.component';
import { IntervencionRoutingModule } from './route/intervencion-routing.module';

@NgModule({
  imports: [SharedModule, IntervencionRoutingModule],
  declarations: [IntervencionComponent, IntervencionDetailComponent, IntervencionUpdateComponent, IntervencionDeleteDialogComponent],
  entryComponents: [IntervencionDeleteDialogComponent],
})
export class IntervencionModule {}
