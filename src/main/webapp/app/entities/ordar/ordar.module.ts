import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MystoreSharedModule } from 'app/shared/shared.module';
import { OrdarComponent } from './ordar.component';
import { OrdarDetailComponent } from './ordar-detail.component';
import { OrdarUpdateComponent } from './ordar-update.component';
import { OrdarDeleteDialogComponent } from './ordar-delete-dialog.component';
import { ordarRoute } from './ordar.route';

@NgModule({
  imports: [MystoreSharedModule, RouterModule.forChild(ordarRoute)],
  declarations: [OrdarComponent, OrdarDetailComponent, OrdarUpdateComponent, OrdarDeleteDialogComponent],
  entryComponents: [OrdarDeleteDialogComponent],
})
export class MystoreOrdarModule {}
