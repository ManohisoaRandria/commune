/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commune.app.utilitaire.models;

import commune.app.utilitaire.annotation.Attribut;
import commune.app.utilitaire.annotation.Classe;
import commune.app.utilitaire.annotation.PrimaryKey;
import java.sql.Timestamp;

/**
 *
 * @author P11A-MANOHISOA
 */
@Classe(table = "DelivranceCopie")
public class DelivranceCopie {
    @PrimaryKey
    @Attribut(colonne = "id")
    private String id;
    @Attribut(colonne = "idDemandeCopie")
    private String idDemandeCopie;
    @Attribut(colonne = "dateDelivrance")
    private Timestamp dateDelivrance;
    @Attribut(colonne = "etat")
    private int etat;

    public DelivranceCopie(String id, String idDemandeCopie, Timestamp dateDelivrance, int etat) {
        this.id = id;
        this.idDemandeCopie = idDemandeCopie;
        this.dateDelivrance = dateDelivrance;
        this.etat = etat;
    }

    public DelivranceCopie() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdDemandeCopie() {
        return idDemandeCopie;
    }

    public void setIdDemandeCopie(String idDemandeCopie) {
        this.idDemandeCopie = idDemandeCopie;
    }

    public Timestamp getDateDelivrance() {
        return dateDelivrance;
    }

    public void setDateDelivrance(Timestamp dateDelivrance) {
        this.dateDelivrance = dateDelivrance;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }
}
