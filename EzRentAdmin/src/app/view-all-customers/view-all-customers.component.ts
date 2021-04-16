import { Component, OnInit } from '@angular/core';
import { AdminService } from '../services/admin.service';
import { SessionService } from '../services/session.service';

import {animate, state, style, transition, trigger} from '@angular/animations';
import { Customer } from '../models/customer';

@Component({
  selector: 'app-view-all-customers',
  templateUrl: './view-all-customers.component.html',
  styleUrls: ['./view-all-customers.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class ViewAllCustomersComponent implements OnInit {

  displayedHeaders : string[] = ['Id', 'Username', 'First Name', 'Last Name', 'Email'];

  customers: Customer[];
  
  expandedElement : Customer | undefined;

  successMessage: string | undefined;
  errorMessage: string | undefined;

  constructor(private adminService : AdminService,
    public sessionService: SessionService) {
      this.customers = new Array();
    }

  ngOnInit(): void {
    this.adminService.retrieveAllCustomer(this.sessionService.getUsername(), this.sessionService.getPassword()).subscribe(
			response => {
				this.customers = response;
			},
			error => {
				console.log('********** ViewAllCustomersComponent.ts: ' + error);
			}
		);
  }

  handleDisableClick(event: Event, customer: Customer): void {
    if (customer.userId !== undefined) {
      this.adminService.updateCustomerAccountStatus(this.sessionService.getUsername(), this.sessionService.getPassword(), customer.userId, true).subscribe(
        response => {
          this.successMessage = "Customer (id: " + response.userId + ") has been disabled!";
          this.ngOnInit();
        }, error => {
          this.errorMessage = error;
        }
      )
    }
  }

  handleEnableClick(event: Event, customer: Customer): void {
    if (customer.userId !== undefined) {
      this.adminService.updateCustomerAccountStatus(this.sessionService.getUsername(), this.sessionService.getPassword(), customer.userId, false).subscribe(
        response => {
          this.successMessage = "Customer (id: " + response.userId + ") has been enabled!";
          this.ngOnInit();
        }, error => {
          this.errorMessage = error;
        }
      )
    }
  }

}
