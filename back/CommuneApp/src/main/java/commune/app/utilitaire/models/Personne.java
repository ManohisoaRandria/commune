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
import java.sql.Time;

/**
 *
 * @author P11A-MANOHISOA
 */
@Classe(table = "Personne")
public class Personne {
    @PrimaryKey
    @Attribut(colonne = "id")
    private String id;
     @Attribut(colonne = "idUnique")
    private String idUnique;
     @Attribut(colonne = "nom")
    private String nom;
     @Attribut(colonne = "prenom")
    private String prenom;
     @Attribut(colonne = "dateNaissance")
    private Date dateNaissance;
     @Attribut(colonne = "lieuNaissance")
    private String lieuNaissance;
     @Attribut(colonne = "heureNaissance")
    private Time heureNaissance;
     @Attribut(colonne = "pere")
    private String pere;
     @Attribut(colonne = "mere")
    private String mere;
     @Attribut(colonne = "idCommune")
    private String idCommune;

    public Personne() {
    }

    public Personne(String id, String idUnique, String nom, String prenom, Date dateNaissance, String lieuNaissance, Time heureNaissance, String pere, String mere, String idCommune) {
        this.id = id;
        this.idUnique = idUnique;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.lieuNaissance = lieuNaissance;
        this.heureNaissance = heureNaissance;
        this.pere = pere;
        this.mere = mere;
        this.idCommune = idCommune;
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

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public Time getHeureNaissance() {
        return heureNaissance;
    }

    public void setHeureNaissance(Time heureNaissance) {
        this.heureNaissance = heureNaissance;
    }

    public String getPere() {
        return pere;
    }

    public void setPere(String pere) {
        this.pere = pere;
    }

    public String getMere() {
        return mere;
    }

    public void setMere(String mere) {
        this.mere = mere;
    }

    public String getIdCommune() {
        return idCommune;
    }

    public void setIdCommune(String idCommune) {
        this.idCommune = idCommune;
    }
}
