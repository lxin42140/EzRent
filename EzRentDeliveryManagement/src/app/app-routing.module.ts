import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ViewAllTransactionsComponent} from './view-all-transactions/view-all-transactions.component';

import { ViewAllOngoingDeliveriesComponent } from './view-all-ongoing-deliveries/view-all-ongoing-deliveries.component';
import { ViewAllCompletedDeliveriesComponent } from './view-all-completed-deliveries/view-all-completed-deliveries.component';

const routes: Routes = [
  {path: '', redirectTo: '/index', pathMatch: 'full'},
  {path: 'viewAllOngoingDeliveries', component: ViewAllOngoingDeliveriesComponent},
  {path: 'viewAllTransactions', component: ViewAllTransactionsComponent},
  {path: 'viewAllCompletedDeliveries', component: ViewAllCompletedDeliveriesComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
