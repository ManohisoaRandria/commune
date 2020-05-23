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
    
    public final static int AGE_MIN_AUTHORIZED=18;
    
    public final static String EMAIL_AND_PHONE_FORMAT_ERROR="Veuillez entrez un Numero de téléphone ou une Adresse Email Valide";
    
    public final static String PHONE_INVALID_FORMAT_ERROR="Veuillez entrez un Numero de Téléphone Valide";
    
    public final static int PHONE_NUMBER_LENGTH=9; ///sans le 0 ou le 261 
    
    public final static String MAIL_DOUBLON_ERROR="Adresse Email Déjà utilisé ";
    
    public final static String PHONE_DOUBLON_ERROR="Numero de téléphone déjà utilisé ";
    
    public final static String DATE_NAISSANCE_ERROR="Date de naissance Invalide: "+AGE_MIN_AUTHORIZED+" ans minimun";
    
    public final static String ID_USERS_STARTS="USR";
    
    public final static int SEQUENCE_LENGTH=4;
    
    public final static String REGEX_PATTERN_LETTRE="[a-zA-Z]";
    
    public final static String REGEX_PATTERN_CHIFFRE="[0-9]";
    
    public final static String REGEX_PATTERN_SPECIAUX="[^a-zA-Z|^0-9]";
    
    public final static int PASSWORD_LETTRE_REQUIS=6;
    
    public final static int PASSWORD_CHIFFRE_REQUIS=2;
    
    public final static int PASSWORD_SPECIAUX_REQUIS=1;
    
    public final static String MESSAGE_ERROR_PASSWORD_CHANGE="Mot de passe requis: "+PASSWORD_CHIFFRE_REQUIS+" Chiffre(s), "+PASSWORD_LETTRE_REQUIS+" Lettre(s), "+PASSWORD_SPECIAUX_REQUIS+" Caractère(s) spéciaux requis";
    
    public final static String ID_MDP_STARTS="PASS";
    
    public final static String ID_VOYAGE_STARTS="VOYG";
    
    public final static String DATE_VOYAGE_INVALID="Date de départ incorrecte/Impossible ! Marge d'entrée de Voyage : "+Constantes.DELAI_MIN_RESERVATION+" Jours";
    
    public final static String WARNING_DEPART_VOYAGE="La voiture n'est actuellement pas à l'endroit de départ du voyage que vous avez planifié. Continuer? ";
    
    public final static String WARNING_MULTIPLE_VOYAGE="Cette Voiture a déjà un/plusieurs voyage(s) de prévu à cette date. Continuer? ";
    
    public final static String WARNING_DOUBLON_VOYAGE="Cette Voiture a déjà un voyage de prévu à cette date pour ce trajet ";
    
    public final static int DUREE_MIN_VOYAGE=6; ///heure; entre chaque voyage
    
    public final static int ETAT_VALID=1;
    
    public final static int ETAT_ANNULEE=0;
    
    public final static int DELAI_MIN_RESERVATION=1;///jours ;ohatra oe rapitso misy voyage prévu donc androany iny farany afaka deployena oe misy voyage rapitso
    
    public final static String AJOUT_VOYAGE_INVALID="Marge minimale entre chaque Voyage pour un véhicule : "+Constantes.DUREE_MIN_VOYAGE+" Heures";
    
    public final static String ITINERAIRE_NON_RELIEE_BORNE_INFERIEUR="La destination du précedent voyage et le départ du voyage prévue ne concordent pas ! Continuer ?";
    
    public final static String ITINERAIRE_NON_RELIEE_BORNE_SUPERIEUR="La destination de ce voyage et le départ du prochain voyage ne concordent pas ! Continuer ?";
    
    public final static String ITINERAIRE_NON_RELIEE_BORNE="La destination du précedent voyage et le départ du voyage prévue, et/ou la destination de ce voyage et le départ du prochain voyage ne concordent pas ! Continuer ?";

    public final static String ID_USER_TOKEN="UT";
    
    public final static int TOKEN_VALIDE=1;
    
    public final static int TOKEN_ANNULER=0;
    
    public final static String DATA_ERROR="Données incorrectes ou incomplètes ";
    
    public final static String ID_CONFIG="CFG";
    
    public final static String INSERT_OK="Insert OK";
    
    public final static String GET_OK="Success";
    
    public final static String DELETE_OK="Suppression OK";
    
    public final static int DEFAULT_VALIDE=1;
    
    public final static int DEFAULT_DELETE=0;
    
    public final static int DEFAULT_PAYEE=2;
    
    public final static String ID_OPTION_VOYAGE="OPTV";
    
    public final static int TOKEN_EXPIRATION=50;//minute
    
    public final static String HOST="ec2-54-247-181-232.eu-west-1.compute.amazonaws.com";
    
    public final static String USERNAME="ckvgmwzxdoasja";
    
    public final static String DBNAME="d8j26pe300hauf";
    
    public final static String PASSWORD="49f929df0d11fb4af3c3a4dcce3b41cfb08bc8c8879e88bcb8d6cdd4da731269";
    
    public final static int PORT=5432;
    
    public final static String SGBD="postgres";
    
    public final static String ID_CLASSE_VEHICULE="CV";
    
    public final static String CLASSE_VEHICULE_ERROR="Nom de Classe déjà existante ";
    
    public final static String UPDATE_OK="Modification OK";
    
    public final static String DELETE_CLASSE_VEHICULE_ERROR="Impossible de supprimer cette Classe car des véhicules y sont encore liés";
    
    public final static String ID_DESTINATION="DE";
    
    public final static String ID_MARQUE_VEHICULE="MV";
    
    public final static String ID_OPERATEUR="OPE";
    
    public final static String MARQUE_DATA_ERROR="Marque Vide";
    
    public final static String MARQUE_ERROR="Marque déjà existante ";
    
    public final static String TABLE_MARQUE_VEHICULE="marquevehicule";
    
    public final static String UPDATE_NON_VALIDEE="Aucune Modification à effectuer";
    
    public final static String TABLE_VEHICULE="Vehicule";
    
    public final static String MARQUE_ERROR_DELETE="Impossible de supprimer cette Marque car des véhicules y sont encore liés";
    
    public final static String TABLE_CLASSE_VEHICULE="classeVehicule";
    
    public final static String CLASSE_ERROR_ID="Donnée incorrecte (idClasseVehicule)";
    
    public final static String REGEX_EMAIL="^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    
    public final static String IMMATRICULATION_ERROR="Immatriculation déjà existante ";
    
    public final static String ID_VEHICULE="VEH";
    
    public final static String ID_PLACE="PLA";
    
    public final static String TABLE_PLACE="Place";
    
    public final static String TABLE_VOYAGE="Voyage";
    
    public final static String TABLE_TRAJET="trajet";
    
    public final static String ENLEVER_PLACE="enlever";
    
    public final static String VALIDER_PLACE="valider";
    
    public final static String PLACE_ETAT_INVALID="Path invalide pour la modification de place ";
    
    public final static String TABLE_DESTINATION="destination";
    
    public final static String DESTINATION_DOUBLON_ERROR="Destination déjà existante";
    
    public final static String DELETE_DESTINATION_ERROR="Impossible à  supprimer car destination encore présente dans des trajets ";
    
    public final static String ID_TRAJET="TR";
    
    public final static String TABLE_BILLET="Billet";
    
    public final static String ENLEVER_VOYAGE_ERROR="Impossible d'annuler ce voyage car des réservations ont déjà été effectué ";
    
    public final static String ID_TARIF="TA";
    
    public final static String LOGIN_FAILED="Login failed, vérifier votre Email/Numero et/ou votre mot de passe";
    
    public final static String LOGIN_SUCCESS="Login Success";
    
    public final static int NIVEAU_SUPER_ADMIN=1;
    
    public final static int NIVEAU_ADMIN_SIMPLE=2;
    
    public final static String AUTHORIZATION_REFUSED="Vous n'avez pas les droits nécessaires pour modifier/accéder à ce contenu veuillez vous authentifier";
    
    public final static String PROFIL_ADMIN_ERROR="Cet Utilisateur est déjà un administrateur ";
    
    public final static String ID_ADMIN="ADMIN";
    
    public final static String TABLE_ADMIN="ProfilAdmin";
    
    public final static String ID_BILLET="BI";
    
    public final static String ID_PLACE_RESERVEE="PR";
    
    public final static String ID_PAIEMENT="PA";
  
}
