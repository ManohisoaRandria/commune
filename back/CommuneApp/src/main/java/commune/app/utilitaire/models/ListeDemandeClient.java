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
@Classe(table = "ListeDemandeClient")
public class ListeDemandeClient {
    @PrimaryKey
    @Attribut(colonne = "id")
    private String id;
    @Attribut(colonne = "idUnique")
    private String idUnique;
    @Attribut(colonne = "dateDemande")
    private Date dateDemande;
    @Attribut(colonne = "nbCopie")
    private int nbCopie;
    @Attribut(colonne = "etat")
    private int etat;
    @Attribut(colonne = "nomCommune")
    private String nomCommune;

    public ListeDemandeClient() {
    }

    public ListeDemandeClient(String id, String idUnique, Date dateDemande, int nbCopie, int etat, String nomCommune) {
        this.id = id;
        this.idUnique = idUnique;
        this.dateDemande = dateDemande;
        this.nbCopie = nbCopie;
        this.etat = etat;
        this.nomCommune = nomCommune;
    }

    public String getNomCommune() {
        return nomCommune;
    }

    public void setNomCommune(String nomCommune) {
        this.nomCommune = nomCommune;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUnique() {
        return idUnique;
    }

    public void setIdUnique(String idUnique) {
        this.idUnique = idUnique;
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
}
