package validator;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

/**
 *
 * @author Bosko
 */
@Named
@SessionScoped
@FacesValidator("validSifra")
public class ValidatorSifre implements Validator
{

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException
    {
        String nalog = o.toString();
        if (nalog.equals(""))
            throw new ValidatorException(new FacesMessage("Neostavljajte prazno polje!"));
    }
}