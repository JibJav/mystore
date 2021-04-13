import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { OrdarService } from 'app/entities/ordar/ordar.service';
import { IOrdar, Ordar } from 'app/shared/model/ordar.model';
import { OrdarStatus } from 'app/shared/model/enumerations/ordar-status.model';

describe('Service Tests', () => {
  describe('Ordar Service', () => {
    let injector: TestBed;
    let service: OrdarService;
    let httpMock: HttpTestingController;
    let elemDefault: IOrdar;
    let expectedResult: IOrdar | IOrdar[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(OrdarService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Ordar(0, 'AAAAAAA', 0, OrdarStatus.DELIVERED, currentDate, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            ordarDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Ordar', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            ordarDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            ordarDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Ordar()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Ordar', () => {
        const returnedFromService = Object.assign(
          {
            ordarwithname: 'BBBBBB',
            ordarId: 1,
            status: 'BBBBBB',
            ordarDate: currentDate.format(DATE_TIME_FORMAT),
            amount: 1,
            totalItems: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            ordarDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Ordar', () => {
        const returnedFromService = Object.assign(
          {
            ordarwithname: 'BBBBBB',
            ordarId: 1,
            status: 'BBBBBB',
            ordarDate: currentDate.format(DATE_TIME_FORMAT),
            amount: 1,
            totalItems: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            ordarDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Ordar', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
