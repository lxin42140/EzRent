/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import util.enumeration.ReportIssueEnum;

/**
 *
 * @author Yuxin
 */
@Entity
public class ReportEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private ReportIssueEnum reportIssue;

    private String reportDescription;
    
    @Column(nullable = false)
    @NotNull
    private boolean isClosed;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "customerId")
    @NotNull
    private CustomerEntity customer;

    @OneToOne
    @JoinColumn(name = "violatingCustomerId")
    private CustomerEntity violatingCustomer;

    @OneToOne
    @JoinColumn(name = "violatingListingId")
    private ListingEntity violatingListing;

    public ReportEntity() {
        this.isClosed = false;
    }

    public ReportEntity(ReportIssueEnum reportIssue, String reportDescription, CustomerEntity reporter, CustomerEntity violatingCustomer) {
        this();
        
        this.reportIssue = reportIssue;
        this.reportDescription = reportDescription;
        this.customer = reporter;
        this.violatingCustomer = violatingCustomer;
    }

    public ReportEntity(ReportIssueEnum reportIssue, String reportDescription, CustomerEntity reporter, ListingEntity violatingListing) {
        this();
        
        this.reportIssue = reportIssue;
        this.reportDescription = reportDescription;
        this.customer = reporter;
        this.violatingListing = violatingListing;
    }

    public Long getReportId() {
        return reportId;
    }

    public ReportIssueEnum getReportIssue() {
        return reportIssue;
    }

    public void setReportIssue(ReportIssueEnum reportIssue) {
        this.reportIssue = reportIssue;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public CustomerEntity getViolatingCustomer() {
        return violatingCustomer;
    }

    public void setViolatingCustomer(CustomerEntity violatingCustomer) {
        this.violatingCustomer = violatingCustomer;
    }

    public ListingEntity getViolatingListing() {
        return violatingListing;
    }

    public void setViolatingListing(ListingEntity violatingListing) {
        this.violatingListing = violatingListing;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reportId != null ? reportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reportId fields are not set
        if (!(object instanceof ReportEntity)) {
            return false;
        }
        ReportEntity other = (ReportEntity) object;
        if ((this.reportId == null && other.reportId != null) || (this.reportId != null && !this.reportId.equals(other.reportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Report[ id=" + reportId + " ]";
    }

    public boolean isIsClosed() {
        return isClosed;
    }

    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }
}
