import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICita, Cita } from '../cita.model';

import { CitaService } from './cita.service';

describe('Cita Service', () => {
  let service: CitaService;
  let httpMock: HttpTestingController;
  let elemDefault: ICita;
  let expectedResult: ICita | ICita[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CitaService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fechaEmison: currentDate,
      fechaCita: currentDate,
      descripcion: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaEmison: currentDate.format(DATE_TIME_FORMAT),
          fechaCita: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Cita', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaEmison: currentDate.format(DATE_TIME_FORMAT),
          fechaCita: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaEmison: currentDate,
          fechaCita: currentDate,
        },
        returnedFromService
      );

      service.create(new Cita()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cita', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaEmison: currentDate.format(DATE_TIME_FORMAT),
          fechaCita: currentDate.format(DATE_TIME_FORMAT),
          descripcion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaEmison: currentDate,
          fechaCita: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cita', () => {
      const patchObject = Object.assign(
        {
          fechaEmison: currentDate.format(DATE_TIME_FORMAT),
          fechaCita: currentDate.format(DATE_TIME_FORMAT),
        },
        new Cita()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaEmison: currentDate,
          fechaCita: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cita', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaEmison: currentDate.format(DATE_TIME_FORMAT),
          fechaCita: currentDate.format(DATE_TIME_FORMAT),
          descripcion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaEmison: currentDate,
          fechaCita: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Cita', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCitaToCollectionIfMissing', () => {
      it('should add a Cita to an empty array', () => {
        const cita: ICita = { id: 123 };
        expectedResult = service.addCitaToCollectionIfMissing([], cita);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cita);
      });

      it('should not add a Cita to an array that contains it', () => {
        const cita: ICita = { id: 123 };
        const citaCollection: ICita[] = [
          {
            ...cita,
          },
          { id: 456 },
        ];
        expectedResult = service.addCitaToCollectionIfMissing(citaCollection, cita);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cita to an array that doesn't contain it", () => {
        const cita: ICita = { id: 123 };
        const citaCollection: ICita[] = [{ id: 456 }];
        expectedResult = service.addCitaToCollectionIfMissing(citaCollection, cita);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cita);
      });

      it('should add only unique Cita to an array', () => {
        const citaArray: ICita[] = [{ id: 123 }, { id: 456 }, { id: 3952 }];
        const citaCollection: ICita[] = [{ id: 123 }];
        expectedResult = service.addCitaToCollectionIfMissing(citaCollection, ...citaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cita: ICita = { id: 123 };
        const cita2: ICita = { id: 456 };
        expectedResult = service.addCitaToCollectionIfMissing([], cita, cita2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cita);
        expect(expectedResult).toContain(cita2);
      });

      it('should accept null and undefined values', () => {
        const cita: ICita = { id: 123 };
        expectedResult = service.addCitaToCollectionIfMissing([], null, cita, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cita);
      });

      it('should return initial array if no Cita is added', () => {
        const citaCollection: ICita[] = [{ id: 123 }];
        expectedResult = service.addCitaToCollectionIfMissing(citaCollection, undefined, null);
        expect(expectedResult).toEqual(citaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
