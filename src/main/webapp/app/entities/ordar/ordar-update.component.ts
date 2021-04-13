import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IOrdar, Ordar } from 'app/shared/model/ordar.model';
import { OrdarService } from './ordar.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product/product.service';
import { IStore } from 'app/shared/model/store.model';
import { StoreService } from 'app/entities/store/store.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer/customer.service';

type SelectableEntity = IUser | IProduct | IStore | ICustomer;

@Component({
  selector: 'jhi-ordar-update',
  templateUrl: './ordar-update.component.html',
})
export class OrdarUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  products: IProduct[] = [];
  stores: IStore[] = [];
  customers: ICustomer[] = [];

  editForm = this.fb.group({
    id: [],
    ordarwithname: [null, [Validators.required]],
    ordarId: [null, [Validators.required]],
    status: [],
    ordarDate: [],
    amount: [],
    totalItems: [],
    userId: [null, Validators.required],
    products: [],
    storeId: [],
    customerId: [],
  });

  constructor(
    protected ordarService: OrdarService,
    protected userService: UserService,
    protected productService: ProductService,
    protected storeService: StoreService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordar }) => {
      if (!ordar.id) {
        const today = moment().startOf('day');
        ordar.ordarDate = today;
      }

      this.updateForm(ordar);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.productService.query().subscribe((res: HttpResponse<IProduct[]>) => (this.products = res.body || []));

      this.storeService.query().subscribe((res: HttpResponse<IStore[]>) => (this.stores = res.body || []));

      this.customerService.query().subscribe((res: HttpResponse<ICustomer[]>) => (this.customers = res.body || []));
    });
  }

  updateForm(ordar: IOrdar): void {
    this.editForm.patchValue({
      id: ordar.id,
      ordarwithname: ordar.ordarwithname,
      ordarId: ordar.ordarId,
      status: ordar.status,
      ordarDate: ordar.ordarDate ? ordar.ordarDate.format(DATE_TIME_FORMAT) : null,
      amount: ordar.amount,
      totalItems: ordar.totalItems,
      userId: ordar.userId,
      products: ordar.products,
      storeId: ordar.storeId,
      customerId: ordar.customerId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ordar = this.createFromForm();
    if (ordar.id !== undefined) {
      this.subscribeToSaveResponse(this.ordarService.update(ordar));
    } else {
      this.subscribeToSaveResponse(this.ordarService.create(ordar));
    }
  }

  private createFromForm(): IOrdar {
    return {
      ...new Ordar(),
      id: this.editForm.get(['id'])!.value,
      ordarwithname: this.editForm.get(['ordarwithname'])!.value,
      ordarId: this.editForm.get(['ordarId'])!.value,
      status: this.editForm.get(['status'])!.value,
      ordarDate: this.editForm.get(['ordarDate'])!.value ? moment(this.editForm.get(['ordarDate'])!.value, DATE_TIME_FORMAT) : undefined,
      amount: this.editForm.get(['amount'])!.value,
      totalItems: this.editForm.get(['totalItems'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      products: this.editForm.get(['products'])!.value,
      storeId: this.editForm.get(['storeId'])!.value,
      customerId: this.editForm.get(['customerId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrdar>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: IProduct[], option: IProduct): IProduct {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
