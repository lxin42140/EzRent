import { Delivery } from './delivery';

export class Transaction {

    transactionId : number | undefined;
    transactionStartDate: Date | undefined;
    transactionEndDate: Date | undefined;

    constructor(transactionId : number, transactionStartDate: Date, transactionEndDate: Date) {
        this.transactionId = transactionId;
        this.transactionStartDate = transactionStartDate;
        this.transactionEndDate = transactionEndDate;
    }
}
