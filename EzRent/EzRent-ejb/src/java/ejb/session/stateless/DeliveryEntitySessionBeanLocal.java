/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DeliveryEntity;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.DeliveryStatusEnum;
import util.exception.CreateNewDeliveryException;
import util.exception.DeliveryNotFoundException;
import util.exception.UpdateDeliveryException;

/**
 *
 * @author Li Xin
 */
@Local
public interface DeliveryEntitySessionBeanLocal {

    public Long createNewDelivery(DeliveryEntity newDeliveryEntity) throws CreateNewDeliveryException;

    public Long updateDeliveryStatus(Long deliveryId, DeliveryStatusEnum newDeliveryStatus) throws UpdateDeliveryException, DeliveryNotFoundException;

    public List<DeliveryEntity> retrieveAllDeliveries();

    public DeliveryEntity retrieveDeliveryByDeliveryId(Long deliveryId) throws DeliveryNotFoundException;

    public List<DeliveryEntity> retrieveDeliveriesByStatus(DeliveryStatusEnum deliveryStatus);
    
}
