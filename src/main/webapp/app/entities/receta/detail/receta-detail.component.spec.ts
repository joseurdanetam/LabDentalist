import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RecetaDetailComponent } from './receta-detail.component';

describe('Receta Management Detail Component', () => {
  let comp: RecetaDetailComponent;
  let fixture: ComponentFixture<RecetaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RecetaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ receta: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RecetaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RecetaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load receta on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.receta).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
