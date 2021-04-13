import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IOrdar, Ordar } from 'app/shared/model/ordar.model';
import { OrdarService } from './ordar.service';
import { OrdarComponent } from './ordar.component';
import { OrdarDetailComponent } from './ordar-detail.component';
import { OrdarUpdateComponent } from './ordar-update.component';

@Injectable({ providedIn: 'root' })
export class OrdarResolve implements Resolve<IOrdar> {
  constructor(private service: OrdarService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrdar> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((ordar: HttpResponse<Ordar>) => {
          if (ordar.body) {
            return of(ordar.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ordar());
  }
}

export const ordarRoute: Routes = [
  {
    path: '',
    component: OrdarComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Ordars',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrdarDetailComponent,
    resolve: {
      ordar: OrdarResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Ordars',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrdarUpdateComponent,
    resolve: {
      ordar: OrdarResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Ordars',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrdarUpdateComponent,
    resolve: {
      ordar: OrdarResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Ordars',
    },
    canActivate: [UserRouteAccessService],
  },
];
