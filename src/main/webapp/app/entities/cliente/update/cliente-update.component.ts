import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_FORMAT } from 'app/config/input.constants';

import { ICliente, Cliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';

@Component({
  selector: 'jhi-cliente-update',
  templateUrl: './cliente-update.component.html',
})
export class ClienteUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: new FormControl('', Validators.compose([Validators.required, Validators.minLength(3), Validators.maxLength(50)])),
    apellido: new FormControl('', Validators.compose([Validators.required, Validators.minLength(3), Validators.maxLength(50)])),
    dni: new FormControl('', Validators.compose([Validators.required, Validators.minLength(9), Validators.maxLength(9)])),
    sexo: new FormControl('', Validators.compose([Validators.required])),
    edad: new FormControl('', Validators.compose([Validators.required])),
    email: new FormControl('', Validators.compose([Validators.email])),
    telefono: new FormControl('', Validators.compose([Validators.required, Validators.pattern('^\\d{9}$')])),
    direccion: new FormControl('', Validators.compose([Validators.required, Validators.minLength(5), Validators.maxLength(60)])),
    observacion: new FormControl(''),
  });

  constructor(protected clienteService: ClienteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliente }) => {
      if (cliente.id === undefined) {
        const today = dayjs().startOf('day');
        cliente.edad = today;
      }

      this.updateForm(cliente);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cliente = this.createFromForm();
    if (cliente.id !== undefined) {
      this.subscribeToSaveResponse(this.clienteService.update(cliente));
    } else {
      this.subscribeToSaveResponse(this.clienteService.create(cliente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>): void {
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

  protected updateForm(cliente: ICliente): void {
    this.editForm.patchValue({
      id: cliente.id,
      nombre: cliente.nombre,
      apellido: cliente.apellido,
      dni: cliente.dni,
      sexo: cliente.sexo,
      edad: cliente.edad ? cliente.edad.format(DATE_FORMAT) : null,
      email: cliente.email,
      telefono: cliente.telefono,
      direccion: cliente.direccion,
      observacion: cliente.observacion,
    });
  }

  protected createFromForm(): ICliente {
    return {
      ...new Cliente(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      apellido: this.editForm.get(['apellido'])!.value,
      dni: this.editForm.get(['dni'])!.value,
      sexo: this.editForm.get(['sexo'])!.value,
      edad: this.editForm.get(['edad'])!.value ? dayjs(this.editForm.get(['edad'])!.value, DATE_FORMAT) : undefined,
      email: this.editForm.get(['email'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      observacion: this.editForm.get(['observacion'])!.value,
    };
  }
}
