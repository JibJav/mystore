import { Moment } from 'moment';
import { IProduct } from 'app/shared/model/product.model';
import { OrdarStatus } from 'app/shared/model/enumerations/ordar-status.model';

export interface IOrdar {
  id?: number;
  ordarwithname?: string;
  ordarId?: number;
  status?: OrdarStatus;
  ordarDate?: Moment;
  amount?: number;
  totalItems?: number;
  userLogin?: string;
  userId?: number;
  products?: IProduct[];
  storeId?: number;
  customerId?: number;
}

export class Ordar implements IOrdar {
  constructor(
    public id?: number,
    public ordarwithname?: string,
    public ordarId?: number,
    public status?: OrdarStatus,
    public ordarDate?: Moment,
    public amount?: number,
    public totalItems?: number,
    public userLogin?: string,
    public userId?: number,
    public products?: IProduct[],
    public storeId?: number,
    public customerId?: number
  ) {}
}
