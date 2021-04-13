import { SessionService } from '../services/session.service';
import { DeliveryCompany } from '../models/delivery-company';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-view-delivery-company-details',
  templateUrl: './view-delivery-company-details.component.html',
  styleUrls: ['./view-delivery-company-details.component.css']
})
export class ViewDeliveryCompanyDetailsComponent implements OnInit {

  currentDeliveryCompany: DeliveryCompany | undefined;

  constructor(private sessionService: SessionService) { }

  ngOnInit(): void {
    this.currentDeliveryCompany = this.sessionService.getCurrentDeliveryCompany();
  }

}
