import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'product',
        loadChildren: () => import('./product/product.module').then(m => m.MystoreProductModule),
      },
      {
        path: 'customer',
        loadChildren: () => import('./customer/customer.module').then(m => m.MystoreCustomerModule),
      },
      {
        path: 'store',
        loadChildren: () => import('./store/store.module').then(m => m.MystoreStoreModule),
      },
      {
        path: 'ordar',
        loadChildren: () => import('./ordar/ordar.module').then(m => m.MystoreOrdarModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class MystoreEntityModule {}
