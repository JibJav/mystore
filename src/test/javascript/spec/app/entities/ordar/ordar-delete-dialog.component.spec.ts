import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MystoreTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { OrdarDeleteDialogComponent } from 'app/entities/ordar/ordar-delete-dialog.component';
import { OrdarService } from 'app/entities/ordar/ordar.service';

describe('Component Tests', () => {
  describe('Ordar Management Delete Component', () => {
    let comp: OrdarDeleteDialogComponent;
    let fixture: ComponentFixture<OrdarDeleteDialogComponent>;
    let service: OrdarService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MystoreTestModule],
        declarations: [OrdarDeleteDialogComponent],
      })
        .overrideTemplate(OrdarDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OrdarDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(OrdarService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
