import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_FORMAT } from 'app/config/input.constants';

import { IFactura, Factura } from '../factura.model';
import { FacturaService } from '../service/factura.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IntervencionService } from 'app/entities/intervencion/service/intervencion.service';
import { IDropdownSettings } from 'ng-multiselect-dropdown';

@Component({
  selector: 'jhi-factura-update',
  templateUrl: './factura-update.component.html',
})
export class FacturaUpdateComponent implements OnInit {
  isSaving = false;
  random = Math.round(new Date().getTime() / 1000);
  totalIn: any = [];
  selectedItems: any = [];
  totalAux = 0;

  dropdownSettings: IDropdownSettings = {
    singleSelection: false,
    idField: 'precioUnitario',
    textField: 'titulo',
    selectAllText: 'Seleccionar todos',
    unSelectAllText: 'Borrar',
    itemsShowLimit: 15,
    allowSearchFilter: true,
  };

  clientesSharedCollection: ICliente[] = [];
  intervencionesSharedCollection: any;

  editForm = this.fb.group({
    id: [],
    numeroFactura: [],
    fechaEmision: [],
    tipoPago: new FormControl('', Validators.compose([Validators.required])),
    fechaVencimiento: [],
    decripcion: new FormControl(''),
    subtotal: [],
    total: 0,
    importePagado: [],
    importeAPagar: [],
    cliente: new FormControl('', Validators.compose([Validators.required])),
    intervenciones: new FormControl('', Validators.compose([Validators.required])),
  });

  constructor(
    protected facturaService: FacturaService,
    protected intervencionService: IntervencionService,
    protected clienteService: ClienteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factura }) => {
      if (factura.id === undefined) {
        const today = dayjs().startOf('day');
        factura.fechaEmision = today;
        factura.fechaVencimiento = dayjs().day(today.day() + 15);
      }
      this.datosIntervencion();
      this.updateForm(factura);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  cambiarValor(event: any): void {
    this.sumValor();
    const mes1 = <HTMLInputElement>document.getElementById('mes1');
    if (mes1.checked) {
      this.subPlazo(1);
    }
  }

  cambiarValorAll(event: any): void {
    this.selectedItems = event;
    this.sumValor();
  }

  quitarValor(event: any): void {
    this.selectedItems = this.selectedItems.filter((intervencion: any) => intervencion !== event); // hace un filtro y deja el que no sea igual a este.
    this.sumValor();
  }

  quitarValorAll(): void {
    this.selectedItems = [];
    this.editForm.patchValue({
      total: 0,
    });
  }

  sumValor(): void {
    this.totalAux = 0;
    this.selectedItems.forEach((element: any) => {
      this.totalAux += element.precioUnitario;
    });
    this.editForm.patchValue({
      total: this.totalAux,
    });
  }

  subPlazo(val: number): void {
    this.editForm.patchValue({
      subtotal: Math.round((this.editForm.getRawValue().total / val) * 100) / 100, // getRawValue esto trae todos los datos
    });
  }

  datosIntervencion(): void {
    this.intervencionService.query({}).subscribe(data => {
      this.intervencionesSharedCollection = data.body;
    });
  }

  save(): void {
    this.isSaving = true;
    const factura = this.createFromForm();
    if (factura.id !== undefined) {
      this.subscribeToSaveResponse(this.facturaService.update(factura));
    } else {
      this.subscribeToSaveResponse(this.facturaService.create(factura));
    }
  }

  trackClienteById(index: number, item: ICliente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactura>>): void {
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

  protected updateForm(factura: IFactura): void {
    this.editForm.patchValue({
      id: factura.id,
      numeroFactura: factura.numeroFactura,
      fechaEmision: factura.fechaEmision ? factura.fechaEmision.format(DATE_FORMAT) : null,
      tipoPago: factura.tipoPago,
      fechaVencimiento: factura.fechaVencimiento ? factura.fechaVencimiento.format(DATE_FORMAT) : null,
      decripcion: factura.decripcion,
      subtotal: 0,
      total: 0,
      importePagado: 0,
      importeAPagar: 0,
      cliente: factura.cliente,
    });

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing(this.clientesSharedCollection, factura.cliente);
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

  protected createFromForm(): IFactura {
    return {
      ...new Factura(),
      id: this.editForm.get(['id'])!.value,
      numeroFactura: this.editForm.get(['numeroFactura'])!.value,
      fechaEmision: this.editForm.get(['fechaEmision'])!.value ? dayjs(this.editForm.get(['fechaEmision'])!.value, DATE_FORMAT) : undefined,
      tipoPago: this.editForm.get(['tipoPago'])!.value,
      fechaVencimiento: this.editForm.get(['fechaVencimiento'])!.value
        ? dayjs(this.editForm.get(['fechaVencimiento'])!.value, DATE_FORMAT)
        : undefined,
      decripcion: this.editForm.get(['decripcion'])!.value,
      subtotal: this.editForm.get(['subtotal'])!.value,
      total: this.editForm.get(['total'])!.value,
      importePagado: this.editForm.get(['importePagado'])!.value,
      importeAPagar: this.editForm.get(['importeAPagar'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
    };
  }
}
