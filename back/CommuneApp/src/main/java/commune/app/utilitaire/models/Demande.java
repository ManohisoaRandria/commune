/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commune.app.utilitaire.models;

/**
 *
 * @author P11A-MANOHISOA
 */
public class Demande {
    private String idU;
    private int nbCopie;

    public Demande() {
    }

    public Demande(String idU, int nbCopie) {
        this.idU = idU;
        this.nbCopie = nbCopie;
    }

    public int getNbCopie() {
        return nbCopie;
    }

    public void setNbCopie(int nbCopie) {
        this.nbCopie = nbCopie;
    }

    public String getIdU() {
        return idU;
    }

    public void setIdU(String idU) {
        this.idU = idU;
    }
}
