import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CitaDetailComponent } from './cita-detail.component';

describe('Cita Management Detail Component', () => {
  let comp: CitaDetailComponent;
  let fixture: ComponentFixture<CitaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CitaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cita: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CitaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CitaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cita on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cita).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
