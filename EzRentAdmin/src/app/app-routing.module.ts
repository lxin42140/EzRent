import { ViewAllCategoriesComponent } from './category/view-all-categories/view-all-categories.component';
import { IndexComponent } from './index/index.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DeliveryCompanyComponent } from './delivery-company/delivery-company.component';
import {
    AuthGuardServiceService
} from '../../../EzRentDeliveryManagement/src/app/services/auth-guard-service.service';

const routes: Routes = [

  { path: '', redirectTo: '/index', pathMatch: 'full' },
	{ path: 'index', component: IndexComponent },
  { path: 'viewAllCategories', component: ViewAllCategoriesComponent, canActivate: [AuthGuardServiceService] },
  { path: 'deliveryCompany', component: DeliveryCompanyComponent, canActivate: [AuthGuardServiceService]}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)], 
  exports: [RouterModule]
})
export class AppRoutingModule { }
