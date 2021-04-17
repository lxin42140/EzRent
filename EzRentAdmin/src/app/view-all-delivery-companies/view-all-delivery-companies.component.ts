import { Component, OnInit } from '@angular/core';

import { DeliveryCompanyService } from '../services/delivery-company.service'
import { SessionService } from '../services/session.service'
import { DeliveryCompany } from '../models/delivery-company'

import {animate, state, style, transition, trigger} from '@angular/animations';


@Component({
  selector: 'app-view-all-delivery-companies',
  templateUrl: './view-all-delivery-companies.component.html',
  styleUrls: ['./view-all-delivery-companies.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class ViewAllDeliveryCompaniesComponent implements OnInit {

  displayedHeaders : string[] = ['Id', 'UEN', 'Company name', 'Contact number'];
  // displayedColumns: string[] = ['userId', 'companyUEN', 'companyName', 'companyContactNumber', 'action'];
  deliveryCompanies: DeliveryCompany[];
  expandedElement : DeliveryCompany | undefined;

  successMessage: string | undefined;
  errorMessage: string | undefined;

  constructor(private deliveryCompanyService: DeliveryCompanyService,
    private sessionService: SessionService) {
    this.deliveryCompanies = new Array();
  }

  ngOnInit(): void {
    this.deliveryCompanyService.retrieveAllDeliveryCompanies(this.sessionService.getUsername(), this.sessionService.getPassword()).subscribe(
      response => {
        this.deliveryCompanies = response;
      }, error => {
        console.log(error);
      }
    )
  }

  handleDisableClick(event: Event, company: DeliveryCompany): void {
    if (company.userId !== undefined) {
      this.deliveryCompanyService.updateDeliveryCompanyStatus(this.sessionService.getUsername(), this.sessionService.getPassword(), company.userId, true).subscribe(
        response => {
          this.successMessage = "Delivery company (id: " + response.userId + ") has been disabled!";
          this.ngOnInit();
        }, error => {
          this.errorMessage = error;
        }
      )
    }
  }

  handleEnableClick(event: Event, company: DeliveryCompany): void {
    if (company.userId !== undefined) {
      this.deliveryCompanyService.updateDeliveryCompanyStatus(this.sessionService.getUsername(), this.sessionService.getPassword(), company.userId, false).subscribe(
        response => {
          this.successMessage = "Delivery company (id: " + response.userId + ") has been enabled!";
          this.ngOnInit();
        }, error => {
          this.errorMessage = error;
        }
      )
    }
  }
}
