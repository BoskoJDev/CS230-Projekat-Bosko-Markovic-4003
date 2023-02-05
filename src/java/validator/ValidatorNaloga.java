package validator;

import cdi.Takmicar;
import cdi.TakmicarController;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Bosko
 */
@Named
@SessionScoped
@FacesValidator("validNalog")
public class ValidatorNaloga implements Validator
{
    private TakmicarController kontroler;
    
    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException
    {
        String nalog = o.toString();
        if (nalog.equals(""))
            throw new ValidatorException(new FacesMessage("Neostavljajte prazno polje!"));
    }
    
    public String setKontroler(TakmicarController kontroler, Takmicar t)
    {
        this.kontroler = kontroler;
        return this.kontroler.proveraUlaza(t);
    }
}