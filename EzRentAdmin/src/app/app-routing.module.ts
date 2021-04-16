import { ViewAllCategoriesComponent } from './category/view-all-categories/view-all-categories.component';
import { UpdateCategoryNameComponent } from './category/update-category-name/update-category-name.component';
import { CreateNewCategoryComponent } from './category/create-new-category/create-new-category.component';
import { IndexComponent } from './index/index.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DeliveryCompanyComponent } from './delivery-company/delivery-company.component';
import { AdminComponent } from './admin/admin.component';
import { ViewAllDeliveryCompaniesComponent } from './view-all-delivery-companies/view-all-delivery-companies.component'
import { ViewAllAdminsComponent } from './view-all-admins/view-all-admins.component';
import { TagManagementComponent } from './tag-management/tag-management.component';
import { AuthGuardServiceService } from './services/auth-guard.service';
import { ViewAllCustomersComponent } from './view-all-customers/view-all-customers.component';

const routes: Routes = [

  { path: '', redirectTo: '/index', pathMatch: 'full' },
	{ path: 'index', component: IndexComponent },
  { path: 'category/createNewCategory', component: CreateNewCategoryComponent, canActivate: [AuthGuardServiceService]},
  { path: 'category/viewAllCategories', component: ViewAllCategoriesComponent, canActivate: [AuthGuardServiceService]},
  { path: 'category/updateRootCategoryName/:categoryId', component: UpdateCategoryNameComponent, canActivate: [AuthGuardServiceService]},
  { path: 'deliveryCompany', component: DeliveryCompanyComponent, canActivate: [AuthGuardServiceService]},
  { path: 'admin', component: AdminComponent, canActivate: [AuthGuardServiceService]},
  { path: 'viewAllAdmins', component: ViewAllAdminsComponent, canActivate: [AuthGuardServiceService]},
  { path: 'tag', component: TagManagementComponent, canActivate: [AuthGuardServiceService]},
  { path: 'viewAllDeliveryCompanies', component: ViewAllDeliveryCompaniesComponent , canActivate: [AuthGuardServiceService]},
  { path: 'viewAllCustomers', component: ViewAllCustomersComponent, canActivate: [AuthGuardServiceService]}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
