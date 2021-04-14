import { CreateNewCategoryComponent } from './../category/create-new-category/create-new-category.component';
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
		this.items = this.sessionService.getMenuBarItem();
		this.username = "";
		this.password = "";
	}




	ngOnInit(): void {
	}


	adminLogin(): void {
		// this.sessionService.setUsername(this.username);
		// this.sessionService.setPassword(this.password);

		this.adminService.adminLogin(this.username, this.password).subscribe(
			response => {
				let admin: Admin = response;


				if (admin != null) {
					this.sessionService.setIsLogin(true);
					this.sessionService.setCurrentAdmin(admin);
					this.sessionService.setUsername(this.username);
					this.sessionService.setPassword(this.password);

					this.loginError = false;

					this.childEvent.emit();

					this.router.navigate(["/index"]);
					this.items = this.sessionService.getMenuBarItem();
					
				}
				else {
					this.loginError = true;
				}
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
		this.items = this.sessionService.getMenuBarItem();
		
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
