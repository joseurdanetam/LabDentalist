import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFactura } from '../factura.model';
import { FacturaService } from '../service/factura.service';

@Component({
  templateUrl: './factura-delete-dialog.component.html',
})
export class FacturaDeleteDialogComponent {
  factura?: IFactura;

  constructor(protected facturaService: FacturaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.facturaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
