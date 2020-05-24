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
public class Signature {
    private String base64;
    private String idDemande;

    public Signature() {
    }

    public Signature(String base64, String idDemande) {
        this.base64 = base64;
        this.idDemande = idDemande;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
