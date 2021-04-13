import { Moment } from 'moment';
import { IOrdar } from 'app/shared/model/ordar.model';
import { IProduct } from 'app/shared/model/product.model';

export interface ICustomer {
  id?: number;
  name?: string;
  address?: string;
  timestamp?: Moment;
  userLogin?: string;
  userId?: number;
  ordars?: IOrdar[];
  products?: IProduct[];
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public name?: string,
    public address?: string,
    public timestamp?: Moment,
    public userLogin?: string,
    public userId?: number,
    public ordars?: IOrdar[],
    public products?: IProduct[]
  ) {}
}
