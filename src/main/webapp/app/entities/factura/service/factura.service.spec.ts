import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFactura, Factura } from '../factura.model';

import { FacturaService } from './factura.service';

describe('Factura Service', () => {
  let service: FacturaService;
  let httpMock: HttpTestingController;
  let elemDefault: IFactura;
  let expectedResult: IFactura | IFactura[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FacturaService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      numeroFactura: 0,
      fechaEmision: currentDate,
      tipoPago: 'AAAAAAA',
      fechaVencimiento: currentDate,
      decripcion: 'AAAAAAA',
      subtotal: 0,
      total: 0,
      importePagado: 0,
      importeAPagar: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaEmision: currentDate.format(DATE_TIME_FORMAT),
          fechaVencimiento: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Factura', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaEmision: currentDate.format(DATE_TIME_FORMAT),
          fechaVencimiento: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaEmision: currentDate,
          fechaVencimiento: currentDate,
        },
        returnedFromService
      );

      service.create(new Factura()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Factura', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numeroFactura: 1,
          fechaEmision: currentDate.format(DATE_TIME_FORMAT),
          tipoPago: 'BBBBBB',
          fechaVencimiento: currentDate.format(DATE_TIME_FORMAT),
          decripcion: 'BBBBBB',
          subtotal: 1,
          total: 1,
          importePagado: 1,
          importeAPagar: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaEmision: currentDate,
          fechaVencimiento: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Factura', () => {
      const patchObject = Object.assign(
        {
          numeroFactura: 1,
          fechaEmision: currentDate.format(DATE_TIME_FORMAT),
          fechaVencimiento: currentDate.format(DATE_TIME_FORMAT),
          total: 1,
        },
        new Factura()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaEmision: currentDate,
          fechaVencimiento: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Factura', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numeroFactura: 1,
          fechaEmision: currentDate.format(DATE_TIME_FORMAT),
          tipoPago: 'BBBBBB',
          fechaVencimiento: currentDate.format(DATE_TIME_FORMAT),
          decripcion: 'BBBBBB',
          subtotal: 1,
          total: 1,
          importePagado: 1,
          importeAPagar: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaEmision: currentDate,
          fechaVencimiento: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Factura', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFacturaToCollectionIfMissing', () => {
      it('should add a Factura to an empty array', () => {
        const factura: IFactura = { id: 123 };
        expectedResult = service.addFacturaToCollectionIfMissing([], factura);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factura);
      });

      it('should not add a Factura to an array that contains it', () => {
        const factura: IFactura = { id: 123 };
        const facturaCollection: IFactura[] = [
          {
            ...factura,
          },
          { id: 456 },
        ];
        expectedResult = service.addFacturaToCollectionIfMissing(facturaCollection, factura);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Factura to an array that doesn't contain it", () => {
        const factura: IFactura = { id: 123 };
        const facturaCollection: IFactura[] = [{ id: 456 }];
        expectedResult = service.addFacturaToCollectionIfMissing(facturaCollection, factura);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factura);
      });

      it('should add only unique Factura to an array', () => {
        const facturaArray: IFactura[] = [{ id: 123 }, { id: 456 }, { id: 99956 }];
        const facturaCollection: IFactura[] = [{ id: 123 }];
        expectedResult = service.addFacturaToCollectionIfMissing(facturaCollection, ...facturaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const factura: IFactura = { id: 123 };
        const factura2: IFactura = { id: 456 };
        expectedResult = service.addFacturaToCollectionIfMissing([], factura, factura2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factura);
        expect(expectedResult).toContain(factura2);
      });

      it('should accept null and undefined values', () => {
        const factura: IFactura = { id: 123 };
        expectedResult = service.addFacturaToCollectionIfMissing([], null, factura, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factura);
      });

      it('should return initial array if no Factura is added', () => {
        const facturaCollection: IFactura[] = [{ id: 123 }];
        expectedResult = service.addFacturaToCollectionIfMissing(facturaCollection, undefined, null);
        expect(expectedResult).toEqual(facturaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
