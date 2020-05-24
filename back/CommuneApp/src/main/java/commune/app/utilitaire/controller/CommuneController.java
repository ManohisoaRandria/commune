/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commune.app.utilitaire.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commune.app.utilitaire.fonction.Constantes;
import commune.app.utilitaire.fonction.GeneriqueDAO;
import commune.app.utilitaire.fonction.Login;
import commune.app.utilitaire.fonction.Utilitaire;
import commune.app.utilitaire.models.Commune;
import commune.app.utilitaire.models.DelivranceCopie;
import commune.app.utilitaire.models.DemandeCopie;
import commune.app.utilitaire.models.JSONResponse;
import commune.app.utilitaire.models.Personne;
import commune.app.utilitaire.models.UploadResponse;
import commune.app.utilitaire.models.UserCommune;
import commune.app.utilitaire.models.UserToken;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author P11A-MANOHISOA
 */
@RestController
public class CommuneController {

    @PostMapping(path = "/genererPdf/{id}")
    public JSONResponse genererPdf(@PathVariable String id) throws JsonProcessingException, SQLException {
        JSONResponse rep = new JSONResponse();
        Connection c = null;
        try {
             c = GeneriqueDAO.getConnection();
             DemandeCopie[] demande = (DemandeCopie[]) GeneriqueDAO.select(DemandeCopie.class, " where id='" + id + "' and etat=" + Constantes.ETAT_VALID, c);
             if(demande.length==0){
                 throw new Exception("demande deja signé ou demande introuvable");
             }else{
               Personne[]pers= (Personne[]) GeneriqueDAO.select(Personne.class, " where id='" + demande[0].getIdPersonne().trim() + "'", c);
               //delivrance copie
               DelivranceCopie[]delivre= (DelivranceCopie[]) GeneriqueDAO.select(DelivranceCopie.class, " where idDemandeCopie='" +id.trim() + "'", c);
               DelivranceCopie delivreRes;
               if(delivre.length==0){
                   //mbola tsisy de mila mabotra
                   DelivranceCopie singleDelivre = new DelivranceCopie("DLCP" + Utilitaire.formatNumber(Utilitaire.getsequence("DelivranceCopie", c), 
                           Constantes.SEQUENCE_LENGTH), 
                           id, Utilitaire.getCurrentTimeStamp(), Constantes.ETAT_VALID);
                   GeneriqueDAO.insert(singleDelivre, false, c);
                   delivre= (DelivranceCopie[]) GeneriqueDAO.select(DelivranceCopie.class, " where idDemandeCopie='" +id.trim() + "'", c);
                   delivreRes=delivre[0];
               } else{
                   delivreRes=delivre[0];
               }
               rep.setCode(200);
               rep.setMessage("pdf data");
               rep.setResponse(new Object[]{pers[0],delivreRes});
               c.commit();
             }
        } catch (Exception ex) {
            if (c != null) {
                c.rollback();
            }
            rep.setCode(403);
            rep.setMessage(ex.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return rep;

    }

    @PostMapping(path = "/upload")
    public UploadResponse index(@RequestBody String inputRequest) throws JsonProcessingException {
        System.out.println(inputRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(inputRequest, headers);
        RestTemplate restTemplate = new RestTemplate();
        String responseBody = restTemplate.postForObject("http://localhost/uploadHost/uploadLink", request, String.class);
        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //read json file and convert to customer object
        UploadResponse customer = objectMapper.readValue(responseBody, UploadResponse.class);

        return customer;
    }

    @PutMapping("/updatePersonne")
    public JSONResponse setPersonne(@RequestBody Personne user) throws SQLException {
        JSONResponse rep = new JSONResponse();
        Connection c = null;
        try {
            c = GeneriqueDAO.getConnection();
            if (user.getId() == null) {
                throw new Exception("no id found");
            }
            ArrayList<String> cle = new ArrayList<>();
            ArrayList<Object> valeur = new ArrayList<>();
            if (user.getNom() != null && !user.getNom().equalsIgnoreCase("")) {
                cle.add("nom");
                valeur.add(user.getNom());
            }
            if (user.getPrenom() != null && !user.getPrenom().equalsIgnoreCase("")) {
                cle.add("prenom");
                valeur.add(user.getPrenom());
            }
            if (user.getPere() != null && !user.getPere().equalsIgnoreCase("")) {
                cle.add("pere");
                valeur.add(user.getPere());
            }
            if (user.getMere() != null && !user.getMere().equalsIgnoreCase("")) {
                cle.add("mere");
                valeur.add(user.getMere());
            }
            if (user.getLieuNaissance() != null && !user.getLieuNaissance().equalsIgnoreCase("")) {
                cle.add("lieuNaissance");
                valeur.add(user.getLieuNaissance());
            }
            if (user.getDateNaissance() != null) {
                if (user.getDateNaissance().compareTo(Date.valueOf(Utilitaire.getCurrentDate())) <= 0) {
                    cle.add("dateNaissance");
                    valeur.add(user.getDateNaissance());
                }
            }

            if (user.getHeureNaissance() != null && !user.getHeureNaissance().toString().equalsIgnoreCase("")) {
                cle.add("heureNaissance");
                valeur.add(user.getHeureNaissance());

            }
            if (user.getIdCommune() != null && !user.getIdCommune().equalsIgnoreCase("")) {
                cle.add("idCommune");
                valeur.add(user.getIdCommune());
            }

            GeneriqueDAO.update("UserCommune", cle.toArray(new String[cle.size()]), valeur.toArray(), " id='" + user.getId() + "'", c);
            rep.setCode(200);
            rep.setMessage("UPDATE_Ok");
            c.commit();
        } catch (Exception ex) {
            if (c != null) {
                c.rollback();
            }
            rep.setCode(403);
            rep.setMessage(ex.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return rep;
    }

    @PostMapping("/insertPersonne")
    public JSONResponse insertPersonne(@RequestBody Personne user) throws SQLException {
        JSONResponse rep = new JSONResponse();
        Connection c = null;
        try {
            if (user.getNom() == null || user.getDateNaissance() == null
                    || user.getPrenom() == null || user.getDateNaissance() == null || user.getIdCommune() == null
                    || user.getHeureNaissance() == null || user.getPere() == null || user.getMere() == null) {
                throw new Exception("erreur de donnee");
            }

            c = GeneriqueDAO.getConnection();
            if (user.getNom().equals("") || user.getPrenom().equals("")) {
                throw new Exception("nom ou prenom non valide");
            }
            if (user.getPere().equals("") || user.getMere().equals("")) {
                throw new Exception("pere ou mere non valide");
            }
            if (user.getIdCommune().equals("")) {
                throw new Exception("commune non valide");
            }
            if (user.getLieuNaissance().equals("")) {
                throw new Exception("lieu de naissance non valide");
            }
            if (user.getDateNaissance().compareTo(Date.valueOf(Utilitaire.getCurrentDate())) > 0) {
                throw new Exception("date de naissance non valide");
            }
            if (user.getHeureNaissance().toString().equalsIgnoreCase("")) {
                throw new Exception("heure de naissance non valide");
            }

            user.setId("PERS" + Utilitaire.formatNumber(Utilitaire.getsequence("Personne", c), Constantes.SEQUENCE_LENGTH));
            user.setIdUnique(Login.generateRandomIdentifier(10) + Utilitaire.getCurrentTime().substring(6, 8));
            GeneriqueDAO.insert(user, false, c);
            rep.setCode(200);
            rep.setMessage(Constantes.INSERT_OK);
            c.commit();

        } catch (Exception ex) {
            if (c != null) {
                c.rollback();
            }
            rep.setCode(500);
            rep.setMessage(ex.getMessage());
            rep.setResponse(null);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return rep;
    }

    @GetMapping("/getUserCommune/{id}")
    public JSONResponse getUserByCommune(@PathVariable(required = false) String id) throws Exception {
        JSONResponse rep = new JSONResponse();
        Connection c = null;
        try {
            c = GeneriqueDAO.getConnection();
            Object[] o;
            o = GeneriqueDAO.select(UserCommune.class, " where idCommune='" + id.trim() + "'", c);
            rep.setCode(200);
            rep.setMessage(Constantes.GET_OK);
            rep.setResponse(o);
        } catch (Exception ex) {
            rep.setCode(500);
            rep.setMessage(ex.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return rep;
    }

    @GetMapping(value = {"/getUser", "/getUser/{id}"})
    public JSONResponse getUser(@PathVariable(required = false) String id) throws Exception {
        JSONResponse rep = new JSONResponse();
        Connection c = null;
        try {
            c = GeneriqueDAO.getConnection();
            Object[] o;
            if (id == null) {
                o = GeneriqueDAO.select(UserCommune.class, null, c);
            } else {
                o = GeneriqueDAO.select(UserCommune.class, " where id='" + id.trim() + "'", c);
            }
            rep.setCode(200);
            rep.setMessage(Constantes.GET_OK);
            rep.setResponse(o);
        } catch (Exception ex) {
            rep.setCode(500);
            rep.setMessage(ex.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return rep;
    }

    @GetMapping("/getPersonneCommune/{id}")
    public JSONResponse getPersonneByCommune(@PathVariable(required = false) String id) throws Exception {
        JSONResponse rep = new JSONResponse();
        Connection c = null;
        try {
            c = GeneriqueDAO.getConnection();
            Object[] o;
            o = GeneriqueDAO.select(Personne.class, " where idCommune='" + id.trim() + "'", c);
            rep.setCode(200);
            rep.setMessage(Constantes.GET_OK);
            rep.setResponse(o);
        } catch (Exception ex) {
            rep.setCode(500);
            rep.setMessage(ex.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return rep;
    }

    @GetMapping(value = {"/getPersonne", "/getPersonne/{id}"})
    public JSONResponse getPersonne(@PathVariable(required = false) String id) throws Exception {
        JSONResponse rep = new JSONResponse();
        Connection c = null;
        try {
            c = GeneriqueDAO.getConnection();
            Object[] o;
            if (id == null) {
                o = GeneriqueDAO.select(Personne.class, null, c);
            } else {
                o = GeneriqueDAO.select(Personne.class, " where id='" + id.trim() + "'", c);
            }
            rep.setCode(200);
            rep.setMessage(Constantes.GET_OK);
            rep.setResponse(o);
        } catch (Exception ex) {
            rep.setCode(500);
            rep.setMessage(ex.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return rep;
    }

    @PostMapping("/signerDemande/{id}")
    public JSONResponse signerDemande(@RequestBody String sing,@PathVariable String id) throws SQLException,JsonProcessingException {
        JSONResponse rep = new JSONResponse();
        Connection c = null;
        try {
            if (id == null) {
                throw new Exception("demande non valide");
            }
            c = GeneriqueDAO.getConnection();

            //verifier si deja signer
            DemandeCopie[] demande = (DemandeCopie[]) GeneriqueDAO.select(DemandeCopie.class, " where id='" + id + "' and etat=" + Constantes.ETAT_VALID, c);

            if (demande.length != 0) {
                DelivranceCopie[]delivre= (DelivranceCopie[]) GeneriqueDAO.select(DelivranceCopie.class, " where idDemandeCopie='" +demande[0].getId().trim() + "'", c);
                if(delivre.length==0)throw new Exception("vous devez d'abord generer le pdf de la demande avant de le signer");
                
                GeneriqueDAO.update("DemandeCopie", new String[]{"etat"}, new Object[]{Constantes.ETAT_VITA_SONIA}, " id='" + id.trim() + "'", c);
                GeneriqueDAO.update("DelivranceCopie", new String[]{"etat"}, new Object[]{Constantes.ETAT_VITA_SONIA}, " id='" + delivre[0].getId().trim() + "'", c);
                //upload
                if(sing==null||(sing!=null && sing.equalsIgnoreCase("")))throw new Exception("fichier pdf non valide");
                 HttpHeaders headers = new HttpHeaders();
                    headers.set("Content-type", "application/json");
                    System.out.println(sing);
//                    String inputRequest="{\n"+"\"img\":\""+sing.getBase64()+"\"\n"+"}";
//                    System.out.println(inputRequest);
                    HttpEntity<String> request = new HttpEntity<>(sing, headers);
                    RestTemplate restTemplate = new RestTemplate();
                    String responseBody = restTemplate.postForObject("http://localhost/uploadHost/uploadLink", request, String.class);
                    System.out.println(responseBody);
                    //create ObjectMapper instance
                    ObjectMapper objectMapper = new ObjectMapper();
//
                    //read json file and convert to customer object
                UploadResponse customer = objectMapper.readValue(responseBody, UploadResponse.class);
                GeneriqueDAO.update("DemandeCopie", new String[]{"urlDown"}, new Object[]{customer.getUrl()}, " id='" + demande[0].getId().trim() + "'", c);
                rep.setCode(200);
                rep.setMessage("pdf signé");
                rep.setResponse(new Object[]{customer});
                c.commit();
            }
        } catch (Exception ex) {
            if (c != null) {
                c.rollback();
            }
            rep.setCode(403);
            rep.setMessage(ex.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return rep;
    }

    @GetMapping("/getDemandesCommune/{id}")
    public JSONResponse getDemandeByCommune(@PathVariable String id) throws SQLException {
        JSONResponse rep = new JSONResponse();
        Connection c = null;
        try {
            DemandeCopie[] demande = null;
            c = GeneriqueDAO.getConnection();
            if (id != null) {
                demande = (DemandeCopie[]) GeneriqueDAO.select(DemandeCopie.class, " where idCommune='" + id + "'", c);
            } else {
                throw new Exception("commune non valide");
            }
            rep.setCode(200);
            rep.setMessage(Constantes.GET_OK);
            rep.setResponse(demande);
        } catch (Exception e) {
            rep.setCode(500);
            rep.setMessage(e.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return rep;
    }

    @PutMapping("/updateUser")
    public JSONResponse setUserCommune(@RequestBody UserCommune user) throws SQLException {
        JSONResponse rep = new JSONResponse();
        Connection c = null;
        try {
            c = GeneriqueDAO.getConnection();
            if (user.getId() == null) {
                throw new Exception("no id found");
            }
            ArrayList<String> cle = new ArrayList<>();
            ArrayList<Object> valeur = new ArrayList<>();
            if (user.getNom() != null && !user.getNom().equalsIgnoreCase("")) {
                cle.add("nom");
                valeur.add(user.getNom());
            }
            if (user.getMdp() != null && !user.getMdp().equalsIgnoreCase("")) {
                if (user.getMdp().length() >= Constantes.PASSWORD_MIN) {
                    cle.add("mdp");
                    valeur.add(Utilitaire.getSecurePassword(user.getMdp()));
                }
            }
            if (user.getPrenom() != null && !user.getPrenom().equalsIgnoreCase("")) {
                cle.add("prenom");
                valeur.add(user.getPrenom());
            }
            if (user.getEmail() != null && !user.getEmail().equalsIgnoreCase("")) {
                if (!Utilitaire.checkEmail(user.getEmail()) && !Utilitaire.verifyIfNoMailDoublon(user.getEmail(), c)) {
                    cle.add("email");
                    valeur.add(user.getEmail());
                }

            }
            if (user.getIdCommune() != null && !user.getIdCommune().equalsIgnoreCase("")) {
                cle.add("idCommune");
                valeur.add(user.getIdCommune());
            }

            GeneriqueDAO.update("UserCommune", cle.toArray(new String[cle.size()]), valeur.toArray(), " id='" + user.getId() + "'", c);
            rep.setCode(200);
            rep.setMessage("UPDATE_Ok");
            c.commit();
        } catch (Exception ex) {
            if (c != null) {
                c.rollback();
            }
            rep.setCode(403);
            rep.setMessage(ex.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return rep;
    }

    @PostMapping("/insertUser")
    public JSONResponse insertUserCommune(@RequestBody UserCommune user) throws SQLException {
        JSONResponse rep = new JSONResponse();
        Connection c = null;
        try {
            if (user.getNom() == null || user.getMdp() == null || user.getPrenom() == null || user.getEmail() == null || user.getIdCommune() == null) {
                throw new Exception("erreur de donnee");
            }
            c = GeneriqueDAO.getConnection();
            if (user.getNom().equals("") || user.getPrenom().equals("")) {
                throw new Exception("nom ou prenom non valide");
            }
            if (user.getIdCommune().equals("")) {
                throw new Exception("commune non valide");
            }
            if (user.getMdp().equals("") || user.getMdp().length() < Constantes.PASSWORD_MIN) {
                throw new Exception("mdp non valide");
            }
            if (!Utilitaire.checkEmail(user.getEmail())) {
                throw new Exception(Constantes.EMAIL_FORMAT_ERROR);
            }
            if (!Utilitaire.verifyIfNoMailDoublon(user.getEmail(), c)) {
                throw new Exception(Constantes.MAIL_DOUBLON_ERROR);
            }

            user.setId("USRCOM" + Utilitaire.formatNumber(Utilitaire.getsequence("UserCommune", c), Constantes.SEQUENCE_LENGTH));
            user.setMdp(Utilitaire.getSecurePassword(user.getMdp()));
            user.setIdDroit("DRT2");
            GeneriqueDAO.insert(user, false, c);
            rep.setCode(200);
            rep.setMessage(Constantes.INSERT_OK);
            c.commit();

        } catch (Exception ex) {
            if (c != null) {
                c.rollback();
            }
            rep.setCode(500);
            rep.setMessage(ex.getMessage());
            rep.setResponse(null);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return rep;
    }

    @PostMapping("/insertCommune")
    public JSONResponse insertCommune(@RequestBody Commune commune) throws SQLException {
        JSONResponse rep = new JSONResponse();
        Connection c = null;
        try {
            if (commune.getNom().equals("")) {
                throw new Exception("nom comune non valide");
            }
            c = GeneriqueDAO.getConnection();

            commune.setId("COM" + Utilitaire.formatNumber(Utilitaire.getsequence("Commune", c), Constantes.SEQUENCE_LENGTH));
            GeneriqueDAO.insert(commune, false, c);
            rep.setCode(200);
            rep.setMessage(Constantes.INSERT_OK);
            c.commit();

        } catch (Exception ex) {
            if (c != null) {
                c.rollback();
            }
            rep.setCode(500);
            rep.setMessage(ex.getMessage());
            rep.setResponse(null);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return rep;
    }

    @PostMapping("/deco")
    public JSONResponse deco(HttpServletRequest req) throws SQLException {
        JSONResponse rep = new JSONResponse();
        Connection c = null;
        try {
            c = GeneriqueDAO.getConnection();
            String authorization = req.getHeader(HttpHeaders.AUTHORIZATION);
            UserToken user = (UserToken) (GeneriqueDAO.select(UserToken.class, " where token='" + authorization.trim().replace("Bearer", "").trim() + "' ", c)[0]);
            Login.invalidateToken(user, c);
            c.commit();
            rep.setCode(200);
        } catch (Exception ex) {
            rep.setCode(500);
            rep.setMessage(ex.getMessage() + " " + req.getHeader(HttpHeaders.AUTHORIZATION));
            rep.setResponse(null);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return rep;
    }

    @PostMapping("/checkAuth")
    public JSONResponse getAllMarque(HttpServletRequest req) throws SQLException {
        JSONResponse rep = new JSONResponse();
        Connection c = null;
        try {
            c = GeneriqueDAO.getConnection();
            String token = Login.protectionPage(req, c);
            UserCommune user = Utilitaire.getUserFromToken(token, c);
            rep.setCode(200);
            rep.setMessage("autorisé");
            rep.setResponse(new Object[]{user.getId(), user.getNom(), user.getPrenom(), user.getIdCommune()});
        } catch (Exception ex) {
            rep.setCode(500);
            rep.setMessage(ex.getMessage());
            rep.setResponse(null);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return rep;
    }

    /**
     *
     * @param log
     * @param id
     * @return
     * @throws SQLException
     */
    @PostMapping("/login")
    public JSONResponse getAllMarque(@RequestBody Login log) throws SQLException {
        JSONResponse rep = new JSONResponse();
        Connection c = null;
        try {
            c = GeneriqueDAO.getConnection();
            if (log.signIn(c)) {
                //retourner token
                String token = log.insertToken(c);
                rep.setCode(200);
                rep.setMessage(Constantes.LOGIN_SUCCESS);
                rep.setResponse(token);
            } else {
                rep.setCode(403);
                rep.setMessage(Constantes.LOGIN_FAILED);
                rep.setResponse(null);
            }

            c.commit();
        } catch (Exception e) {
            if (c != null) {
                c.rollback();
            }
            rep.setCode(500);
            rep.setMessage(e.getMessage());
            rep.setResponse(null);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return rep;
    }
}
