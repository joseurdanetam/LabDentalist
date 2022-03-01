import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CitaComponent } from './list/cita.component';
import { CitaDetailComponent } from './detail/cita-detail.component';
import { CitaUpdateComponent } from './update/cita-update.component';
import { CitaDeleteDialogComponent } from './delete/cita-delete-dialog.component';
import { CitaRoutingModule } from './route/cita-routing.module';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule } from 'angularx-flatpickr';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { CalendarCitaComponent } from './update/calendar-cita.component';

@NgModule({
  imports: [
    SharedModule,
    CitaRoutingModule,
    CommonModule,
    FormsModule,
    NgbModalModule,
    FlatpickrModule.forRoot(),
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory,
    }),
  ],
  declarations: [CitaComponent, CitaDetailComponent, CitaUpdateComponent, CitaDeleteDialogComponent, CalendarCitaComponent],
  entryComponents: [CitaDeleteDialogComponent],
})
export class CitaModule {}
