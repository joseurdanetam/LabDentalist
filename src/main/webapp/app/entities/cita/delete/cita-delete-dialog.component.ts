import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICita } from '../cita.model';
import { CitaService } from '../service/cita.service';

@Component({
  templateUrl: './cita-delete-dialog.component.html',
})
export class CitaDeleteDialogComponent {
  cita?: ICita;

  constructor(protected citaService: CitaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.citaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
