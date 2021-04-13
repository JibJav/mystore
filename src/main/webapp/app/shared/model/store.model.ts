import { Moment } from 'moment';
import { IOrdar } from 'app/shared/model/ordar.model';

export interface IStore {
  id?: number;
  name?: string;
  address?: string;
  timestamp?: Moment;
  ordars?: IOrdar[];
}

export class Store implements IStore {
  constructor(public id?: number, public name?: string, public address?: string, public timestamp?: Moment, public ordars?: IOrdar[]) {}
}
