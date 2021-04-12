import { Component, OnInit } from '@angular/core';

import { DeliveryService } from '../services/delivery.service'
import { Delivery } from '../models/delivery'
import { SessionService } from '../services/session.service';
import {DeliveryStatusEnum} from '../models/delivery-status-enum'

@Component({
  selector: 'app-view-all-completed-deliveries',
  templateUrl: './view-all-completed-deliveries.component.html',
  styleUrls: ['./view-all-completed-deliveries.component.css']
})
export class ViewAllCompletedDeliveriesComponent implements OnInit {

  completedDeliveries : Delivery[];
  lostDeliveries : Delivery[];

  constructor(private deliveryService: DeliveryService,
    private sessionService: SessionService) { 
      this.completedDeliveries = new Array();
      this.lostDeliveries = new Array();
  }

  ngOnInit(): void {
    if (this.sessionService.getIsLogin()) {
      this.deliveryService.getDeliveries().subscribe(
        response => {
          response.forEach(x => {
            if (x.deliveryStatus == DeliveryStatusEnum.DELIVERED) {
              this.completedDeliveries.push(x);
            } else if (x.deliveryStatus == DeliveryStatusEnum.LOST) {
              this.lostDeliveries.push(x);
            }
          })
        },
        error => {
          console.log("********* View All Completed Deliveries: error at ngOnInit: " + error);
        }
      ); 
    }
  }

}
