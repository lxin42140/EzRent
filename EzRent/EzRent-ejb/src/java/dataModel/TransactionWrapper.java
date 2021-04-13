/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataModel;

import java.io.Serializable;
import java.util.Date;
import util.enumeration.ModeOfPaymentEnum;

/**
 *
 * @author Li Xin
 */
public class TransactionWrapper implements Serializable {

    private Long transactionId;
    private Date acceptedDate;
    private String listingName;
    private String deliveryLocation;
    private ModeOfPaymentEnum modeOfPaymentEnum;
    private String customerName;

    public TransactionWrapper() {
    }

    public TransactionWrapper(Long transactionId, Date acceptedDate, String listingName, String deliveryLocation, ModeOfPaymentEnum modeOfPaymentEnum, String customerName) {
        this.transactionId = transactionId;
        this.acceptedDate = acceptedDate;
        this.listingName = listingName;
        this.deliveryLocation = deliveryLocation;
        this.modeOfPaymentEnum = modeOfPaymentEnum;
        this.customerName = customerName;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Date getAcceptedDate() {
        return acceptedDate;
    }

    public void setAcceptedDate(Date acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public String getListingName() {
        return listingName;
    }

    public void setListingName(String listingName) {
        this.listingName = listingName;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public ModeOfPaymentEnum getModeOfPaymentEnum() {
        return modeOfPaymentEnum;
    }

    public void setModeOfPaymentEnum(ModeOfPaymentEnum modeOfPaymentEnum) {
        this.modeOfPaymentEnum = modeOfPaymentEnum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

}
