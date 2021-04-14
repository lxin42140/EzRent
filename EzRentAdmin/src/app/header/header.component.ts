import { UserAccessRightEnum } from './../models/user-access-right-enum.enum';
import { Admin } from './../models/admin';
import { AdminService } from './../services/admin.service';
import { SessionService } from './../services/session.service';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MenuItem } from 'primeng/api';

@Component({
	selector: 'app-header',
	templateUrl: './header.component.html',
	styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
	@Output()
	childEvent = new EventEmitter();

	username: string;
	password: string;
	loginError: boolean;
	errorMessage: string | undefined;
	items: MenuItem[];



	constructor(private router: Router,
		private activatedRoute: ActivatedRoute,
		public sessionService: SessionService,
		private adminService: AdminService) {
		this.loginError = false;
		if (this.sessionService.getIsLogin() != true) {
			this.items = [
				{
					label: 'Home',
					icon: 'pi pi-fw pi-home',
					routerLink:"/index"
				}
			];
		} else {
			this.items = [
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
									routerLink:"/deliveryCompany"
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
					items: [
						{
							label: 'Category',
							icon: 'pi pi-fw pi-book'
						},

						{
							label: 'Tag',
							icon: 'pi pi-fw pi-tag'
						}
					]
				}

			]
		}
		this.username = "";
		this.password = "";
	}




	ngOnInit(): void {
	}


	adminLogin(): void {


		console.log(this.username, this.password);

		this.adminService.adminLogin(this.username, this.password).subscribe(
			response => {
				this.sessionService.setUsername(this.username);
				this.sessionService.setPassword(this.password);
				this.sessionService.setIsLogin(true);
				this.sessionService.setUsername(this.username);
				this.sessionService.setPassword(this.password);
				this.sessionService.setCurrentAdmin(response);
				this.loginError = false;

				this.childEvent.emit();

				this.router.navigate(["/index"]);
				this.items = [
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
										routerLink:"/deliveryCompany"
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
						label: 'Manage Category and Tag',
						icon: 'pi pi-fw pi-user-edit',
						items: [
							{
								label: 'Category',
								icon: 'pi pi-fw pi-book'
							},
	
							{
								label: 'Tag',
								icon: 'pi pi-fw pi-tag',
								command: () => {
									this.router.navigate(["/tag"])
								  }
							}
						]
					}
	
				]

			},
			error => {
				this.loginError = true;
				this.errorMessage = error
			}
		);
	}



	adminLogout(): void {
		this.sessionService.setIsLogin(false);
		window.sessionStorage.clear();
		this.username = "";
		this.password = "";
		this.router.navigate(["/index"]);

		this.items = [
			{
				label: 'Home',
				icon: 'pi pi-fw pi-home'
			}
		];
	}

	handleClear() {
		this.username = "";
		this.password = "";
	}

}
