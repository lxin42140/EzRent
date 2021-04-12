import { Delivery } from './delivery';
import { ModeOfPaymentEnum } from './mode-of-payment-enum';

export class Transaction {

    transactionId: number;
    acceptedDate: Date;
    listingName: string;
    deliveryLocation: string;
    modeOfPaymentEnum: ModeOfPaymentEnum;
    customerName: string;

    constructor(
        transactionId: number,
        acceptedDate: Date,
        listingName: string,
        deliveryLocation: string,
        modeOfPaymentEnum: ModeOfPaymentEnum,
        customerName: string
    ) {
        this.transactionId = transactionId;
        this.acceptedDate = acceptedDate;
        this.listingName = listingName;
        this.deliveryLocation = deliveryLocation;
        this.modeOfPaymentEnum = modeOfPaymentEnum;
        this.customerName = customerName;
    }


}
