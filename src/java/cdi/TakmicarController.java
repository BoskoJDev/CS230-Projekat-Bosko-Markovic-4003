package cdi;

import cdi.util.JsfUtil;
import cdi.util.PaginationHelper;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("takmicarController")
@SessionScoped
public class TakmicarController implements Serializable {

    private Takmicar current;
    private DataModel items = null;
    @EJB
    private cdi.TakmicarFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    private static final String[] geografijaOdgovori = {
        "ekumena", "5", "ekvator", "erozija", "vojvodina"
    };
    private static final String[] istorijaOdgovori = {
        "Martin Luter", "dusan", "1217", "podela", "1371"
    };
    private static final String[] biologijaOdgovori = {
        "ameba", "uvo", "Carls Darvin", "gvozdje", "list"
    };
    private static final String[] fizikaOdgovori = {
        "metar", "drugi", "9.81", "Ne", "izotermno"
    };
    
    public TakmicarController() {
    }

    public String proveraUlaza(Takmicar t)
    {
        Takmicar temp = this.getTakmicar(t.getIme());
        if (temp == null)
            return "index";
        
        if (!temp.getSifra().equals(t.getSifra()))
            return "index";
        
        return "oblasti";
    }
    
    public String navigacijaZaProveru(Takmicar t)
    {
        String[] odgovori = {
            t.getPrviOdgovor(), t.getDrugiOdgovor(), t.getTreciOdgovor(),
            t.getCetvrtiOdgovor(), t.getPetiOdgovor()
        };
        String[] odgovoriIzOblasti = null;
        switch (t.getOblast())
        {
            case "geografija": odgovoriIzOblasti = geografijaOdgovori;
                break;
            case "istorija": odgovoriIzOblasti = istorijaOdgovori;
                break;
            case "biologija": odgovoriIzOblasti = biologijaOdgovori;
                break;
            case "fizika": odgovoriIzOblasti = fizikaOdgovori;
                break;
        }
        
        int poeni = ((Takmicar) getItems().getRowData()).getPoeni();
        for (int i = 0; i < odgovori.length; i++)
            poeni += odgovori[i].equalsIgnoreCase(odgovoriIzOblasti[i]) ? 1 : 0;
        
        System.out.println(poeni);
        t.setPoeni(poeni);
        
        return "prikazUspeha";
    }
    
    public String navigacijaDoOblasti(Takmicar t)
    {
        String oblast = t.getOblast();
        switch (oblast)
        {
            case "geografija":
                {
                    t.setDrugiOdgovor("3");
                }
                break;
            case "istorija":
                {
                    t.setTreciOdgovor("1217");
                    t.setCetvrtiOdgovor("ubistvo");
                }
                break;
            case "biologija":
                {
                    t.setCetvrtiOdgovor("cink");
                    t.setPetiOdgovor("koren");
                }
                break;
            case "fizika":
                {
                    t.setDrugiOdgovor("prvi");
                    t.setPetiOdgovor("izotermno");
                }
                break;
        }
        
        return oblast;
    }
    
    public Takmicar getSelected() {
        if (current == null) {
            current = new Takmicar();
            selectedItemIndex = -1;
        }
        return current;
    }

    private TakmicarFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Takmicar) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Takmicar();
        selectedItemIndex = -1;
        return "index";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TakmicarCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(Takmicar takmicar) {
        //current = (Takmicar) getItems().getRowData();
        current = takmicar;
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "takmicar/List";
    }

    public String update(Takmicar t) {
        try {
            getFacade().edit(t);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TakmicarUpdated"));
            return prepareEdit(t);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Takmicar) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TakmicarDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Takmicar getTakmicar(java.lang.String id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Takmicar.class)
    public static class TakmicarControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TakmicarController controller = (TakmicarController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "takmicarController");
            return controller.getTakmicar(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Takmicar) {
                Takmicar o = (Takmicar) object;
                return getStringKey(o.getIme());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Takmicar.class.getName());
            }
        }

    }

}
