import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHistorial, Historial } from '../historial.model';

import { HistorialService } from './historial.service';

describe('Historial Service', () => {
  let service: HistorialService;
  let httpMock: HttpTestingController;
  let elemDefault: IHistorial;
  let expectedResult: IHistorial | IHistorial[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HistorialService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Historial', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Historial()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Historial', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Historial', () => {
      const patchObject = Object.assign({}, new Historial());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Historial', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Historial', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addHistorialToCollectionIfMissing', () => {
      it('should add a Historial to an empty array', () => {
        const historial: IHistorial = { id: 123 };
        expectedResult = service.addHistorialToCollectionIfMissing([], historial);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(historial);
      });

      it('should not add a Historial to an array that contains it', () => {
        const historial: IHistorial = { id: 123 };
        const historialCollection: IHistorial[] = [
          {
            ...historial,
          },
          { id: 456 },
        ];
        expectedResult = service.addHistorialToCollectionIfMissing(historialCollection, historial);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Historial to an array that doesn't contain it", () => {
        const historial: IHistorial = { id: 123 };
        const historialCollection: IHistorial[] = [{ id: 456 }];
        expectedResult = service.addHistorialToCollectionIfMissing(historialCollection, historial);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(historial);
      });

      it('should add only unique Historial to an array', () => {
        const historialArray: IHistorial[] = [{ id: 123 }, { id: 456 }, { id: 5098 }];
        const historialCollection: IHistorial[] = [{ id: 123 }];
        expectedResult = service.addHistorialToCollectionIfMissing(historialCollection, ...historialArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const historial: IHistorial = { id: 123 };
        const historial2: IHistorial = { id: 456 };
        expectedResult = service.addHistorialToCollectionIfMissing([], historial, historial2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(historial);
        expect(expectedResult).toContain(historial2);
      });

      it('should accept null and undefined values', () => {
        const historial: IHistorial = { id: 123 };
        expectedResult = service.addHistorialToCollectionIfMissing([], null, historial, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(historial);
      });

      it('should return initial array if no Historial is added', () => {
        const historialCollection: IHistorial[] = [{ id: 123 }];
        expectedResult = service.addHistorialToCollectionIfMissing(historialCollection, undefined, null);
        expect(expectedResult).toEqual(historialCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
