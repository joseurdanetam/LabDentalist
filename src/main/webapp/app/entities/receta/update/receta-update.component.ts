import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_FORMAT } from 'app/config/input.constants';

import { IReceta, Receta } from '../receta.model';
import { RecetaService } from '../service/receta.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';

@Component({
  selector: 'jhi-receta-update',
  templateUrl: './receta-update.component.html',
})
export class RecetaUpdateComponent implements OnInit {
  isSaving = false;
  random = Math.round(new Date().getTime() / 1000);
  clientesSharedCollection: ICliente[] = [];

  editForm = this.fb.group({
    id: [],
    cliente: new FormControl('', Validators.compose([Validators.required])),
    fechaEmision: new FormControl('', Validators.compose([Validators.required])),
    fechaVencimiento: new FormControl('', Validators.compose([Validators.required])),
    numeroReceta: new FormControl('', Validators.compose([Validators.required])),
    descripcion: new FormControl(''),
  });

  constructor(
    protected recetaService: RecetaService,
    protected clienteService: ClienteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ receta }) => {
      if (receta.id === undefined) {
        const today = dayjs().startOf('day');
        receta.fechaEmision = today;
        receta.fechaVencimiento = dayjs().month(today.month() + 1);
      }

      this.updateForm(receta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const receta = this.createFromForm();
    if (receta.id !== undefined) {
      this.subscribeToSaveResponse(this.recetaService.update(receta));
    } else {
      this.subscribeToSaveResponse(this.recetaService.create(receta));
    }
  }

  trackClienteById(index: number, item: ICliente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReceta>>): void {
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

  protected updateForm(receta: IReceta): void {
    this.editForm.patchValue({
      id: receta.id,
      numeroReceta: receta.numeroReceta,
      fechaEmision: receta.fechaEmision ? receta.fechaEmision.format(DATE_FORMAT) : null,
      fechaVencimiento: receta.fechaVencimiento ? receta.fechaVencimiento.format(DATE_FORMAT) : null,
      descripcion: receta.descripcion,
      cliente: receta.cliente,
    });

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing(this.clientesSharedCollection, receta.cliente);
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing(clientes, this.editForm.get('cliente')!.value))
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));
  }

  protected createFromForm(): IReceta {
    return {
      ...new Receta(),
      id: this.editForm.get(['id'])!.value,
      numeroReceta: this.editForm.get(['numeroReceta'])!.value,
      fechaEmision: this.editForm.get(['fechaEmision'])!.value ? dayjs(this.editForm.get(['fechaEmision'])!.value, DATE_FORMAT) : undefined,
      fechaVencimiento: this.editForm.get(['fechaVencimiento'])!.value
        ? dayjs(this.editForm.get(['fechaVencimiento'])!.value, DATE_FORMAT)
        : undefined,
      descripcion: this.editForm.get(['descripcion'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
    };
  }
}
