import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FacturaDetailComponent } from './factura-detail.component';

describe('Factura Management Detail Component', () => {
  let comp: FacturaDetailComponent;
  let fixture: ComponentFixture<FacturaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FacturaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ factura: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FacturaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FacturaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load factura on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.factura).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
