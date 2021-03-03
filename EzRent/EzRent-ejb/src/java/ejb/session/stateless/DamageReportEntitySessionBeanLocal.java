/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DamageReportEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewDamageReportException;
import util.exception.CustomerNotFoundException;
import util.exception.DamageReportNotFoundException;
import util.exception.DeleteDamageReportException;
import util.exception.TransactionNotFoundException;
import util.exception.UpdateDamageReportException;

/**
 *
 * @author Ziyue
 */
@Local
public interface DamageReportEntitySessionBeanLocal {

    public Long createNewDamageReport(Long customerId, Long transactionId, DamageReportEntity damageReportEntity) throws CreateNewDamageReportException, CustomerNotFoundException, TransactionNotFoundException;

    public List<DamageReportEntity> retrieveAllDamageReports();

    public List<DamageReportEntity> retrieveAllPendingDamageReports();

    public List<DamageReportEntity> retrieveAllResolvedDamageReports();

    public DamageReportEntity retrieveDamageReportByDamageReportId(Long damageReportId) throws DamageReportNotFoundException;

    public void resolveDamageReport(Long damageReportId) throws DamageReportNotFoundException, UpdateDamageReportException;

    public void deleteDamageReport(Long damageReportId) throws DamageReportNotFoundException, DeleteDamageReportException;

    public void updateDamageReportDetails(Long damageReportId, DamageReportEntity damageReportEntityToUpdate) throws UpdateDamageReportException, DamageReportNotFoundException;
    
}
