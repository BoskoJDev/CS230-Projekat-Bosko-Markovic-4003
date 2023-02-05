package cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Bosko
 */
@Named
@ApplicationScoped
@Entity
@XmlRootElement
public class Takmicar implements java.io.Serializable
{
    @Id
    private String ime;
    
    private String sifra;
    private String oblast;
    private String prviOdgovor;
    private String drugiOdgovor;
    private String treciOdgovor;
    private String cetvrtiOdgovor;
    private String petiOdgovor;
    private int poeni;
    
    private static final String[] geografijaOdgovori = {
        "ekumena", "5", "ekvator", "erozija", "Vojvodina"
    };
    private static final String[] istorijaOdgovori = {
        "Martin Luter", "Dusan Nemanjic", "1217", "Nova podela sveta", "1371"
    };
    private static final String[] biologijaOdgovori = {
        "Ameba", "Uvo", "Carls Darvin", "gvozdje", "list"
    };
    private static final String[] fizikaOdgovori = {
        "metar", "drugi", "9.81", "Ne", "izotermno"
    };
    
    public String proveraOdgovora()
    {
        switch (this.oblast)
        {
            case "geografija": this.saberiPoene(geografijaOdgovori);
                break;
            case "istorija": this.saberiPoene(istorijaOdgovori);
                break;
            case "biologija": this.saberiPoene(biologijaOdgovori);
                break;
            case "fizika": this.saberiPoene(fizikaOdgovori);
                break;
        }
        
        this.izbrisiPrethodnoUneto();
        return "prikazUspeha";
    }
    
    private void izbrisiPrethodnoUneto()
    {
        this.oblast = "";
        this.prviOdgovor = "";
        this.drugiOdgovor = "";
        this.treciOdgovor = "";
        this.cetvrtiOdgovor = "";
        this.petiOdgovor = "";
    }
    
    private void saberiPoene(String[] nizOdgovora)
    {
        final String[] odgovori = {
            this.prviOdgovor, this.drugiOdgovor, this.treciOdgovor,
            this.cetvrtiOdgovor, this.petiOdgovor
        };
        
        for (int i = 0; i < nizOdgovora.length; i++)
        {
            if (odgovori[i].equalsIgnoreCase(nizOdgovora[i]))
                this.poeni++;
        }
    }
    
    public void setIme(String ime) { this.ime = ime; }
    
    public String getIme() { return this.ime; }

    public void setOblast(String oblast) { this.oblast = oblast; }

    public String getOblast() { return this.oblast; }

    public void setPoeni(int poeni) { this.poeni = poeni; }
    
    public int getPoeni() { return this.poeni; }

    public void setSifra(String sifra) { this.sifra = sifra; }
    
    public String getSifra() { return this.sifra; }

    public void setPrviOdgovor(String odgovor) { this.prviOdgovor = odgovor; }
    
    public String getPrviOdgovor() { return this.prviOdgovor; }

    public void setDrugiOdgovor(String odgovor) { this.drugiOdgovor = odgovor; }

    public String getDrugiOdgovor() { return this.drugiOdgovor; }

    public void setTreciOdgovor(String odgovor) { this.treciOdgovor = odgovor; }

    public String getTreciOdgovor() { return this.treciOdgovor; }

    public void setCetvrtiOdgovor(String odgovor) { this.cetvrtiOdgovor = odgovor; }

    public String getCetvrtiOdgovor() { return this.cetvrtiOdgovor; }

    public void setPetiOdgovor(String odgovor) { this.petiOdgovor = odgovor; }

    public String getPetiOdgovor() { return this.petiOdgovor; }
}