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
 * @author Mirantolalaina
 */
@Classe(table = "Users")
public class Users {
    @PrimaryKey
    @Attribut(colonne = "idUser")
    private String idUser;
    
    @Attribut(colonne = "email")
    private String email;
    
    @Attribut(colonne = "phone")
    private String phone;
    
    @Attribut(colonne = "naissance")
    private Date naissance;
    
    @Attribut(colonne = "nom")
    private String nom;
    
    @Attribut(colonne = "prenom")
    private String prenom;
    
    @Attribut(colonne = "photo")
    private String photo; 

    public Users(String idUser, String email, String phone, Date naissance, String nom, String prenom, String photo) {
        this.idUser = idUser;
        this.email = email;
        this.phone = phone;
        this.naissance = naissance;
        this.nom = nom;
        this.prenom = prenom;
        this.photo = photo;
    }

    public Users() {
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getNaissance() {
        return naissance;
    }

    public void setNaissance(Date naissance) {
        this.naissance = naissance;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    
    
}
