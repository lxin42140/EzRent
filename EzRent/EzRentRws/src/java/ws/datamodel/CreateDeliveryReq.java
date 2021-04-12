/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.DeliveryEntity;

/**
 *
 * @author Li Xin
 */
public class CreateDeliveryReq {

    Long deliveryCompanyId;
    Long transactionId;
    DeliveryEntity newDeliveryEntity;

    public CreateDeliveryReq() {
    }

    public CreateDeliveryReq(Long deliveryCompanyId, Long transactionId, DeliveryEntity newDeliveryEntity) {
        this.deliveryCompanyId = deliveryCompanyId;
        this.transactionId = transactionId;
        this.newDeliveryEntity = newDeliveryEntity;
    }

    public Long getDeliveryCompanyId() {
        return deliveryCompanyId;
    }

    public void setDeliveryCompanyId(Long deliveryCompanyId) {
        this.deliveryCompanyId = deliveryCompanyId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public DeliveryEntity getNewDeliveryEntity() {
        return newDeliveryEntity;
    }

    public void setNewDeliveryEntity(DeliveryEntity newDeliveryEntity) {
        this.newDeliveryEntity = newDeliveryEntity;
    }

}
