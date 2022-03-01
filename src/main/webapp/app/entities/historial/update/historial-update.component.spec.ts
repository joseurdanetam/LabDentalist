import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HistorialService } from '../service/historial.service';
import { IHistorial, Historial } from '../historial.model';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';

import { HistorialUpdateComponent } from './historial-update.component';

describe('Historial Management Update Component', () => {
  let comp: HistorialUpdateComponent;
  let fixture: ComponentFixture<HistorialUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let historialService: HistorialService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HistorialUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(HistorialUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HistorialUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    historialService = TestBed.inject(HistorialService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call cliente query and add missing value', () => {
      const historial: IHistorial = { id: 456 };
      const cliente: ICliente = { id: 76825 };
      historial.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 99385 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const expectedCollection: ICliente[] = [cliente, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ historial });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(clienteCollection, cliente);
      expect(comp.clientesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const historial: IHistorial = { id: 456 };
      const cliente: ICliente = { id: 50989 };
      historial.cliente = cliente;

      activatedRoute.data = of({ historial });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(historial));
      expect(comp.clientesCollection).toContain(cliente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Historial>>();
      const historial = { id: 123 };
      jest.spyOn(historialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historial });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: historial }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(historialService.update).toHaveBeenCalledWith(historial);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Historial>>();
      const historial = new Historial();
      jest.spyOn(historialService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historial });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: historial }));
      saveSubject.complete();

      // THEN
      expect(historialService.create).toHaveBeenCalledWith(historial);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Historial>>();
      const historial = { id: 123 };
      jest.spyOn(historialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historial });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(historialService.update).toHaveBeenCalledWith(historial);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackClienteById', () => {
      it('Should return tracked Cliente primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClienteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
