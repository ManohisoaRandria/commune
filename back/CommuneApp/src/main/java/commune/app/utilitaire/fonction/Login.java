/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commune.app.utilitaire.fonction;

import commune.app.utilitaire.models.UserToken;
import commune.app.utilitaire.models.UserCommune;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

/**
 *
 * @author P11A-MANOHISOA
 */
public class Login {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@";
    private static final Random RANDOM = new SecureRandom();
    private String email;
    private String mdp;
    private String idUser;

    public Login(String email, String mdp) {
        this.email = email;
        this.mdp = mdp;
    }

    public Login() {
    }

    //admin na normal no type
    public boolean signIn(Connection con) throws Exception {
        try {
            String mdpHash=Utilitaire.getSecurePassword(this.mdp);
            UserCommune[] user = (UserCommune[]) GeneriqueDAO.select(UserCommune.class, " where lower(email)=lower('" + this.email.trim() + "') and mdp='" + mdpHash+ "'", con);
            if(user.length != 0)this.idUser=user[0].getId();
            return user.length != 0;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public String insertToken(Connection con) throws Exception {
        try {
            //invaliderna daholo aloha ze token mbola misy any de aveo zay vo mamorona token vaovao
            deconnex(this.idUser, con);

            String token = Utilitaire.getSecurePassword(generateRandomIdentifier(20) + Utilitaire.getCurrentTimeStamp().toString());
            Timestamp creation = Utilitaire.getCurrentTimeStamp();
            Timestamp expiration = Utilitaire.getTimeStamp(creation, Constantes.TOKEN_EXPIRATION);
            UserToken tok = new UserToken("", this.idUser, creation, token, expiration, Constantes.TOKEN_VALIDE);
            tok.setId("TOK" + Utilitaire.formatNumber(Utilitaire.getsequence("UserToken", con), 4));

            GeneriqueDAO.insert(tok, false, con);

            return token;
        } catch (Exception ex) {
            throw ex;
        }
    }

    //mamorona random token,mampiasa anle variable static ary ambony ar
    //le length ny alavanle izy
    public static String generateRandomIdentifier(int pwdLength) {
        StringBuilder retour = new StringBuilder(pwdLength);
        for (int i = 0; i < pwdLength; i++) {
            retour.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(retour);
    }

    //true raha lany daty na tsy misy anaty base na vide
    public static boolean checkToken(String token, Connection con) throws Exception {

        try {
            if (token.equals("")) {
                return true;
            }
            UserToken[] ut = (UserToken[]) GeneriqueDAO.select(UserToken.class, " where token='" + token + "' and etat=" + Constantes.TOKEN_VALIDE, con);

            if (ut.length != 0) {
                if (ut[0].getExpiration().compareTo(Utilitaire.getCurrentTimeStamp()) <= 0) {
                    invalidateToken(ut[0], con);
                    return true;
                } else {
                    return false;
                }

            }
            return true;
        } catch (Exception ex) {
            throw ex;
        }

    }

    public static String protectionPage(HttpServletRequest request) throws Exception {
        Connection con = null;
        String token = null;
        try {
            con = GeneriqueDAO.getConnection();
            token = protectionPage(request, con);
        } catch (Exception e) {
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return token;
    }

    public static String protectionPage(HttpServletRequest request, Connection c) throws Exception {
        String token = null;
        try {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorization == null || (authorization != null && authorization.equalsIgnoreCase(""))) {
                throw new Exception(Constantes.AUTHORIZATION_REFUSED);
            } else {
                if (authorization.trim().contains("Bearer")) {

                    authorization = authorization.trim().replace("Bearer", "");

                    //lany daty
                    if (checkToken(authorization.trim(), c)) {
                        throw new Exception(Constantes.AUTHORIZATION_REFUSED);
                    }
                    token = authorization;
                } else {
                    c = GeneriqueDAO.getConnection();
                    //lany daty
                    if (checkToken(authorization.trim(), c)) {
                        throw new Exception(Constantes.AUTHORIZATION_REFUSED);
                    }
                    token = authorization;
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
        return token;
    }

    public static void invalidateToken(UserToken token, Connection con) throws Exception {
        try {
            token.setEtat(Constantes.TOKEN_ANNULER);
            GeneriqueDAO.update(token, false, con);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void deconnex(String iduser, Connection con) throws Exception {
        try {
            UserToken[] ut = (UserToken[]) GeneriqueDAO.select(UserToken.class, " where idusercommune='" + iduser + "' and etat=" + Constantes.TOKEN_VALIDE, con);
            for (UserToken temp : ut) {
                invalidateToken(temp, con);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
}
