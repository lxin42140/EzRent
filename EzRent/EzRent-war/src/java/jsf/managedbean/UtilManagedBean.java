package jsf.managedbean;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;



@Named(value = "utilManagedBean")
@RequestScoped

public class UtilManagedBean
{
    public UtilManagedBean()
    {
    }
    
    
    
    public String formatMotdHeader(Date date)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
        return simpleDateFormat.format(date);
    }
}
