import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import {TableModule} from 'primeng/table';	

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { IndexComponent } from './index/index.component';
import { ViewAllOngoingDeliveriesComponent } from './view-all-ongoing-deliveries/view-all-ongoing-deliveries.component';
import { ViewAllTransactionsComponent } from './view-all-transactions/view-all-transactions.component';

@NgModule({
  declarations: [
    AppComponent,
    IndexComponent,
    ViewAllOngoingDeliveriesComponent,
    ViewAllTransactionsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    TableModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
