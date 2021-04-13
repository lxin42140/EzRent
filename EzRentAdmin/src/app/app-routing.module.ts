import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DeliveryCompanyComponent } from './delivery-company/delivery-company.component';

const routes: Routes = [
  {path: '', redirectTo: '/index', pathMatch: 'full'},
	{ path: 'deliveryCompany', component: DeliveryCompanyComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)], 
  exports: [RouterModule]
})
export class AppRoutingModule { }
