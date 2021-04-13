import { Moment } from 'moment';
import { ICustomer } from 'app/shared/model/customer.model';
import { IOrdar } from 'app/shared/model/ordar.model';
import { AvailableStatus } from 'app/shared/model/enumerations/available-status.model';

export interface IProduct {
  id?: number;
  name?: string;
  brand?: string;
  status?: AvailableStatus;
  ordarDate?: Moment;
  price?: number;
  quantity?: number;
  customers?: ICustomer[];
  ordars?: IOrdar[];
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string,
    public brand?: string,
    public status?: AvailableStatus,
    public ordarDate?: Moment,
    public price?: number,
    public quantity?: number,
    public customers?: ICustomer[],
    public ordars?: IOrdar[]
  ) {}
}
