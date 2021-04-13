import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOrdar } from 'app/shared/model/ordar.model';
import { OrdarService } from './ordar.service';

@Component({
  templateUrl: './ordar-delete-dialog.component.html',
})
export class OrdarDeleteDialogComponent {
  ordar?: IOrdar;

  constructor(protected ordarService: OrdarService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ordarService.delete(id).subscribe(() => {
      this.eventManager.broadcast('ordarListModification');
      this.activeModal.close();
    });
  }
}
