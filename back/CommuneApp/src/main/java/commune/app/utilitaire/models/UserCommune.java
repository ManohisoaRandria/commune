/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commune.app.utilitaire.models;

import commune.app.utilitaire.annotation.Attribut;
import commune.app.utilitaire.annotation.Classe;
import commune.app.utilitaire.annotation.PrimaryKey;

/**
 *
 * @author Manohisoa
 */
@Classe(table = "UserCommune")
public class UserCommune {
     @PrimaryKey
    @Attribut(colonne = "id")
    private String id;
     @Attribut(colonne = "nom")
    private String nom;
     @Attribut(colonne = "prenom")
    private String prenom;
     @Attribut(colonne = "mdp")
    private String mdp;
     @Attribut(colonne = "idCommune")
    private String idCommune;
      @Attribut(colonne = "idDroit")
    private String idDroit;

    public UserCommune() {
    }

    public UserCommune(String id, String nom, String prenom, String mdp, String idCommune, String idDroit) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.mdp = mdp;
        this.idCommune = idCommune;
        this.idDroit = idDroit;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getIdCommune() {
        return idCommune;
    }

    public void setIdCommune(String idCommune) {
        this.idCommune = idCommune;
    }
   
    
}
