import { Delivery } from './delivery';

export class Transaction {

    transactionId : number | undefined;
    transactionStartDate: Date | undefined;
    transactionEndDate: Date | undefined;
    delivery : Delivery | undefined;

    constructor(transactionId : number, transactionStartDate: Date, transactionEndDate: Date, delivery : Delivery) {
        this.transactionId = transactionId;
        this.transactionStartDate = transactionStartDate;
        this.transactionEndDate = transactionEndDate;
        this.delivery = delivery;
    }
}
