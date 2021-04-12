import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ViewAllOngoingDeliveriesComponent } from './view-all-ongoing-deliveries/view-all-ongoing-deliveries.component'

const routes: Routes = [
  {path: '', redirectTo: '/index', pathMatch: 'full'},
  {path: 'viewAllOngoingDeliveries', component: ViewAllOngoingDeliveriesComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
