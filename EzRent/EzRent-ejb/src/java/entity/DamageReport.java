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
import javax.validation.constraints.NotNull;
import util.enumeration.DamageReportEnum;

/**
 *
 * @author Yuxin
 */
@Entity
public class DamageReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long damageReportId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private DamageReportEnum damageReport;
    @Column(nullable = false)
    @NotNull
    private String damageDescription;
    @Column(nullable = false)
    @NotNull
    private String damagePhotoLink;
    
    public Long getDamageReportId() {
        return damageReportId;
    }

    public void setDamageReportId(Long damageReportId) {
        this.damageReportId = damageReportId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (damageReportId != null ? damageReportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the damageReportId fields are not set
        if (!(object instanceof DamageReport)) {
            return false;
        }
        DamageReport other = (DamageReport) object;
        if ((this.damageReportId == null && other.damageReportId != null) || (this.damageReportId != null && !this.damageReportId.equals(other.damageReportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DamageReport[ id=" + damageReportId + " ]";
    }
    
}
