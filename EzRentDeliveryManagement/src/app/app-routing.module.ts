import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ViewAllTransactionsComponent } from './view-all-transactions/view-all-transactions.component';

import { ViewAllOngoingDeliveriesComponent } from './view-all-ongoing-deliveries/view-all-ongoing-deliveries.component';
import { ViewAllCompletedDeliveriesComponent } from './view-all-completed-deliveries/view-all-completed-deliveries.component';
import { IndexComponent } from './index/index.component';
import {
  ViewDeliveryCompanyDetailsComponent
} from './view-delivery-company-details/view-delivery-company-details.component';
import { AuthGuardServiceService } from './services/auth-guard-service.service';

const routes: Routes = [
  { path: '', redirectTo: '/index', pathMatch: 'full' },
  { path: 'index', component: IndexComponent},
  { path: 'viewAllOngoingDeliveries', component: ViewAllOngoingDeliveriesComponent, canActivate: [AuthGuardServiceService] },
  { path: 'viewAllTransactions', component: ViewAllTransactionsComponent, canActivate: [AuthGuardServiceService] },
  { path: 'viewAllCompletedDeliveries', component: ViewAllCompletedDeliveriesComponent, canActivate: [AuthGuardServiceService] },
  { path: 'profile', component: ViewDeliveryCompanyDetailsComponent, canActivate: [AuthGuardServiceService] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
