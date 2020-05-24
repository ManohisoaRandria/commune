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
 * @author Manohisoa
 */
@Classe(table = "UserToken")
public class UserToken {
    @PrimaryKey
    @Attribut(colonne = "id")
    private String id;
    
    @Attribut(colonne = "idUserCommune")
    private String idUserCommune;
    
    @Attribut(colonne = "creation")
    private Timestamp creation;
    
    @Attribut(colonne = "token")
    private String token;
    
    @Attribut(colonne = "expiration")
    private Timestamp expiration;
    
    @Attribut(colonne = "etat")
    private int etat;

    public UserToken(String id, String idUserCommune, Timestamp creation, String token, Timestamp expiration, int etat) {
        this.id = id;
        this.idUserCommune = idUserCommune;
        this.creation = creation;
        this.token = token;
        this.expiration = expiration;
        this.etat = etat;
    }
    
    public UserToken() {
    }

   

    public Timestamp getExpiration() {
        return expiration;
    }

    public void setExpiration(Timestamp expiration) {
        this.expiration = expiration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUserCommune() {
        return idUserCommune;
    }

    public void setIdUserCommune(String idUserCommune) {
        this.idUserCommune = idUserCommune;
    }

    public Timestamp getCreation() {
        return creation;
    }

    public void setCreation(Timestamp creation) {
        this.creation = creation;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }
    
    
}
