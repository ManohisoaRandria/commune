/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commune.app.utilitaire.fonction;

import commune.app.utilitaire.models.UserToken;
import commune.app.utilitaire.models.Users;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.regex.*;

/**
 *
 * @author Mirantolalaina
 */
public class Utilitaire {

    ///Heure Actuel
    public static String getCurrentTime() {
        LocalTime lt = LocalTime.now();
        return lt.toString();
    }

    ///Date Actuel 
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new java.util.Date());
        return date;
    }

    ///Date et Heure Actuel
    public static Timestamp getCurrentTimeStamp() {
        java.util.Date date = new java.util.Date();
        long time = date.getTime();
        return new Timestamp(time);
    }

    ///Date et Heure + tempsmin
    public static Timestamp getTimeStamp(Timestamp ts, int min) {
        return new Timestamp(ts.getYear(), ts.getMonth(), ts.getDate(), ts.getHours(), ts.getMinutes() + min, ts.getSeconds(), 0);
    }

    ///ToUpperCase 
    public String toUpperCase(String arg) {
        char[] name = arg.toCharArray();
        name[0] = Character.toUpperCase(name[0]);
        arg = String.valueOf(name);
        return arg;
    }

    /// Fonction pour avoir la séquence
    public static String getsequence(String nomTable, Connection c) throws Exception {
        String seq = null;
        String requete = " SELECT nextval('" + nomTable + "_SEQ') as nb";
        ResultSet rs2;
        try (Statement st2 = c.createStatement()) {
            rs2 = st2.executeQuery(requete);
            while (rs2.next()) {
                seq = rs2.getString("nb");
                break;
            }
        }
        rs2.close();
        return seq;
    }

    public static String formatNumber(String seq, int ordre) throws Exception {
        if (seq.split("").length > ordre) {
            throw new Exception("Format impossible !");
        }
        String ret = "";
        for (int i = 0; i < ordre - seq.split("").length; i++) {
            ret += "0";
        }
        return ret + seq;
    }
    public static String getSecurePassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static boolean checkEmail(String email) throws Exception {
        boolean isValid = false;
        isValid = email.matches(Constantes.REGEX_EMAIL);

        return isValid;
    }
    public static boolean checkNaissance(Date datenaiss) {
        boolean b = false;
        Date currentdate = Date.valueOf(Utilitaire.getCurrentDate());
        int currentDateenJour = (currentdate.getYear() * 365) + (currentdate.getMonth() * 30) + (currentdate.getDate());
        int datenaissenJour = (datenaiss.getYear() * 365) + (datenaiss.getMonth() * 30) + (datenaiss.getDate());
        int calc = currentDateenJour - datenaissenJour;
        if (calc <= 0) {
            //false 
        } else {
            if ((calc / 365) >= Constantes.AGE_MIN_AUTHORIZED) {
                b = true;
            }
        }
        return b;
    }
    public static boolean checkMotDePasseSeq(String mdp) {
        boolean b = false;
        Pattern patternlettre = Pattern.compile(Constantes.REGEX_PATTERN_LETTRE);
        Matcher matcher = patternlettre.matcher(mdp);
        int countlettre = 0, countchiffre = 0, countspec = 0;
        while (matcher.find()) {
            countlettre++;
        }

        Pattern patternchiffre = Pattern.compile(Constantes.REGEX_PATTERN_CHIFFRE);
        matcher = patternchiffre.matcher(mdp);

        while (matcher.find()) {
            countchiffre++;
        }
        Pattern patternspec = Pattern.compile(Constantes.REGEX_PATTERN_SPECIAUX);
        matcher = patternspec.matcher(mdp);
        while (matcher.find()) {
            countspec++;
        }

        if (countchiffre >= Constantes.PASSWORD_CHIFFRE_REQUIS && countlettre >= Constantes.PASSWORD_LETTRE_REQUIS && countspec >= Constantes.PASSWORD_SPECIAUX_REQUIS) {
            b = true;
        }
        //System.out.println(countlettre+" "+countchiffre+" "+countspec);
        return b;
    }

    public static Time getTime(Time ts, int heure) {
        ts.setHours(ts.getHours() + heure);
        return ts;
    }

    public static int getDiffTime(Time t1, Time t2) {    ///en heure
        int t11 = (t1.getSeconds()) + (t1.getMinutes() * 60) + (t1.getHours() * 60 * 60);
        int t12 = (t2.getSeconds()) + (t2.getMinutes() * 60) + (t2.getHours() * 60 * 60);
        return abs(((t11 - t12) / 60) / 60);
    }

    public static int getDiffDate(Date t1, Date t2) {    ///en heure
        int t11 = (t1.getDate()) + ((t1.getMonth() + 1) * 30) + (t1.getYear() * 12 * 30);
        int t12 = (t2.getDate()) + ((t2.getMonth() + 1) * 30) + (t2.getYear() * 12 * 30);
        return abs(t11 - t12);
    }

    public static Date getDate(Date ts, int day) {
        ts.setDate(ts.getDate() + day);
        return ts;
    }

    public static int abs(int val) {
        if (val < 0) {
            return val * -1;
        } else {
            return val;
        }
    }

    public static Users getUserFromToken(String token, Connection c) throws Exception {
        Users user = null;
        try {
           
            UserToken[] ut = (UserToken[]) GeneriqueDAO.select(UserToken.class, " where token='" + token.trim() + "' and etat=" + Constantes.TOKEN_VALIDE, c);
            if (ut.length != 0) {
               
                Users[] userTab = (Users[]) GeneriqueDAO.select(Users.class, " where iduser='" + ut[0].getIdUser() + "'", c);
                
                if (userTab.length != 0) {
                    return userTab[0];
                }

            }
        } catch (Exception e) {
            throw e;
        }

        return user;
    }
  

}
