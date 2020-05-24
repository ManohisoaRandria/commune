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
 * @author P11A-MANOHISOA
 */
@Classe(table = "Commune")
public class Commune {
    @PrimaryKey
    @Attribut(colonne = "id")
    private String id;
    @Attribut(colonne = "nom")
    private String nom;

    public Commune() {
    }

    public Commune(String id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
