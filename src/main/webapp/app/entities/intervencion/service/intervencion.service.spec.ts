import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIntervencion, Intervencion } from '../intervencion.model';

import { IntervencionService } from './intervencion.service';

describe('Intervencion Service', () => {
  let service: IntervencionService;
  let httpMock: HttpTestingController;
  let elemDefault: IIntervencion;
  let expectedResult: IIntervencion | IIntervencion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IntervencionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      titulo: 'AAAAAAA',
      precioUnitario: 0,
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

    it('should create a Intervencion', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Intervencion()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Intervencion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          titulo: 'BBBBBB',
          precioUnitario: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Intervencion', () => {
      const patchObject = Object.assign(
        {
          titulo: 'BBBBBB',
        },
        new Intervencion()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Intervencion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          titulo: 'BBBBBB',
          precioUnitario: 1,
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

    it('should delete a Intervencion', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addIntervencionToCollectionIfMissing', () => {
      it('should add a Intervencion to an empty array', () => {
        const intervencion: IIntervencion = { id: 123 };
        expectedResult = service.addIntervencionToCollectionIfMissing([], intervencion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(intervencion);
      });

      it('should not add a Intervencion to an array that contains it', () => {
        const intervencion: IIntervencion = { id: 123 };
        const intervencionCollection: IIntervencion[] = [
          {
            ...intervencion,
          },
          { id: 456 },
        ];
        expectedResult = service.addIntervencionToCollectionIfMissing(intervencionCollection, intervencion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Intervencion to an array that doesn't contain it", () => {
        const intervencion: IIntervencion = { id: 123 };
        const intervencionCollection: IIntervencion[] = [{ id: 456 }];
        expectedResult = service.addIntervencionToCollectionIfMissing(intervencionCollection, intervencion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(intervencion);
      });

      it('should add only unique Intervencion to an array', () => {
        const intervencionArray: IIntervencion[] = [{ id: 123 }, { id: 456 }, { id: 11234 }];
        const intervencionCollection: IIntervencion[] = [{ id: 123 }];
        expectedResult = service.addIntervencionToCollectionIfMissing(intervencionCollection, ...intervencionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const intervencion: IIntervencion = { id: 123 };
        const intervencion2: IIntervencion = { id: 456 };
        expectedResult = service.addIntervencionToCollectionIfMissing([], intervencion, intervencion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(intervencion);
        expect(expectedResult).toContain(intervencion2);
      });

      it('should accept null and undefined values', () => {
        const intervencion: IIntervencion = { id: 123 };
        expectedResult = service.addIntervencionToCollectionIfMissing([], null, intervencion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(intervencion);
      });

      it('should return initial array if no Intervencion is added', () => {
        const intervencionCollection: IIntervencion[] = [{ id: 123 }];
        expectedResult = service.addIntervencionToCollectionIfMissing(intervencionCollection, undefined, null);
        expect(expectedResult).toEqual(intervencionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
