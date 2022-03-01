import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReceta } from '../receta.model';
import { RecetaService } from '../service/receta.service';

@Component({
  templateUrl: './receta-delete-dialog.component.html',
})
export class RecetaDeleteDialogComponent {
  receta?: IReceta;

  constructor(protected recetaService: RecetaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.recetaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
