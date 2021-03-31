/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Yuxin
 */
@Named(value = "commonUtilityManagedBean")
@RequestScoped
public class CommonUtilityManagedBean {
    
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
    
    public CommonUtilityManagedBean() {
    }
    
        
    public String formatDateTime(Date date) {
        return sdf.format(date);
    }
}
