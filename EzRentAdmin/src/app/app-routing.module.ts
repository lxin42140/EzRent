import { CreateNewTagComponent } from './tag/create-new-tag/create-new-tag.component';
import { ViewAllCategoriesComponent } from './category/view-all-categories/view-all-categories.component';
import { UpdateCategoryNameComponent } from './category/update-category-name/update-category-name.component';
import { CreateNewCategoryComponent } from './category/create-new-category/create-new-category.component';
import { IndexComponent } from './index/index.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DeliveryCompanyComponent } from './delivery-company/delivery-company.component';

import { ViewAllTagsComponent } from './tag/view-all-tags/view-all-tags.component';
import { AuthGuardServiceService } from './services/auth-guard.service';

const routes: Routes = [

  { path: '', redirectTo: '/index', pathMatch: 'full' },
	{ path: 'index', component: IndexComponent },
  { path: 'category/createNewCategory', component: CreateNewCategoryComponent, canActivate: [AuthGuardServiceService]},
  { path: 'category/viewAllCategories', component: ViewAllCategoriesComponent, canActivate: [AuthGuardServiceService]},
  { path: 'category/updateRootCategoryName/:categoryId', component: UpdateCategoryNameComponent, canActivate: [AuthGuardServiceService]},
  { path: 'createNewTag', component: CreateNewTagComponent, canActivate: [AuthGuardServiceService] },
  { path: 'viewAllTags', component: ViewAllTagsComponent, canActivate: [AuthGuardServiceService]},
  { path: 'deliveryCompany', component: DeliveryCompanyComponent, canActivate: [AuthGuardServiceService]}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)], 
  exports: [RouterModule]
})
export class AppRoutingModule { }
