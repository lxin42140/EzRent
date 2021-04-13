import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {InputTextModule} from 'primeng/inputtext';
import {MenubarModule} from 'primeng/menubar';

//angular material
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';

//primeng
import {TableModule} from 'primeng/table';	


import { OrderListModule } from 'primeng/orderlist';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { PanelModule } from 'primeng/panel';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { TabViewModule } from 'primeng/tabview';
import {SidebarModule} from 'primeng/sidebar';
import {MenuModule} from 'primeng/menu';
import {InputTextareaModule} from 'primeng/inputtextarea';

//components
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { IndexComponent } from './index/index.component';
import { ViewAllCategoriesComponent } from './category/view-all-categories/view-all-categories.component';
import { CreateNewCategoryComponent } from './category/create-new-category/create-new-category.component';
import { HeaderComponent } from './header/header.component';
import { MainMenuComponent } from './main-menu/main-menu.component';
import { DeliveryCompanyComponent } from './delivery-company/delivery-company.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AdminComponent } from './admin/admin.component';
import { TagManagementComponent } from './tag-management/tag-management.component';
import { ViewAllDeliveryCompaniesComponent } from './view-all-delivery-companies/view-all-delivery-companies.component';

@NgModule({
  declarations: [
    AppComponent,
    IndexComponent,
    ViewAllCategoriesComponent,
    CreateNewCategoryComponent,
    AdminComponent,
    HeaderComponent,
    MainMenuComponent,
    DeliveryCompanyComponent,
    TagManagementComponent,
    ViewAllDeliveryCompaniesComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    TableModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatButtonModule,
    InputTextModule,
    MenubarModule,
    MatInputModule, 
    MatFormFieldModule,
    ReactiveFormsModule,
    FormsModule,
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
