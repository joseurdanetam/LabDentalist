import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReceta } from '../receta.model';

@Component({
  selector: 'jhi-receta-detail',
  templateUrl: './receta-detail.component.html',
})
export class RecetaDetailComponent implements OnInit {
  receta: IReceta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ receta }) => {
      this.receta = receta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
