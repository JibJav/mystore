import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MystoreTestModule } from '../../../test.module';
import { OrdarDetailComponent } from 'app/entities/ordar/ordar-detail.component';
import { Ordar } from 'app/shared/model/ordar.model';

describe('Component Tests', () => {
  describe('Ordar Management Detail Component', () => {
    let comp: OrdarDetailComponent;
    let fixture: ComponentFixture<OrdarDetailComponent>;
    const route = ({ data: of({ ordar: new Ordar(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MystoreTestModule],
        declarations: [OrdarDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(OrdarDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OrdarDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ordar on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ordar).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
