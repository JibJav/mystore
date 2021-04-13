import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IOrdar } from 'app/shared/model/ordar.model';

type EntityResponseType = HttpResponse<IOrdar>;
type EntityArrayResponseType = HttpResponse<IOrdar[]>;

@Injectable({ providedIn: 'root' })
export class OrdarService {
  public resourceUrl = SERVER_API_URL + 'api/ordars';

  constructor(protected http: HttpClient) {}

  create(ordar: IOrdar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ordar);
    return this.http
      .post<IOrdar>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(ordar: IOrdar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ordar);
    return this.http
      .put<IOrdar>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOrdar>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrdar[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(ordar: IOrdar): IOrdar {
    const copy: IOrdar = Object.assign({}, ordar, {
      ordarDate: ordar.ordarDate && ordar.ordarDate.isValid() ? ordar.ordarDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.ordarDate = res.body.ordarDate ? moment(res.body.ordarDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((ordar: IOrdar) => {
        ordar.ordarDate = ordar.ordarDate ? moment(ordar.ordarDate) : undefined;
      });
    }
    return res;
  }
}
