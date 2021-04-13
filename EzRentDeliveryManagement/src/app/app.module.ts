import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

//primeng
import { TableModule } from 'primeng/table';
import { OrderListModule } from 'primeng/orderlist';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { PanelModule } from 'primeng/panel';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { TabViewModule } from 'primeng/tabview';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {SidebarModule} from 'primeng/sidebar';
import {MenuModule} from 'primeng/menu';
import {InputTextareaModule} from 'primeng/inputtextarea';
import {MenubarModule} from 'primeng/menubar';

//Components
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { IndexComponent } from './index/index.component';
import { ViewAllOngoingDeliveriesComponent } from './view-all-ongoing-deliveries/view-all-ongoing-deliveries.component';
import { ViewAllTransactionsComponent } from './view-all-transactions/view-all-transactions.component';
import { ViewDeliveryCompanyDetailsComponent } from './view-delivery-company-details/view-delivery-company-details.component';
import { ViewAllCompletedDeliveriesComponent } from './view-all-completed-deliveries/view-all-completed-deliveries.component';
import { MainmenuComponent } from './mainmenu/mainmenu.component';

@NgModule({
  declarations: [
    AppComponent,
    IndexComponent,
    ViewAllOngoingDeliveriesComponent,
    ViewAllTransactionsComponent,
    ViewDeliveryCompanyDetailsComponent,
    ViewAllCompletedDeliveriesComponent,
    MainmenuComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    TableModule,
    OrderListModule,
    ButtonModule,
    DialogModule,
    PanelModule,
    ConfirmDialogModule,
    TabViewModule,
    MessagesModule, 
    MessageModule,
    SidebarModule,
    MenuModule,
    InputTextareaModule,
    MenubarModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }