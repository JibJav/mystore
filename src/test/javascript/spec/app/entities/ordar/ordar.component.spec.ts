import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MystoreTestModule } from '../../../test.module';
import { OrdarComponent } from 'app/entities/ordar/ordar.component';
import { OrdarService } from 'app/entities/ordar/ordar.service';
import { Ordar } from 'app/shared/model/ordar.model';

describe('Component Tests', () => {
  describe('Ordar Management Component', () => {
    let comp: OrdarComponent;
    let fixture: ComponentFixture<OrdarComponent>;
    let service: OrdarService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MystoreTestModule],
        declarations: [OrdarComponent],
      })
        .overrideTemplate(OrdarComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrdarComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(OrdarService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Ordar(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.ordars && comp.ordars[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
