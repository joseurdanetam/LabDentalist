import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IIntervencion, Intervencion } from '../intervencion.model';
import { IntervencionService } from '../service/intervencion.service';
import { ICita } from 'app/entities/cita/cita.model';
import { CitaService } from 'app/entities/cita/service/cita.service';
import { IFactura } from 'app/entities/factura/factura.model';
import { FacturaService } from 'app/entities/factura/service/factura.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';

@Component({
  selector: 'jhi-intervencion-update',
  templateUrl: './intervencion-update.component.html',
})
export class IntervencionUpdateComponent implements OnInit {
  isSaving = false;

  citasSharedCollection: ICita[] = [];
  facturasSharedCollection: IFactura[] = [];
  clientesSharedCollection: ICliente[] = [];

  editForm = this.fb.group({
    id: [],
    titulo: new FormControl('', Validators.compose([Validators.required, Validators.minLength(3), Validators.maxLength(50)])),
    precioUnitario: new FormControl(
      '',
      Validators.compose([
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(8),
        Validators.pattern('[1-9]\\d{0,5}(\\.\\d{1,2})?'),
      ])
    ),
  });

  constructor(
    protected intervencionService: IntervencionService,
    protected citaService: CitaService,
    protected facturaService: FacturaService,
    protected clienteService: ClienteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ intervencion }) => {
      this.updateForm(intervencion);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const intervencion = this.createFromForm();
    if (intervencion.id !== undefined) {
      this.subscribeToSaveResponse(this.intervencionService.update(intervencion));
    } else {
      this.subscribeToSaveResponse(this.intervencionService.create(intervencion));
    }
  }

  trackCitaById(index: number, item: ICita): number {
    return item.id!;
  }

  trackFacturaById(index: number, item: IFactura): number {
    return item.id!;
  }

  trackClienteById(index: number, item: ICliente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIntervencion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(intervencion: IIntervencion): void {
    this.editForm.patchValue({
      id: intervencion.id,
      titulo: intervencion.titulo,
      precioUnitario: intervencion.precioUnitario,
    });

    this.citasSharedCollection = this.citaService.addCitaToCollectionIfMissing(this.citasSharedCollection, intervencion.cita);
    this.facturasSharedCollection = this.facturaService.addFacturaToCollectionIfMissing(
      this.facturasSharedCollection,
      intervencion.factura
    );
    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing(
      this.clientesSharedCollection,
      intervencion.cliente
    );
  }

  protected createFromForm(): IIntervencion {
    return {
      ...new Intervencion(),
      id: this.editForm.get(['id'])!.value,
      titulo: this.editForm.get(['titulo'])!.value,
      precioUnitario: this.editForm.get(['precioUnitario'])!.value,
    };
  }
}
