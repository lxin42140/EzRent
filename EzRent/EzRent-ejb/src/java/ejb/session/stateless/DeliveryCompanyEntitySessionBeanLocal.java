/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DeliveryCompanyEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewDeliveryCompanyException;
import util.exception.DeliveryCompanyNotFoundException;
import util.exception.InvalidLoginException;
import util.exception.UpdateDeliveryCompanyException;

/**
 *
 * @author Li Xin
 */
@Local
public interface DeliveryCompanyEntitySessionBeanLocal {

    public Long createNewDeliveryCompany(DeliveryCompanyEntity newDeliveryCompanyEntity) throws CreateNewDeliveryCompanyException;

    public Long updateDeliveryCompanyDetails(Long deliveryCompanyId, DeliveryCompanyEntity deliveryCompanyToUpdate) throws DeliveryCompanyNotFoundException, UpdateDeliveryCompanyException;

    public DeliveryCompanyEntity retrieveDeliveryCompanyByUsernameAndPassword(String username, String password) throws DeliveryCompanyNotFoundException, InvalidLoginException;
    
    public List<DeliveryCompanyEntity> retrieveAllDeliveryCompanies();

    public List<DeliveryCompanyEntity> retrieveAllDisabledDeliveryCompanies();

    public DeliveryCompanyEntity retrieveDeliveryCompanyById(Long companyId) throws DeliveryCompanyNotFoundException;
    
}
