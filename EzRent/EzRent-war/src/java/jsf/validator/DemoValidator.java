/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Li Xin
 */
@FacesValidator(value = "lengthValidator")
public class DemoValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        if (value != null) {
            if (value instanceof String) {
                String stringValue = (String) value;

                if (stringValue.trim().length() < 8) {
                    FacesMessage msg = new FacesMessage("Attribute length must be at least 8 characters", null);
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);

                    throw new ValidatorException(msg);
                }
            }
        }
    }
}
