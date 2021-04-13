import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MystoreTestModule } from '../../../test.module';
import { OrdarUpdateComponent } from 'app/entities/ordar/ordar-update.component';
import { OrdarService } from 'app/entities/ordar/ordar.service';
import { Ordar } from 'app/shared/model/ordar.model';

describe('Component Tests', () => {
  describe('Ordar Management Update Component', () => {
    let comp: OrdarUpdateComponent;
    let fixture: ComponentFixture<OrdarUpdateComponent>;
    let service: OrdarService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MystoreTestModule],
        declarations: [OrdarUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(OrdarUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrdarUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(OrdarService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Ordar(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Ordar();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
