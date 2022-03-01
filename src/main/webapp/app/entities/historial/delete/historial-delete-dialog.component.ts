import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHistorial } from '../historial.model';
import { HistorialService } from '../service/historial.service';

@Component({
  templateUrl: './historial-delete-dialog.component.html',
})
export class HistorialDeleteDialogComponent {
  historial?: IHistorial;

  constructor(protected historialService: HistorialService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.historialService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
