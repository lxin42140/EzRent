import { SessionService } from '../services/session.service';
import { TransactionService } from '../services/transaction.service';
import { DeliveryService } from '../services/delivery.service';
import { Transaction } from '../models/transaction';
import { Delivery } from '../models/delivery';
import { CreateDeliveryReq } from '../models/create-delivery-req';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-view-all-transactions',
  templateUrl: './view-all-transactions.component.html',
  styleUrls: ['./view-all-transactions.component.css']
})
export class ViewAllTransactionsComponent implements OnInit {

  transactions: Transaction[];
  error: boolean;
  createDeliveryError: string | undefined;
  newDeliveryId: number | undefined;

  constructor(public sessionService: SessionService, private transactionService: TransactionService, private deliveryService: DeliveryService) {
    this.error = false;
    this.transactions = [];
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

  createDelivery(transactionId: number): void {
    var newDelivery = new Delivery("PENDING", "Delivery arranged", new Date());
    var deliveryCompanyId = this.sessionService.getCurrentDeliveryCompany().userId;
    var createDeliveryReq = new CreateDeliveryReq(7, transactionId, newDelivery);
    this.deliveryService.createNewDelivery(createDeliveryReq).subscribe(
      response => {
        this.newDeliveryId = response;
        //remove from transaction
        this.transactions = this.transactions.filter(x => x.transactionId !== transactionId);
      },
      error => {
        this.createDeliveryError = error;
      }
    )
  }

}
