import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RecetaComponent } from './list/receta.component';
import { RecetaDetailComponent } from './detail/receta-detail.component';
import { RecetaUpdateComponent } from './update/receta-update.component';
import { RecetaDeleteDialogComponent } from './delete/receta-delete-dialog.component';
import { RecetaRoutingModule } from './route/receta-routing.module';

@NgModule({
  imports: [SharedModule, RecetaRoutingModule],
  declarations: [RecetaComponent, RecetaDetailComponent, RecetaUpdateComponent, RecetaDeleteDialogComponent],
  entryComponents: [RecetaDeleteDialogComponent],
})
export class RecetaModule {}
