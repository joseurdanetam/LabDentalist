import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ICita } from 'app/entities/cita/cita.model';
import { IFactura } from 'app/entities/factura/factura.model';
import { IReceta } from 'app/entities/receta/receta.model';

import { IHistorial } from '../historial.model';
import { HistorialService } from '../service/historial.service';

@Component({
  selector: 'jhi-historial-detail',
  templateUrl: './historial-detail.component.html',
})
export class HistorialDetailComponent implements OnInit {
  // Receta
  historial: IHistorial | null = null;
  recetas?: IReceta[];
  isLoading = false;
  itemsPerPage = ITEMS_PER_PAGE;
  pageReceta?: number;
  totalItemsReceta = 0;
  predicateReceta!: string;
  ascendingReceta!: boolean;
  ngbPaginationPageReceta = 1;

  // Factura
  factura?: IFactura[] | null = null;
  pageFactura?: number;
  predicateFactura!: string;
  ascendingFactura!: boolean;
  totalItemsFactura = 0;
  ngbPaginationPageFactura = 1;

  // Cita
  citas?: ICita[];
  totalItemsCita = 0;
  pageCita?: number;
  predicateCita!: string;
  ascendingCita!: boolean;
  ngbPaginationPageCita = 1;

  constructor(protected historialService: HistorialService, protected activatedRoute: ActivatedRoute, protected router: Router) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ historial }) => {
      this.historial = historial;
    });
    this.loadTableReceta();
    this.loadtableFactura();
    this.loadTables();
  }

  previousState(): void {
    window.history.back();
  }

  loadTables(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.pageCita ?? 1;

    if (this.historial?.cliente?.id) {
      this.historialService
        .findAllByClienteId(this.historial.cliente.id, {
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sortCita(),
        })
        .subscribe({
          next: (res: HttpResponse<ICita[]>) => {
            this.isLoading = false;
            this.onSuccessCita(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          error: () => {
            this.isLoading = false;
            this.onErrorCita();
          },
        });
    }
  }

  loadTableReceta(pageReceta?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = pageReceta ?? this.pageReceta ?? 1;
    if (this.historial?.cliente?.id) {
      this.historialService
        .searchRecetaById(this.historial.cliente.id, {
          pageReceta: pageToLoad - 1,
          size: this.itemsPerPage,
          sortRecetas: this.sortRecetas(),
        })
        .subscribe({
          next: (res: HttpResponse<IReceta[]>) => {
            this.isLoading = false;
            this.onSuccessRecetas(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          error: () => {
            this.isLoading = false;
            this.onErrorRecetas();
          },
        });
    }
  }

  loadtableFactura(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.pageFactura ?? 1;

    if (this.historial?.cliente?.id) {
      this.historialService
        .findAllByClienteIdFactura(this.historial.cliente.id, {
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sortFactura(),
        })
        .subscribe({
          next: (res: HttpResponse<IFactura[]>) => {
            this.isLoading = false;
            this.onSuccessFactura(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          error: () => {
            this.isLoading = false;
            this.onErrorFactura();
          },
        });
    }
  }

  protected sortCita(): string[] {
    const result = [this.predicateCita + ',' + (this.ascendingCita ? ASC : DESC)];
    if (this.predicateCita !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected sortRecetas(): string[] {
    const result = [this.predicateReceta + ',' + (this.ascendingReceta ? ASC : DESC)];
    if (this.predicateReceta !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected sortFactura(): string[] {
    const result = [this.predicateFactura + ',' + (this.ascendingFactura ? ASC : DESC)];
    if (this.predicateFactura !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccessCita(data: ICita[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItemsCita = Number(headers.get('X-Total-Count'));
    this.pageCita = page;
    if (navigate && this.historial?.id) {
      this.router.navigate([`historial/${this.historial.id}/view`], {
        queryParams: {
          page: this.pageCita,
          size: this.itemsPerPage,
          sort: this.predicateCita + ',' + (this.ascendingCita ? ASC : DESC),
        },
      });
    }
    this.citas = data ?? [];
    this.ngbPaginationPageCita = this.pageCita;
  }

  protected onSuccessRecetas(data: IReceta[] | null, headers: HttpHeaders, pageReceta: number, navigate: boolean): void {
    this.totalItemsReceta = Number(headers.get('X-Total-Count'));
    this.pageReceta = pageReceta;
    if (navigate && this.historial?.id) {
      this.router.navigate([`historial/${this.historial.id}/view`], {
        queryParams: {
          page: this.pageReceta,
          size: this.itemsPerPage,
          sortRecetas: this.predicateReceta + ',' + (this.ascendingReceta ? ASC : DESC),
        },
      });
    }
    this.recetas = data ?? [];
    this.ngbPaginationPageReceta = this.pageReceta;
  }

  protected onSuccessFactura(data: IFactura[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItemsFactura = Number(headers.get('X-Total-Count'));
    this.pageFactura = page;
    if (navigate && this.historial?.id) {
      this.router.navigate([`historial/${this.historial.id}/view`], {
        queryParams: {
          page: this.pageFactura,
          size: this.itemsPerPage,
          sort: this.predicateFactura + ',' + (this.ascendingFactura ? ASC : DESC),
        },
      });
    }
    this.factura = data ?? [];
    this.ngbPaginationPageFactura = this.pageFactura;
  }

  protected onErrorCita(): void {
    this.ngbPaginationPageCita = this.pageCita ?? 1;
  }

  protected onErrorRecetas(): void {
    this.ngbPaginationPageReceta = this.pageReceta ?? 1;
  }

  protected onErrorFactura(): void {
    this.ngbPaginationPageFactura = this.pageFactura ?? 1;
  }
}
