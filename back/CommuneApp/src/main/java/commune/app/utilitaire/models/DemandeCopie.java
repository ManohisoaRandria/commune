/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commune.app.utilitaire.models;

import commune.app.utilitaire.annotation.Attribut;
import commune.app.utilitaire.annotation.Classe;
import commune.app.utilitaire.annotation.PrimaryKey;
import java.sql.Date;

/**
 *
 * @author P11A-MANOHISOA
 */
@Classe(table = "DemandeCopie")
public class DemandeCopie {
    @PrimaryKey
    @Attribut(colonne = "id")
    private String id;
    @Attribut(colonne = "idPersonne")
    private String idPersonne;
    @Attribut(colonne = "dateDemande")
    private Date dateDemande;
    @Attribut(colonne = "nbCopie")
    private int nbCopie;
    @Attribut(colonne = "etat")
    private int etat;
    @Attribut(colonne = "idCommune")
    private String idCommune;
    @Attribut(colonne = "urlDown")
    private String urlDown;
    
    public DemandeCopie() {
    }

    public DemandeCopie(String id, String idPersonne, Date dateDemande, int nbCopie, int etat, String idCommune, String urlDown) {
        this.id = id;
        this.idPersonne = idPersonne;
        this.dateDemande = dateDemande;
        this.nbCopie = nbCopie;
        this.etat = etat;
        this.idCommune = idCommune;
        this.urlDown = urlDown;
    }

    public String getUrlDown() {
        return urlDown;
    }

    public void setUrlDown(String urlDown) {
        this.urlDown = urlDown;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdPersonne() {
        return idPersonne;
    }

    public void setIdPersonne(String idPersonne) {
        this.idPersonne = idPersonne;
    }

    public Date getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(Date dateDemande) {
        this.dateDemande = dateDemande;
    }

    public int getNbCopie() {
        return nbCopie;
    }

    public void setNbCopie(int nbCopie) {
        this.nbCopie = nbCopie;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public String getIdCommune() {
        return idCommune;
    }

    public void setIdCommune(String idCommune) {
        this.idCommune = idCommune;
    }

   
    
}
