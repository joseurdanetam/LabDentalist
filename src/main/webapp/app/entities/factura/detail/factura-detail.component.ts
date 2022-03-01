import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IIntervencion } from 'app/entities/intervencion/intervencion.model';
import { IntervencionService } from 'app/entities/intervencion/service/intervencion.service';

import { IFactura } from '../factura.model';

@Component({
  selector: 'jhi-factura-detail',
  templateUrl: './factura-detail.component.html',
  styleUrls: ['./factura-detail.component.scss'],
})
export class FacturaDetailComponent implements OnInit {
  factura: IFactura | null = null;
  intervenciones: IIntervencion[] | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected intervencionService: IntervencionService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factura }) => {
      this.factura = factura;
    });
    this.loadPage();
  }

  loadPage(): void {
    if (this.factura?.id) {
      this.intervencionService.getAllIntervenciones(this.factura.id).subscribe({
        next: (res: HttpResponse<IIntervencion[]>) => {
          this.intervenciones = res.body ?? [];
        },
      });
    }
  }

  print(): void {
    window.print();
  }

  previousState(): void {
    window.history.back();
  }
}
