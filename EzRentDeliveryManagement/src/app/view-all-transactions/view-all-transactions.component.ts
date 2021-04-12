import { SessionService } from '../services/session.service';
import { TransactionService } from '../services/transaction.service';
import { DeliveryService } from '../services/delivery.service';
import { Transaction } from '../models/transaction';
import { Delivery } from '../models/delivery';
import { DeliveryCompany } from '../models/delivery-company';
=import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-view-all-transactions',
  templateUrl: './view-all-transactions.component.html',
  styleUrls: ['./view-all-transactions.component.css']
})
export class ViewAllTransactionsComponent implements OnInit {

  transactions: Transaction[] | undefined;
  error: boolean;

  constructor(public sessionService: SessionService, private transactionService: TransactionService, private deliveryService: DeliveryService) {
    this.error = false;
  }

  ngOnInit(): void {
    this.transactionService.getPendingDeliveryTransactions().subscribe(
      response => {
        this.transactions = response;
      }, error => {
        this.error = true;
      }
    )
  }

  createDelivery(transactionID : number) : void {
    // var newDelivery = new Delivery();
    // var deliveryCompanyId = this.sessionService.getCurrentDeliveryCompany().userId;
    // this.deliveryService.createNewDelivery()
  }
  

}
