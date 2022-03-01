import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HistorialDetailComponent } from './historial-detail.component';

describe('Historial Management Detail Component', () => {
  let comp: HistorialDetailComponent;
  let fixture: ComponentFixture<HistorialDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HistorialDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ historial: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HistorialDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HistorialDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load historial on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.historial).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
