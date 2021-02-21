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
    
    @ManyToOne
    @Column(nullable = false)
    @NotNull
    private CustomerEntity reporter;
    
    @OneToOne
    private CustomerEntity violatingCustomer;
    
    @OneToOne
    private ListingEntity violatingListing;

    public ReportEntity() {
    }

    public ReportEntity(ReportIssueEnum reportIssue, String reportDescription, CustomerEntity reporter, CustomerEntity violatingCustomer) {
        this.reportIssue = reportIssue;
        this.reportDescription = reportDescription;
        this.reporter = reporter;
        this.violatingCustomer = violatingCustomer;
    }

    public ReportEntity(ReportIssueEnum reportIssue, String reportDescription, CustomerEntity reporter, ListingEntity violatingListing) {
        this.reportIssue = reportIssue;
        this.reportDescription = reportDescription;
        this.reporter = reporter;
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
    
    public CustomerEntity getReporter() {
        return reporter;
    }
    
    public void setReporter(CustomerEntity reporter) {
        this.reporter = reporter;
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
}
