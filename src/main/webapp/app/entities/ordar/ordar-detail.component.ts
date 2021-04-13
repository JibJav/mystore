import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrdar } from 'app/shared/model/ordar.model';

@Component({
  selector: 'jhi-ordar-detail',
  templateUrl: './ordar-detail.component.html',
})
export class OrdarDetailComponent implements OnInit {
  ordar: IOrdar | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordar }) => (this.ordar = ordar));
  }

  previousState(): void {
    window.history.back();
  }
}
