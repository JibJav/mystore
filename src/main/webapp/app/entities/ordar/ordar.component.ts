import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrdar } from 'app/shared/model/ordar.model';
import { OrdarService } from './ordar.service';
import { OrdarDeleteDialogComponent } from './ordar-delete-dialog.component';

@Component({
  selector: 'jhi-ordar',
  templateUrl: './ordar.component.html',
})
export class OrdarComponent implements OnInit, OnDestroy {
  ordars?: IOrdar[];
  eventSubscriber?: Subscription;

  constructor(protected ordarService: OrdarService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.ordarService.query().subscribe((res: HttpResponse<IOrdar[]>) => (this.ordars = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInOrdars();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IOrdar): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInOrdars(): void {
    this.eventSubscriber = this.eventManager.subscribe('ordarListModification', () => this.loadAll());
  }

  delete(ordar: IOrdar): void {
    const modalRef = this.modalService.open(OrdarDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ordar = ordar;
  }
}
