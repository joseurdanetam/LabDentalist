import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIntervencion } from '../intervencion.model';
import { IntervencionService } from '../service/intervencion.service';

@Component({
  templateUrl: './intervencion-delete-dialog.component.html',
})
export class IntervencionDeleteDialogComponent {
  intervencion?: IIntervencion;

  constructor(protected intervencionService: IntervencionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.intervencionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
