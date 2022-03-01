import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FacturaComponent } from './list/factura.component';
import { FacturaDetailComponent } from './detail/factura-detail.component';
import { FacturaUpdateComponent } from './update/factura-update.component';
import { FacturaDeleteDialogComponent } from './delete/factura-delete-dialog.component';
import { FacturaRoutingModule } from './route/factura-routing.module';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';

@NgModule({
  imports: [SharedModule, FacturaRoutingModule, NgMultiSelectDropDownModule],
  declarations: [FacturaComponent, FacturaDetailComponent, FacturaUpdateComponent, FacturaDeleteDialogComponent],
  entryComponents: [FacturaDeleteDialogComponent],
})
export class FacturaModule {}
