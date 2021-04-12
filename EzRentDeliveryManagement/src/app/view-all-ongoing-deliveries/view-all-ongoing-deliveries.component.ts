import { Component, OnInit } from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import { NgForm } from '@angular/forms';
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import {ConfirmationService} from 'primeng/api';

import { DeliveryService } from '../services/delivery.service'
import { Delivery } from '../models/delivery'

@Component({
  selector: 'app-view-all-ongoing-deliveries',
  templateUrl: './view-all-ongoing-deliveries.component.html',
  styleUrls: ['./view-all-ongoing-deliveries.component.css']
})
export class ViewAllOngoingDeliveriesComponent implements OnInit {

  ongoingDeliveries: Delivery[] | any;

  constructor(private activatedRoute: ActivatedRoute,
              private deliveryService: DeliveryService,
              private confirmationService: ConfirmationService) { }

  ngOnInit(): void {
    this.deliveryService.getDeliveries().subscribe(
      response => {
        response.map(x => x.deliveryId == sessionStorage.getDeliveryCompany().deliveryCompanyId)
      }, 
      error => {
        console.log("********* View All Ongoing Deliveries: error at ngOnInit: " + error);
      }
    )
  }

  confirmDeliver() : void {
    this.confirmationService.confirm({
      message: 'Are you sure that you want to perform this action?',
      accept: () => {
          //Actual logic to perform a confirmation
      }
  });
  }

}
