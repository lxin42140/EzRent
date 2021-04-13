import { ViewAllCategoriesComponent } from './category/view-all-categories/view-all-categories.component';
import { IndexComponent } from './index/index.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DeliveryCompanyComponent } from './delivery-company/delivery-company.component';

const routes: Routes = [

  { path: '', redirectTo: '/index', pathMatch: 'full' },
	{ path: 'index', component: IndexComponent },
  { path: 'viewAllCategories', component: ViewAllCategoriesComponent },
  { path: 'deliveryCompany', component: DeliveryCompanyComponent }
	

];

@NgModule({
  imports: [RouterModule.forRoot(routes)], 
  exports: [RouterModule]
})
export class AppRoutingModule { }
