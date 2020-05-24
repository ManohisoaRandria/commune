/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commune.app.utilitaire.controller;

import commune.app.utilitaire.fonction.Constantes;
import commune.app.utilitaire.fonction.GeneriqueDAO;
import commune.app.utilitaire.fonction.Utilitaire;
import commune.app.utilitaire.models.Demande;
import commune.app.utilitaire.models.DemandeCopie;
import commune.app.utilitaire.models.JSONResponse;
import commune.app.utilitaire.models.ListeDemandeClient;
import commune.app.utilitaire.models.Personne;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author P11A-MANOHISOA
 */
@RestController
public class ClientController {
    @GetMapping(path ="/apropos/{idUnique}")
    public JSONResponse getProfilById(@PathVariable String idUnique) throws SQLException{
           
        JSONResponse rep = new JSONResponse();
         Connection c=null;
            try {
                c=GeneriqueDAO.getConnection();
                Object[] o;
                if(idUnique==null) {
                    throw new Exception("id unique absent");
                }else {
                    o=GeneriqueDAO.select(Personne.class, " where idUnique='"+idUnique.trim()+"'", c);
                }
                rep.setCode(200);
                rep.setMessage(Constantes.GET_OK);
                rep.setResponse(o);
            }catch(Exception ex) {
                rep.setCode(500);
                rep.setMessage(ex.toString());
            }finally {
                if(c!=null) {
                    c.close();
                }
            } 
        return rep;
    }
    @GetMapping(path ="/listeDemande/{id}")
    public JSONResponse getListeDemande(@PathVariable String id) throws SQLException{
           
        JSONResponse rep = new JSONResponse();
         Connection c=null;
            try {
                c=GeneriqueDAO.getConnection();
                ListeDemandeClient[] o;
                if(id==null) {
                    throw new Exception("id unique absent");
                }else {
                    o=(ListeDemandeClient[])GeneriqueDAO.select(ListeDemandeClient.class, " where idUnique='"+id.trim()+"'", c);
                    rep.setCode(200);
                    rep.setMessage(Constantes.GET_OK);
                    rep.setResponse(o);
                }
                
            }catch(Exception ex) {
                rep.setCode(500);
                rep.setMessage(ex.toString());
            }finally {
                if(c!=null) {
                    c.close();
                }
            } 
        return rep;
    }
     @PostMapping(path ="/faireDemande")
    public JSONResponse faireDemande(@RequestBody Demande demande) throws SQLException{
           
        JSONResponse rep = new JSONResponse();
         Connection c=null;
            try {
                c=GeneriqueDAO.getConnection(); 
                Personne[]p= (Personne[])GeneriqueDAO.select(Personne.class, " where idUnique='"+demande.getIdU().trim()+"'", c);
                if(p.length!=0){
                    if(demande.getNbCopie()>3){
                        throw new Exception("le nombre de copie demandé est superieur a 3");
                    }else{
                        //verification si efa nanao demande vo aingana
                        Date today=Date.valueOf(Utilitaire.getCurrentDate());
                        DemandeCopie []demandeDate= (DemandeCopie[])GeneriqueDAO.select(DemandeCopie.class, " where idPersonne='"+p[0].getId()+
                                "' and dateDemande <='"+today.toString()+"' order by dateDemande desc limit 1", c);
                        if(demandeDate.length!=0){
                            int nbJours=Utilitaire.getDaysBetweenTowDates(today,demandeDate[0].getDateDemande());
                            if(nbJours>7){
                                String iddemande=Constantes.ID_DEMANDE_COPIE+Utilitaire.formatNumber(Utilitaire.getsequence("DemandeCopie", c), Constantes.SEQUENCE_LENGTH);
                                DemandeCopie demandeinsert=new DemandeCopie(iddemande, p[0].getId(), today, demande.getNbCopie(), Constantes.ETAT_VALID, p[0].getIdCommune(),"");
                                
                                //insertion
                                GeneriqueDAO.insert(demandeinsert, false, c);
                                rep.setCode(200);
                                rep.setMessage(Constantes.INSERT_OK);
                                c.commit();
                            }else{
                                throw new Exception("vous ne pouvez pas faire une demande avant "+(7-nbJours)+" jour(s)");
                            }
                        }else{
                            String iddemande=Constantes.ID_DEMANDE_COPIE+Utilitaire.formatNumber(Utilitaire.getsequence("DemandeCopie", c), Constantes.SEQUENCE_LENGTH);
                                DemandeCopie demandeinsert=new DemandeCopie(iddemande, p[0].getId(), today, demande.getNbCopie(), Constantes.ETAT_VALID, p[0].getIdCommune(),"");
                                
                                //insertion
                                GeneriqueDAO.insert(demandeinsert, false, c);
                                rep.setCode(200);
                                rep.setMessage(Constantes.INSERT_OK);
                                c.commit();
                        }
                    }
                   
                }else{
                    throw new Exception("id unique non valide");
                }
               
            }catch(Exception ex) {
                if(c!=null) c.rollback();
                rep.setCode(500);
                rep.setMessage(ex.getMessage());
            }finally {
                if(c!=null) {
                    c.close();
                }
            } 
        return rep;
    }
}
