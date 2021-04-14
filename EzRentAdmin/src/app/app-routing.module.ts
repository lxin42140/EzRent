import { ViewAllCategoriesComponent } from './category/view-all-categories/view-all-categories.component';
import { IndexComponent } from './index/index.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DeliveryCompanyComponent } from './delivery-company/delivery-company.component';
import { AdminComponent } from './admin/admin.component';
import { ViewAllDeliveryCompaniesComponent } from './view-all-delivery-companies/view-all-delivery-companies.component'
import {
  AuthGuardServiceService
} from '../../../EzRentDeliveryManagement/src/app/services/auth-guard-service.service';
import { ViewAllAdminsComponent } from './view-all-admins/view-all-admins.component';
import { TagManagementComponent } from './tag-management/tag-management.component';

const routes: Routes = [

  { path: '', redirectTo: '/index', pathMatch: 'full' },
  { path: 'index', component: IndexComponent },
  { path: 'viewAllCategories', component: ViewAllCategoriesComponent },
  { path: 'deliveryCompany', component: DeliveryCompanyComponent },
  { path: 'admin', component: AdminComponent},
  { path: 'viewAllAdmins', component: ViewAllAdminsComponent},
  { path: 'tag', component: TagManagementComponent },
  { path: 'viewAllDeliveryCompanies', component: ViewAllDeliveryCompaniesComponent }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
