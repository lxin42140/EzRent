package jsf.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;



@FacesValidator(value = "reorderQuantityValidator")

public class ReorderQuantityValidator implements Validator
{
    public ReorderQuantityValidator()
    {
    }
    
    
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        Integer intValue = 0;
        
        try
        {
            if(value != null && value.toString().trim().length() > 0)
            {
                if(value instanceof Integer)
                {
                    intValue = (Integer)value;
                }
                else if(value instanceof String)
                {
                    intValue = Integer.valueOf((String)value);
                }
                
                if(intValue < 0)
                {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Reorder Quantity must be at least 0 or more", null));
                }
                
                if((intValue % 10) != 0)
                {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Reorder Quantity must be stated in multiples of 10, e.g., 10, 100 or 1,000", null));
                }
            }
            else
            {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Reorder Quantity is required", null));
            }
        }
        catch(NumberFormatException ex)
        {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Reorder Quantity must be an integer number", null));
        }
        
    }
}