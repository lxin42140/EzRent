//angular
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {InputTextModule} from 'primeng/inputtext';
import {MenubarModule} from 'primeng/menubar';
import {TreeTableModule} from 'primeng/treetable';
import {ButtonModule} from 'primeng/button';
import {RadioButtonModule} from 'primeng/radiobutton';
import {DropdownModule} from 'primeng/dropdown';
import {ConfirmDialogModule} from 'primeng/confirmdialog';

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
import { HeaderComponent } from './header/header.component';
import { MainMenuComponent } from './main-menu/main-menu.component';
import { DeliveryCompanyComponent } from './delivery-company/delivery-company.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AdminComponent } from './admin/admin.component';
import { ViewAllTagsComponent } from './tag/view-all-tags/view-all-tags.component';
import { CreateNewTagComponent } from './tag/create-new-tag/create-new-tag.component';
import { UpdateCategoryNameComponent } from './category/update-category-name/update-category-name.component';
import { DeleteCategoryComponent } from './category/delete-category/delete-category.component';

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
    ViewAllTagsComponent,
    CreateNewTagComponent,
    UpdateCategoryNameComponent,
    DeleteCategoryComponent
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
    TreeTableModule,
    ButtonModule,
    RadioButtonModule,
    DropdownModule,
    ConfirmDialogModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
