import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReceta, Receta } from '../receta.model';

import { RecetaService } from './receta.service';

describe('Receta Service', () => {
  let service: RecetaService;
  let httpMock: HttpTestingController;
  let elemDefault: IReceta;
  let expectedResult: IReceta | IReceta[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RecetaService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      numeroReceta: 0,
      fechaEmision: currentDate,
      fechaVencimiento: currentDate,
      descripcion: 'AAAAAAA',
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

    it('should create a Receta', () => {
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

      service.create(new Receta()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Receta', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numeroReceta: 1,
          fechaEmision: currentDate.format(DATE_TIME_FORMAT),
          fechaVencimiento: currentDate.format(DATE_TIME_FORMAT),
          descripcion: 'BBBBBB',
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

    it('should partial update a Receta', () => {
      const patchObject = Object.assign(
        {
          fechaVencimiento: currentDate.format(DATE_TIME_FORMAT),
        },
        new Receta()
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

    it('should return a list of Receta', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numeroReceta: 1,
          fechaEmision: currentDate.format(DATE_TIME_FORMAT),
          fechaVencimiento: currentDate.format(DATE_TIME_FORMAT),
          descripcion: 'BBBBBB',
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

    it('should delete a Receta', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRecetaToCollectionIfMissing', () => {
      it('should add a Receta to an empty array', () => {
        const receta: IReceta = { id: 123 };
        expectedResult = service.addRecetaToCollectionIfMissing([], receta);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(receta);
      });

      it('should not add a Receta to an array that contains it', () => {
        const receta: IReceta = { id: 123 };
        const recetaCollection: IReceta[] = [
          {
            ...receta,
          },
          { id: 456 },
        ];
        expectedResult = service.addRecetaToCollectionIfMissing(recetaCollection, receta);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Receta to an array that doesn't contain it", () => {
        const receta: IReceta = { id: 123 };
        const recetaCollection: IReceta[] = [{ id: 456 }];
        expectedResult = service.addRecetaToCollectionIfMissing(recetaCollection, receta);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(receta);
      });

      it('should add only unique Receta to an array', () => {
        const recetaArray: IReceta[] = [{ id: 123 }, { id: 456 }, { id: 79122 }];
        const recetaCollection: IReceta[] = [{ id: 123 }];
        expectedResult = service.addRecetaToCollectionIfMissing(recetaCollection, ...recetaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const receta: IReceta = { id: 123 };
        const receta2: IReceta = { id: 456 };
        expectedResult = service.addRecetaToCollectionIfMissing([], receta, receta2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(receta);
        expect(expectedResult).toContain(receta2);
      });

      it('should accept null and undefined values', () => {
        const receta: IReceta = { id: 123 };
        expectedResult = service.addRecetaToCollectionIfMissing([], null, receta, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(receta);
      });

      it('should return initial array if no Receta is added', () => {
        const recetaCollection: IReceta[] = [{ id: 123 }];
        expectedResult = service.addRecetaToCollectionIfMissing(recetaCollection, undefined, null);
        expect(expectedResult).toEqual(recetaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
