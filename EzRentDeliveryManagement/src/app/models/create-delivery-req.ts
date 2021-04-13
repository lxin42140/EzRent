import { Delivery } from './delivery';

export class CreateDeliveryReq {


    deliveryCompanyId: number;
    transactionId: number;
    newDeliveryEntity: Delivery;

    constructor(deliveryCompanyId: number, transactionId: number, newDeliveryEntity: Delivery) {
        this.deliveryCompanyId = deliveryCompanyId;
        this.transactionId = transactionId;
        this.newDeliveryEntity = newDeliveryEntity;
    }

    getDeliveryCompanyId(): number {
        return this.deliveryCompanyId;
    }

    getTransactionId(): number {
        return this.transactionId;
    }

    getNewDeliveryEntity(): Delivery {
        return this.newDeliveryEntity;
    }

}
