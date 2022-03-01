import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IntervencionDetailComponent } from './intervencion-detail.component';

describe('Intervencion Management Detail Component', () => {
  let comp: IntervencionDetailComponent;
  let fixture: ComponentFixture<IntervencionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IntervencionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ intervencion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IntervencionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IntervencionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load intervencion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.intervencion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
