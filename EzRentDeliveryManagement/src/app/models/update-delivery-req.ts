import { Delivery } from './delivery'

export class UpdateDeliveryReq {

    deliveryCompanyId : number | undefined;
    transactionId : number | undefined;
    newDeliveryEntity : Delivery | undefined;

    constructor(deliveryCompanyId : number, transactionId : number, newDeliveryEntity : Delivery) {
        this.deliveryCompanyId = deliveryCompanyId;
        this.transactionId = transactionId;
        this.newDeliveryEntity = newDeliveryEntity;
    }

}
