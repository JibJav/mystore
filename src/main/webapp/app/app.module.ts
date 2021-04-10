import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { MystoreSharedModule } from 'app/shared/shared.module';
import { MystoreCoreModule } from 'app/core/core.module';
import { MystoreAppRoutingModule } from './app-routing.module';
import { MystoreHomeModule } from './home/home.module';
import { MystoreEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    MystoreSharedModule,
    MystoreCoreModule,
    MystoreHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    MystoreEntityModule,
    MystoreAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class MystoreAppModule {}
