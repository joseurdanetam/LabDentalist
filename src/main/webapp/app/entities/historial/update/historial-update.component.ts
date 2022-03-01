import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IHistorial, Historial } from '../historial.model';
import { HistorialService } from '../service/historial.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';

@Component({
  selector: 'jhi-historial-update',
  templateUrl: './historial-update.component.html',
})
export class HistorialUpdateComponent implements OnInit {
  isSaving = false;

  clientesCollection: ICliente[] = [];

  editForm = this.fb.group({
    id: [],
    cliente: [],
  });

  constructor(
    protected historialService: HistorialService,
    protected clienteService: ClienteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ historial }) => {
      this.updateForm(historial);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const historial = this.createFromForm();
    if (historial.id !== undefined) {
      this.subscribeToSaveResponse(this.historialService.update(historial));
    } else {
      this.subscribeToSaveResponse(this.historialService.create(historial));
    }
  }

  trackClienteById(index: number, item: ICliente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHistorial>>): void {
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

  protected updateForm(historial: IHistorial): void {
    this.editForm.patchValue({
      id: historial.id,
      cliente: historial.cliente,
    });

    this.clientesCollection = this.clienteService.addClienteToCollectionIfMissing(this.clientesCollection, historial.cliente);
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query({ filter: 'historial-is-null' })
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing(clientes, this.editForm.get('cliente')!.value))
      )
      .subscribe((clientes: ICliente[]) => (this.clientesCollection = clientes));
  }

  protected createFromForm(): IHistorial {
    return {
      ...new Historial(),
      id: this.editForm.get(['id'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
    };
  }
}
