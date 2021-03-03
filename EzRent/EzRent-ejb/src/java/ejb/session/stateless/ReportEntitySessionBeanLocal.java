/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReportEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewReportException;
import util.exception.CustomerNotFoundException;
import util.exception.ListingNotFoundException;
import util.exception.ReportNotFoundException;
import util.exception.UpdateReportException;

/**
 *
 * @author Ziyue
 */
@Local
public interface ReportEntitySessionBeanLocal {

    public Long createNewCustomerReport(Long reporterId, Long violatingCustomerId, ReportEntity reportEntity) throws CustomerNotFoundException, ListingNotFoundException, CreateNewReportException;

    public Long createNewListingReport(Long reporterId, Long listingId, ReportEntity reportEntity) throws CustomerNotFoundException, ListingNotFoundException, CreateNewReportException;

    public List<ReportEntity> retrieveAllReports();

    public List<ReportEntity> retrieveAllOpenReports();

    public List<ReportEntity> retrieveAllClosedReports();

    public ReportEntity retrieveReportByReportId(Long reportId) throws ReportNotFoundException;

    public void updateReportDetails(Long reportId, ReportEntity reportEntityToUpdate) throws UpdateReportException, ReportNotFoundException;

    public void closeReport(Long reportId) throws ReportNotFoundException;

    
}
