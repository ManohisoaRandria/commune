/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commune.app.utilitaire.fonction;

/**
 *
 * @author Mirantolalaina
 */
public class Constantes {
    
    public final static int SEQUENCE_LENGTH=4;
    
    public final static String REGEX_PATTERN_LETTRE="[a-zA-Z]";
    
    public final static String REGEX_PATTERN_CHIFFRE="[0-9]";
    
    public final static String REGEX_PATTERN_SPECIAUX="[^a-zA-Z|^0-9]";
    
    public final static String EMAIL_FORMAT_ERROR="Veuillez entrez une Adresse Email Valide";
    
    public final static String MAIL_DOUBLON_ERROR="Adresse Email Déjà utilisé ";
    
    public final static int ETAT_VALID=1;
    
    public final static int ETAT_ANNULEE=20;
    
    public final static int ETAT_VITA_SONIA=10;
    
    public final static int TOKEN_VALIDE=1;
    
    public final static int TOKEN_ANNULER=11;
    
    public final static int TOKEN_EXPIRATION=50;//minute
    
    public final static String INSERT_OK="Insert OK";
    
    public final static String GET_OK="Success";
    
    public final static String DELETE_OK="Suppression OK";
    
    public final static String HOST="ec2-54-247-181-232.eu-west-1.compute.amazonaws.com";
    
    public final static String USERNAME="ckvgmwzxdoasja";
    
    public final static String DBNAME="d8j26pe300hauf";
    
    public final static String PASSWORD="49f929df0d11fb4af3c3a4dcce3b41cfb08bc8c8879e88bcb8d6cdd4da731269";
    
    public final static int PORT=5432;
    
    public final static String SGBD="postgres";
    
    public final static String REGEX_EMAIL="^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
  
    public final static String LOGIN_FAILED="Login failed, vérifier votre Email/Numero et/ou votre mot de passe";
    
    public final static String LOGIN_SUCCESS="Login Success";
    
    public final static int NIVEAU_SUPER_ADMIN=1;
    
    public final static int NIVEAU_ADMIN_SIMPLE=2;
    
    public final static String AUTHORIZATION_REFUSED="Vous n'avez pas les droits nécessaires pour modifier/accéder à ce contenu veuillez vous authentifier";
  
    public final static String ID_DEMANDE_COPIE="DMDCP";
    public final static int PASSWORD_MIN=4;
  
}
