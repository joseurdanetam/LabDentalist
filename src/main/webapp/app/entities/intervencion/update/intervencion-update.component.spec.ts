import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IntervencionService } from '../service/intervencion.service';
import { IIntervencion, Intervencion } from '../intervencion.model';
import { ICita } from 'app/entities/cita/cita.model';
import { CitaService } from 'app/entities/cita/service/cita.service';
import { IFactura } from 'app/entities/factura/factura.model';
import { FacturaService } from 'app/entities/factura/service/factura.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';

import { IntervencionUpdateComponent } from './intervencion-update.component';

describe('Intervencion Management Update Component', () => {
  let comp: IntervencionUpdateComponent;
  let fixture: ComponentFixture<IntervencionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let intervencionService: IntervencionService;
  let citaService: CitaService;
  let facturaService: FacturaService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IntervencionUpdateComponent],
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
      .overrideTemplate(IntervencionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IntervencionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    intervencionService = TestBed.inject(IntervencionService);
    citaService = TestBed.inject(CitaService);
    facturaService = TestBed.inject(FacturaService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cita query and add missing value', () => {
      const intervencion: IIntervencion = { id: 456 };
      const cita: ICita = { id: 27856 };
      intervencion.cita = cita;

      const citaCollection: ICita[] = [{ id: 36606 }];
      jest.spyOn(citaService, 'query').mockReturnValue(of(new HttpResponse({ body: citaCollection })));
      const additionalCitas = [cita];
      const expectedCollection: ICita[] = [...additionalCitas, ...citaCollection];
      jest.spyOn(citaService, 'addCitaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ intervencion });
      comp.ngOnInit();

      expect(citaService.query).toHaveBeenCalled();
      expect(citaService.addCitaToCollectionIfMissing).toHaveBeenCalledWith(citaCollection, ...additionalCitas);
      expect(comp.citasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Factura query and add missing value', () => {
      const intervencion: IIntervencion = { id: 456 };
      const factura: IFactura = { id: 77669 };
      intervencion.factura = factura;

      const facturaCollection: IFactura[] = [{ id: 41358 }];
      jest.spyOn(facturaService, 'query').mockReturnValue(of(new HttpResponse({ body: facturaCollection })));
      const additionalFacturas = [factura];
      const expectedCollection: IFactura[] = [...additionalFacturas, ...facturaCollection];
      jest.spyOn(facturaService, 'addFacturaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ intervencion });
      comp.ngOnInit();

      expect(facturaService.query).toHaveBeenCalled();
      expect(facturaService.addFacturaToCollectionIfMissing).toHaveBeenCalledWith(facturaCollection, ...additionalFacturas);
      expect(comp.facturasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Cliente query and add missing value', () => {
      const intervencion: IIntervencion = { id: 456 };
      const cliente: ICliente = { id: 23729 };
      intervencion.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 78187 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ intervencion });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(clienteCollection, ...additionalClientes);
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const intervencion: IIntervencion = { id: 456 };
      const cita: ICita = { id: 98135 };
      intervencion.cita = cita;
      const factura: IFactura = { id: 46730 };
      intervencion.factura = factura;
      const cliente: ICliente = { id: 31694 };
      intervencion.cliente = cliente;

      activatedRoute.data = of({ intervencion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(intervencion));
      expect(comp.citasSharedCollection).toContain(cita);
      expect(comp.facturasSharedCollection).toContain(factura);
      expect(comp.clientesSharedCollection).toContain(cliente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Intervencion>>();
      const intervencion = { id: 123 };
      jest.spyOn(intervencionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ intervencion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: intervencion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(intervencionService.update).toHaveBeenCalledWith(intervencion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Intervencion>>();
      const intervencion = new Intervencion();
      jest.spyOn(intervencionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ intervencion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: intervencion }));
      saveSubject.complete();

      // THEN
      expect(intervencionService.create).toHaveBeenCalledWith(intervencion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Intervencion>>();
      const intervencion = { id: 123 };
      jest.spyOn(intervencionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ intervencion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(intervencionService.update).toHaveBeenCalledWith(intervencion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCitaById', () => {
      it('Should return tracked Cita primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCitaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFacturaById', () => {
      it('Should return tracked Factura primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFacturaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackClienteById', () => {
      it('Should return tracked Cliente primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClienteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
