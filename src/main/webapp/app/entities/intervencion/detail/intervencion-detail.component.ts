import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIntervencion } from '../intervencion.model';

@Component({
  selector: 'jhi-intervencion-detail',
  templateUrl: './intervencion-detail.component.html',
})
export class IntervencionDetailComponent implements OnInit {
  intervencion: IIntervencion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ intervencion }) => {
      this.intervencion = intervencion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
