//angular
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

//angular material
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';

//primeng
import {TableModule} from 'primeng/table';	

//components
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { IndexComponent } from './index/index.component';
import { ViewAllCategoriesComponent } from './category/view-all-categories/view-all-categories.component';
import { CreateNewCategoryComponent } from './category/create-new-category/create-new-category.component';
import { DeliveryCompanyComponent } from './delivery-company/delivery-company.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatButtonModule} from '@angular/material/button';

@NgModule({
  declarations: [
    AppComponent,
    IndexComponent,
    ViewAllCategoriesComponent,
    CreateNewCategoryComponent,
    DeliveryCompanyComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    TableModule,
    BrowserAnimationsModule, 
    MatInputModule, 
    MatFormFieldModule,
    MatButtonModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
