import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import {TableModule} from 'primeng/table';	
import {OrderListModule} from 'primeng/orderlist';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { IndexComponent } from './index/index.component';
import { ViewAllTransactionsComponent } from './view-all-transactions/view-all-transactions.component';

@NgModule({
  declarations: [
    AppComponent,
    IndexComponent,
    ViewAllTransactionsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    TableModule,
    OrderListModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
