import { Admin } from './../models/admin';
import { Injectable } from '@angular/core';
import { MenuItem } from 'primeng/api';

import { UserAccessRightEnum } from '../models/user-access-right-enum.enum';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  items: MenuItem[];

  constructor() {
    this.items = [
			{
				label: 'Home',
        icon: 'pi pi-fw pi-home',
        url: '/index'
			}
		];

  }

  getMenuBarItem(): MenuItem[] {
    if (sessionStorage.isLogin == "true") {
      return this.items = [
				{
					label: 'Home',
					icon: 'pi pi-fw pi-home',
					routerLink:"/index"
				},

				{
					label: 'Manage Account',
					icon: 'pi pi-fw pi-user-edit',
					items: [
						{
							label: 'Admin',
							icon: 'pi pi-fw pi-user',
							items: [
								{
									label: 'Create New Admin Account',
									icon: 'pi pi-fw pi-user-plus',
									routerLink:"/admin"
								},
		
								{
									label: 'View All Admins',
									icon: 'pi pi-fw pi-users',
									routerLink:"/viewAllAdmins"
								}
							]
						},

						{
							label: 'Delivery Company',
							icon: 'pi pi-fw pi-amazon',
							items: [
								{
									label: 'Create New Delivery Company Account',
									icon: 'pi pi-fw pi-user-plus',
									routerLink:"/deliveryCompany"
								},
		
								{
									label: 'View All Delivery Companies',
									icon: 'pi pi-fw pi-users',
									routerLink:"/viewAllDeliveryCompanies"
								}
							]
						},

						// {
						// 	label: 'Customer',
						// 	icon: 'pi pi-fw pi-users'
						// }
					]
				},
						{
							label: 'Manage Listing',
							icon: 'pi pi-fw pi-user-edit',
							items:[
								{
									label:'Category',
									icon: 'pi pi-fw pi-book',
									items:[
										{
                      label:'Create Category',
                      icon: 'pi pi-fw pi-plus',
											url: 'category/createNewCategory'
                    },
                    
                    {
                      label:'View All Categories',
                      icon: 'pi pi-fw pi-list',
											url: 'category/viewAllCategories'
                    }
									]
								},

								{
									label:'Tag',
                  icon: 'pi pi-fw pi-tag',
                  url: '/tag'
								}
							]
						}

					];
    } else {
      return this.items = [
        {
          label: 'Home',
          icon: 'pi pi-fw pi-home',
          url: '/index'
        }
      ];
    }
    
  }

  getIsLogin(): boolean {
    if (sessionStorage.isLogin == "true") {
      return true;
    }
    else {
      return false;
    }
  }

  setIsLogin(isLogin: boolean): void {
    sessionStorage.isLogin = isLogin;
  }

  getCurrentAdmin(): Admin {
    return JSON.parse(sessionStorage.currentAdmin);
  }

  setCurrentAdmin(currentAdmin: Admin): void {
    sessionStorage.currentAdmin = JSON.stringify(currentAdmin);
  }

  getUsername(): string {
    return sessionStorage.username;
  }


  setUsername(username: string | undefined): void {
    sessionStorage.username = username;
  }

  getPassword(): string {
    return sessionStorage.password;
  }


  setPassword(password: string | undefined): void {
    sessionStorage.password = password;
  }

}
