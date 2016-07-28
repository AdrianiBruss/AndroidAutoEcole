package com.mpdam.ronald.autoecole.models;

import java.math.BigDecimal;
import com.strongloop.android.loopback.Model;

/**
 * A widget for sale.
 */
public class Voiture extends Model {

    private String Modele;
    private String Marque;

    public void setModele(String name) {
        this.Modele = name;
    }

    public String getModele() {
        return Modele;
    }

    public void setMarque(String m) {
        this.Marque = m;
    }

    public String getMarque() {
        return Marque;
    }
}
