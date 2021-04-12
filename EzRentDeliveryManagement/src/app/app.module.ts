import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import {TableModule} from 'primeng/table';	
import {OrderListModule} from 'primeng/orderlist';
import {DialogModule} from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import {PanelModule} from 'primeng/panel';

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
    HttpClientModule,
    BrowserAnimationsModule,
    TableModule,
    OrderListModule,
    DialogModule,
    ConfirmDialogModule,
    PanelModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
