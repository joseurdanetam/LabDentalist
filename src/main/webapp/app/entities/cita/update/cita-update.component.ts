import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICita, Cita } from '../cita.model';
import { CitaService } from '../service/cita.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { UserService } from 'app/entities/user/user.service';
import { IUser } from 'app/admin/user-management/user-management.model';

@Component({
  selector: 'jhi-cita-update',
  templateUrl: './cita-update.component.html',
})
export class CitaUpdateComponent implements OnInit {
  isSaving = false;
  clientesSharedCollection: ICliente[] = [];
  usersSharedCollection: IUser[] = [];
  today = dayjs();

  editForm = this.fb.group({
    id: new FormControl(),
    fechaEmison: new FormControl(),
    fechaCita: new FormControl(),
    descripcion: new FormControl('', Validators.compose([Validators.required])),
    user: new FormControl('', Validators.compose([Validators.required])),
    cliente: new FormControl('', Validators.compose([Validators.required])),
  });

  constructor(
    protected citaService: CitaService,
    protected clienteService: ClienteService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cita }) => {
      if (cita.id === undefined) {
        const today = dayjs();
        cita.fechaEmison = today;
        cita.fechaCita = today;
      }

      this.updateForm(cita);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cita = this.createFromForm();
    if (cita.id !== undefined) {
      this.subscribeToSaveResponse(this.citaService.update(cita));
    } else {
      this.subscribeToSaveResponse(this.citaService.create(cita));
    }
  }

  trackClienteById(index: number, item: ICliente): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICita>>): void {
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
  //TODO: añadir profesional
  protected updateForm(cita: ICita): void {
    this.editForm.patchValue({
      id: cita.id,
      fechaEmison: cita.fechaEmison ? cita.fechaEmison.format(DATE_TIME_FORMAT) : null,
      fechaCita: cita.fechaCita ? cita.fechaCita.format(DATE_TIME_FORMAT) : null,
      descripcion: cita.descripcion,
      user: cita.user,
      cliente: cita.cliente,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, cita.user);
    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing(this.clientesSharedCollection, cita.cliente);
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing(clientes, this.editForm.get('cliente')!.value))
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ICita {
    return {
      ...new Cita(),
      id: this.editForm.get(['id'])!.value,
      fechaEmison: dayjs(),
      fechaCita: this.editForm.get(['fechaCita'])!.value ? dayjs(this.editForm.get(['fechaCita'])!.value, DATE_TIME_FORMAT) : undefined,
      descripcion: this.editForm.get(['descripcion'])!.value,
      user: this.editForm.get(['user'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
    };
  }
}
