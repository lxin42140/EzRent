/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

/**
 *
 * @author Li Xin
 */
public class UpdateDeliveryReq {

    private String deliveryComment;
    private Long deliveryId;
    private String deliveryStatus;

    public UpdateDeliveryReq() {
    }

    public UpdateDeliveryReq(String deliveryComment, Long deliveryId, String deliveryStatus) {
        this.deliveryComment = deliveryComment;
        this.deliveryId = deliveryId;
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryComment() {
        return deliveryComment;
    }

    public void setDeliveryComment(String deliveryComment) {
        this.deliveryComment = deliveryComment;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

}
