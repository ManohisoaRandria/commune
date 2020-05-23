/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commune.app.utilitaire.fonction;

import commune.app.utilitaire.annotation.PrimaryKey;
import commune.app.utilitaire.annotation.Attribut;
import commune.app.utilitaire.annotation.Cacheable;
import commune.app.utilitaire.annotation.Classe;
import commune.app.utilitaire.annotation.Tableau;
import commune.app.utilitaire.cache.Cache;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;


/**
 *
 * @author Mirantolalaina
 */
public class GeneriqueDAO {
    ////Pagination avec base oracle à tester
    ////Cache final static
    ///rework insert , parametre gettableau insert 
    ///Select/insert en profondeur 
    ///association
    
    public static Connection getConnection() throws Exception {
        String url="D:\\wcc\\s1\\back\\CommuneApp\\src\\main\\java\\commune\\app\\databaseConfig.xml";
        String url2="D:\\S5\\web\\projectAndTp\\projects\\clouds5\\Back\\src\\main\\java\\clouds5\\data\\databaseConfig.xml";
         String dbUrl = System.getenv("JDBC_DATABASE_URL");    
         Connection c= DriverManager.getConnection(dbUrl);
         c.setAutoCommit(false);
         return c;
//        return getConnection(url);
        //return getConnection(Constantes.DBNAME, Constantes.USERNAME, Constantes.PASSWORD, Constantes.SGBD, Constantes.PORT, Constantes.HOST);
    }
    
    ///Nom de table Clé ,HashMap* valeur
    ///valeur : HashMap<String,Cache> ; String requete, Cache (DateTime Création, Object[] result)
    private static final HashMap<String,HashMap<String,Cache>> CACHE=new HashMap<>();
    
    ///Pour Mettre tous les Charactères en minuscules
    private static String toLowerCaseString(String arg) {
        char[] name = arg.trim().toCharArray();
        for(int i=0;i<name.length;i++) {
            name[i] = Character.toLowerCase(name[i]);
        }
        arg = String.valueOf(name);
        return arg;
    }
    
    ///Chercher si le nom de table existe dans le HM
    ///key = nom table 
    private static Boolean checkKeyCache(HashMap hm,String key) {
        return hm.containsKey(key.trim());
    }
    
    ///Apres UPDATE, INSERT, ou DELETE
    ///Si la clé existe dans le HM , supprimer l'Element<K,V> correspondant
    private static void refreshCache(String key) {
        key=toLowerCaseString(key.trim());
        if(checkKeyCache(CACHE,key)) {
            ///true
            CACHE.remove(key);
        } 
    }
    
    ///Si la clé existe dans le HM , La requete existe dans la hashMap et que le cache correspondand n'est plus valide, efface la cache
    ///Assurer que la key et la requete existe dans le Hmap avant d'utiliser cette fonction
    private static boolean refreshCache(String key,String requete) {
        key=toLowerCaseString(key.trim());
        if(!checkDateCache(CACHE.get(key).get(requete))) {
            CACHE.get(key).remove(requete); 
            if(CACHE.get(key).isEmpty()) {
                CACHE.remove(key);
            }
            return true;
        }else {
            return false;
        }
    }
    
    ///checkValidité Cache, 
    private static boolean checkDateCache(Cache c) {
        boolean b=true;
        if(c.getTempexp().before(getCurrentTimeStamp()) || c.getTempexp().equals(getCurrentTimeStamp())) {
            ///expiré
            b=false;
        }
        return b;
    }
    
    ///Pour verifier si la requete existe déja dans le cache
    private static boolean checkRequete(String key,String requete){
        key=toLowerCaseString(key.trim());
        boolean rep=false;
        if(checkKeyCache(CACHE,key)) {
            ///true
            HashMap hm=CACHE.get(key);
            if(checkKeyCache(hm,requete)) {
                rep=true;
            }
        }
        return rep;
    }
    
    ///Pour recupérer les resultats depuis le cache
    private static Object[] getResultFromCache(String key,String requete) {
        Object[] o=null;
        key=toLowerCaseString(key.trim());
        if(checkRequete(key, requete)) {
            boolean b=refreshCache( key, requete);
            if(!b) {
                HashMap hm=CACHE.get(key);
                o=((Cache)hm.get(requete)).getResult();
            }
        }
        return o;
    }
    
    ///Ajouter les données de la fonction SELECT dans le cache si ils n'y sont pas
    private static void addToCache(String key,String requete,Object[] result,int mindureecache) throws Exception{
        key=toLowerCaseString(key.trim());
        if(!(result==null || result.length==0)) {
            if(checkKeyCache(CACHE,key)) {
                ///true
                CACHE.get(key).put(requete, new Cache(result, getTimeStamp(getCurrentTimeStamp(), mindureecache)));
            }else {
                ///false
                HashMap<String, Cache> inst=new HashMap<>();
                inst.put(requete, new Cache(result, getTimeStamp(getCurrentTimeStamp(), mindureecache)));
                CACHE.put(key, inst);
            } 
        }
    }
    
    ///Heure Actuel
    private static String getCurrentTime() {
        LocalTime lt=LocalTime.now();
        return lt.toString();
    }
    
    ///Date Actuel 
    private static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new java.util.Date());
        return date;
    }

    ///Date et Heure Actuel
    private static Timestamp getCurrentTimeStamp() {
        java.util.Date date= new java.util.Date();
        long time = date. getTime();
        return new Timestamp(time);
    }
    
    ///Date et Heure + tempsmin
    private static Timestamp getTimeStamp(Timestamp ts,int min) {
        return new Timestamp(ts.getYear(), ts.getMonth(),ts.getDate() , ts.getHours(), ts.getMinutes()+min, ts.getSeconds(), 0);
    }
    
    /**
     *
     * @param instance
     * @param condition
     * @param nbligneresult
     * @param debutligne
     * @param c
     * @return Select avec pagination 
     * @throws Exception
     */
    public static Object[] select(Class instance,int nbligneresult,String condition,int debutligne,Connection c) throws Exception {
        if(c==null) {
            throw new Exception("Connection null");
        }
        Object[] o=new Object[0];
        try {
            String url=c.getMetaData().getURL();
            String requpagination;
            if(condition==null) {
                condition="";
            }
            ///Postgres
            if(url.startsWith("jdbc:postgresql")) {
                requpagination=" limit "+nbligneresult+" offset "+(debutligne-1);
                o= select(instance, condition+requpagination, c);
            }
            ///Oracle
            else if(url.startsWith("jdbc:oracle")) {
                o= selectWithPaginationOracleOnly(instance, condition,nbligneresult,debutligne, c);
            }
        }catch(Exception ex) {
            throw ex;
        }
        return o;
    }
    
    /**
     *
     * @param objet
     * @param afterwhere
     * @param nbrlignresult
     * @param debutligne
     * @param c
     * @return Select avec pagination 
     * @throws Exception
     */
    public static Object[] selectWithObject(Object objet,int nbrlignresult,String afterwhere,int debutligne,Connection c) throws Exception {
        if(c==null) {
            throw new Exception("Connection null");
        }
        Object[] o=new Object[0];
        try {
            String url=c.getMetaData().getURL();
            String requpagination;
            if(afterwhere==null) {
                afterwhere="";
            }
            ///Postgres
            if(url.startsWith("jdbc:postgresql")) {
                requpagination=" limit "+nbrlignresult+" offset "+(debutligne-1);
                o= selectWithObject(objet, afterwhere+requpagination, c);
            }
            ///Oracle
            else if(url.startsWith("jdbc:oracle")) {
                o= selectWithPaginationOracleOnly(objet, afterwhere,nbrlignresult,debutligne, c);
            }
        }catch(Exception ex) {
            throw ex;
        }
        return o;
    }

    /**
     *
     * @param instance
     * @param condition
     * @param debutligne
     * @param finligne
     * @param c
     * @return  Select avec pagination
     * @throws Exception
     */
    public static Object[] select(Class instance,String condition,int debutligne,int finligne,Connection c) throws Exception {
        if(c==null) {
            throw new Exception("Connection null");
        }
        Object[] o=new Object[0];
        try {
            String url=c.getMetaData().getURL();
            String requpagination;
            if(condition==null) {
                condition="";
            }
            ///Postgres
            if(url.startsWith("jdbc:postgresql")) {
                requpagination=" limit "+(finligne-debutligne+1)+" offset "+(debutligne-1);
                o= select(instance, condition+requpagination, c);
            }
            ///Oracle
            else if(url.startsWith("jdbc:oracle")) {
                o= selectWithPaginationOracleOnly(instance, condition,(finligne-debutligne+1),debutligne, c);
            }
        }catch(Exception ex) {
            throw ex;
        }
        return o;
    }

    /**
     *
     * @param objet
     * @param afterwhere
     * @param debutligne
     * @param finligne
     * @param c
     * @return Select avec pagination
     * @throws Exception
     */
    public static Object[] selectWithObject(Object objet,String afterwhere,int debutligne,int finligne,Connection c) throws Exception {
        if(c==null) {
            throw new Exception("Connection null");
        }
        Object[] o=new Object[0];
        try {
            String url=c.getMetaData().getURL();
            String requpagination;
            if(afterwhere==null) {
                afterwhere="";
            }
            ///Postgres
            if(url.startsWith("jdbc:postgresql")) {
                requpagination=" limit "+(finligne-debutligne+1)+" offset "+(debutligne-1);
                o= selectWithObject(objet, afterwhere+requpagination, c);
            }
            ///Oracle
            else if(url.startsWith("jdbc:oracle")) {
                o= selectWithPaginationOracleOnly(objet, afterwhere,(finligne-debutligne+1),debutligne, c);
            }
        }catch(Exception ex) {
            throw ex;
        }
        return o;
    }
    
    ///Connection base Oracle
    private static Connection getOracleConnection(String dbname,String username,String password,int port,String host) throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String DBurl = "jdbc:oracle:thin:@"+host+":"+port+"/"+dbname;
        Connection con = null;
        con = DriverManager.getConnection(DBurl, username, password);
        setNLS_DATE_FORMAT(con);
        return con;
    }

    ///Pour la connection Oracle, pour ne pas avoir de problème avec la manipulation des dates
    private static void setNLS_DATE_FORMAT(Connection c) throws Exception {
        String requete = "ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD'";
        PreparedStatement stat = c.prepareStatement(requete);
        stat.executeUpdate();
        c.commit();
        stat.close();
    }
    
    ///Connection base Postgresql
    private static Connection getPostgresqlConnection(String dbname,String username,String password,int port,String host) throws Exception {
        Class.forName("org.postgresql.Driver");
        String DBurl = "jdbc:postgresql://"+host+":"+port+"/"+dbname;
        Connection con = DriverManager.getConnection(DBurl, username, password);
        con.setAutoCommit(false);
        return con;
    }

    /**
     *
     * @param dbname
     * @param username
     * @param password
     * @param dbtype
     * @param port
     * @param host
     * @return Pour récupérer l'instance d'une connexion bdd
     * @throws Exception
     */
    public static Connection getConnection(String dbname,String username,String password,String dbtype,int port,String host) throws Exception {
        Connection c=null;
        try {
            if(dbtype.equalsIgnoreCase("oracle")||dbtype.equalsIgnoreCase("orcl")) {
                c= getOracleConnection(dbname,username,password,port,host);
            }else if(dbtype.equalsIgnoreCase("postgresql")||dbtype.equalsIgnoreCase("postgres")||dbtype.equalsIgnoreCase("pgsql")) {
                c= getPostgresqlConnection(dbname,username,password,port,host);
            }
        }catch(Exception e) {
            throw new Exception("Veuillez vérifier le type de base de données que vous avez entré !");
        }
        return c;
    }

    /**
     *
     * @param xmlUrl
     * @return Pour récupérer l'instance d'une connexion bdd 
     * @throws Exception
     */
    public static Connection getConnection(String xmlUrl) throws Exception {
        String dbname, username, password, dbtype, host,port;
        Connection c=null;
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        DocumentBuilder db; Document doc;
        db=dbf.newDocumentBuilder();
        try {
            doc=db.parse(xmlUrl);
            dbname=doc.getElementsByTagName("database").item(0).getFirstChild().getNodeValue();
            username=doc.getElementsByTagName("user").item(0).getFirstChild().getNodeValue();
            password=doc.getElementsByTagName("password").item(0).getFirstChild().getNodeValue();
            dbtype=doc.getElementsByTagName("sgbd").item(0).getFirstChild().getNodeValue();
            host=doc.getElementsByTagName("host").item(0).getFirstChild().getNodeValue();
            port=doc.getElementsByTagName("port").item(0).getFirstChild().getNodeValue();
        }catch(Exception ex) {
            throw ex;
        }
        try {
            if(dbtype.equalsIgnoreCase("oracle")||dbtype.equalsIgnoreCase("orcl")) {
                c= getOracleConnection(dbname,username,password,Integer.parseInt(port),host);
            }else if(dbtype.equalsIgnoreCase("postgresql")||dbtype.equalsIgnoreCase("postgres")||dbtype.equalsIgnoreCase("pgsql")) {
                c= getPostgresqlConnection(dbname,username,password,Integer.parseInt(port),host);
            }
        }catch(Exception e) {
            throw new Exception("Veuillez vérifier le type de base de données que vous avez entré !");
        }
        return c;
    }
    
    /*  Pour  enregistrer la modification d'un fichier xml :
        XMLSerializer ser = new XMLSerializer(new FileOutputStream(xmlUrl), new OutputFormat(doc));
        ser.serialize(doc); */
    
    ///Avec prise en charge d'annotation, héritage
    ///Ne Marche pas si l'object entrée ne respecte pas les normes d'annotation configurés
    /**
     *
     * @param o
     * @param enableCommit
     * @param c
     * @throws Exception
     */
    public static void insert(Object o, boolean enableCommit, Connection c) throws Exception {
        String requete, colonne; Attribut annot;
        PreparedStatement ps=null;
        Class instance=o.getClass();
        Method m; Object g;
        try {
            verifyTable( instance) ;
            String tableName=((Classe) instance.getAnnotation(Classe.class)).table();
            requete = "INSERT INTO " + tableName+"(";
            Class superClasse;
            List<Field> field=new ArrayList();
            superClasse = instance;
            boolean b=false; int nbcolonne=0;
            while (!superClasse.getName().equals("java.lang.Object")) {
                Field[] attribut=superClasse.getDeclaredFields();
                for(int i=0;i<attribut.length;i++) {
                    field.add(attribut[i]);
                    annot = (Attribut)attribut[i].getAnnotation(Attribut.class);
                    if(annot!=null) {
                        colonne = annot.colonne();
                        nbcolonne++;
                        if(!b) {
                            requete+=colonne;
                            b=true;
                        }else {
                            requete+=","+colonne;
                        }
                    }
                }
                superClasse = superClasse.getSuperclass();
            }
            if(b==false) {
                throw new Exception("Aucune Annotation d'Attributs Spécifiés !");
            }
            requete+=") VALUES (";
            for(int i=0;i<nbcolonne;i++) {
                if(i==0) {
                    requete+="?";
                }else {
                    requete+=",?";
                }
            }
            requete+=")";
            ps=c.prepareStatement(requete);
            nbcolonne=1;
            for(int i=0;i<field.size();i++) { 
                annot = (Attribut)field.get(i).getAnnotation(Attribut.class);
                if(annot!=null) {
                    try {
                        m = instance.getMethod("get" + toUpperCase(field.get(i).getName()), new Class[0]);
                    }catch(Exception e) {
                        ///Cas spécifique pour certains getteur de type boolean
                        m = instance.getMethod("is" + toUpperCase(field.get(i).getName()), new Class[0]);
                    }
                    g = m.invoke(o, new Object[0]);
                    setPreparedStatement( ps, field.get(i).getType().getName(), nbcolonne, g);
                    nbcolonne++;
                }
            }
            ps.executeUpdate();
            if(enableCommit) {
                c.commit();
            }
            refreshCache(tableName);
        }catch(Exception e) {
            c.rollback();
            throw  e;
        }finally {
            if(ps!=null) {
                ps.close();
            }
        }
    }
    
    ///Avec prise en charge d'annotation, héritage, insertion d'Attribut tableau 
    ///Ne Marche pas si l'object entrée ne respecte pas les normes d'annotation configurés
    ///Cette fonction insert toutes les attributs avec l'annotations tableau qu'il trouve, sans limites 
    /**
     *
     * @param o
     * @param enableCommit
     * @param insererTableau
     * @param c
     * @param profondeurInsertion
     * @throws Exception
     */
    public static void insert(Object o, boolean enableCommit, boolean insererTableau,int profondeurInsertion, Connection c) throws Exception {
        String requete, colonne; Attribut annot;
        PreparedStatement ps=null;
        Class instance=o.getClass();
        Method m; Object g;
        try {
            verifyTable( instance) ;
            String tableName=((Classe) instance.getAnnotation(Classe.class)).table();
            requete = "INSERT INTO " + tableName+"(";
            Class superClasse;
            List<Field> field=new ArrayList();
            superClasse = instance;
            boolean b=false; int nbcolonne=0;
            while (!superClasse.getName().equals("java.lang.Object")) {
                Field[] attribut=superClasse.getDeclaredFields();
                for(int i=0;i<attribut.length;i++) {
                    field.add(attribut[i]);
                    annot = (Attribut)attribut[i].getAnnotation(Attribut.class);
                    if(annot!=null) {
                        colonne = annot.colonne();
                        nbcolonne++;
                        if(!b) {
                            requete+=colonne;
                            b=true;
                        }else {
                            requete+=","+colonne;
                        }
                    }
                }
                superClasse = superClasse.getSuperclass();
            }
            if(b==false) {
                throw new Exception("Aucune Annotation d'Attributs Spécifiés !");
            }
            requete+=") VALUES (";
            for(int i=0;i<nbcolonne;i++) {
                if(i==0) {
                    requete+="?";
                }else {
                    requete+=",?";
                }
            }
            requete+=")";
            ps=c.prepareStatement(requete);
            nbcolonne=1;
            for(int i=0;i<field.size();i++) { 
                annot = (Attribut)field.get(i).getAnnotation(Attribut.class);
                if(annot!=null) {
                    try {
                        m = instance.getMethod("get" + toUpperCase(field.get(i).getName()), new Class[0]);
                    }catch(Exception e) {
                        ///Cas spécifique pour certains getteur de type boolean
                        m = instance.getMethod("is" + toUpperCase(field.get(i).getName()), new Class[0]);
                    }
                    g = m.invoke(o, new Object[0]);
                    setPreparedStatement( ps, field.get(i).getType().getName(), nbcolonne, g);
                    nbcolonne++;
                }else if(insererTableau) { 
                    if(field.get(i).getAnnotation(Tableau.class)!=null) {
                        ///Insertion de tableau
                        m = instance.getMethod("get" + toUpperCase(field.get(i).getName()), new Class[0]);
                        try {
                            Object[] objTemp=(Object[]) m.invoke(o, new Object[0]);
                            if(objTemp.length>0&&(profondeurInsertion)>0) {
                                for(int n=0;n<objTemp.length;n++) {
                                    if(profondeurInsertion==1) {
                                        insert(objTemp[n],false,c);
                                    }else {
                                        insert(objTemp[n],false,true,--profondeurInsertion, c);
                                    }
                                }
                            }
                        }catch(Exception ex) {
                            System.out.println(ex.toString());
                        }
                    }
                }
            }
            ps.executeUpdate();
            if(enableCommit) {
                c.commit();
            }
            refreshCache(tableName);
        }catch(Exception e) {
            c.rollback();
            throw  e;
        }finally {
            if(ps!=null) {
                ps.close();
            }
        }
    }
    
    ///Pour le 'set" des arguments dans le PreparedStatement 
    private static void setPreparedStatement(PreparedStatement ps,String nomtypefield,int nbcolonne,Object g) throws Exception {
        if(nomtypefield.equals("java.lang.Double")) {
            ps.setDouble(nbcolonne, (Double)g);
        }else if(nomtypefield.equals("boolean")) {
            ps.setBoolean(nbcolonne, (boolean)g);
        }else if(nomtypefield.equals("int")) {
            ps.setInt(nbcolonne, (int)g);
        }else if(nomtypefield.equals("java.lang.String")) {
            ps.setString(nbcolonne, g.toString());
        }else if(nomtypefield.equals("java.sql.Date")||nomtypefield.equals("java.util.Date")) {
            ps.setDate(nbcolonne, Date.valueOf(g.toString()));
        }else if(nomtypefield.equals("float")) {
            ps.setFloat(nbcolonne, (float)g);
        }else if (nomtypefield.equals("java.sql.Timestamp") ) {
           ps.setTimestamp(nbcolonne, Timestamp.valueOf(g.toString()));
        }else if (nomtypefield.equals("java.sql.Time") ) {
           ps.setTime(nbcolonne, Time.valueOf(g.toString()));
        }
    }

    /**
     *
     * @param instance
     * @param condition
     * @param c
     * @return Select avec prise en charge de l'Héritage ,Annotation . Ne Marche pas si l'instance entrée ne respecte pas les normes d'annotation configurés 
     * @throws Exception
     */
    public static Object[] select(Class instance,String condition,Connection c) throws Exception {
        Object o=null; Attribut annot;
        ResultSet rs=null; String colonne="";
        PreparedStatement ps=null;
        try {
            verifyTable(instance);  
            String tableName=getNomTable(instance);
            String sql="Select * from "+tableName;
            if(condition!=null){
                sql+=" "+condition;
            }
            ps=c.prepareStatement(sql);
            String req=ps.toString();
            o=getResultFromCache(tableName,req);
            if(o==null) {
                rs=executeStatementSelect( ps, condition, tableName, instance);
                List<Field> field=getAllField( instance, rs.getMetaData().getColumnCount(), tableName);
                List<Object> rep=new ArrayList();
                Object obj; Method m;
                while(rs.next()){
                    obj=Class.forName(instance.getName()).newInstance();
                    for(int i=0;i<field.size();i++) {
                        annot = (Attribut)field.get(i).getAnnotation(Attribut.class);
                        if(annot!=null) {
                            colonne = annot.colonne();
                            m = instance.getMethod("set" + toUpperCase(field.get(i).getName()), field.get(i).getType());
                            getAndSetResult( obj, rs, m, colonne, field.get(i).getType().getName()) ;
                        }
                    }
                    rep.add(obj);
                }
                o =java.lang.reflect.Array.newInstance(instance,rep.size());
                for(int j=0;j<rep.size();j++){
                    java.lang.reflect.Array.set(o,j,rep.get(j));
                }
                Cacheable cachee;
                cachee=(Cacheable)instance.getAnnotation(Cacheable.class);
                if(cachee!=null) {
                    int mindureecache=(cachee).dureeenminute();
                    addToCache(tableName,req,(Object[])o,mindureecache);
                }
            }
        }catch(Exception ex) {
            throw ex;
        }finally {
            if(rs!=null){
                rs.close();
            }
            if(ps!=null) {
                ps.close();
            }
        }
        return (Object[]) o;
    }

    /**
     *
     * @param objet
     * @param afterwhere
     * @param c
     * @return Select avec prise en charge de l'Héritage,Annotation . Ne Marche pas si l'objet entrée ne respecte pas les normes d'annotation configurés 
     * @throws Exception
     */
    public static Object[] selectWithObject(Object objet,String afterwhere,Connection c) throws Exception {
        Object o=null; Attribut annot;
        ResultSet rs=null; String colonne="";
        PreparedStatement ps=null;
        Class instance=objet.getClass();
        Object obj; Method m;
        try {
            verifyTable(instance);  
            String tableName=getNomTable(instance);
            String sql="Select * from "+tableName+" where 4=4 ";
            List<Field> field=getAllField( instance);
            List<Object> condition=new ArrayList<>();
            List<Integer> indfield=new ArrayList();
            for(int i=0;i<field.size();i++) {
                annot = (Attribut)field.get(i).getAnnotation(Attribut.class);
                if(annot!=null) {
                    colonne = annot.colonne();
                    try {
                        m = instance.getMethod("get" + toUpperCase(field.get(i).getName()), new Class[0]);
                    }catch(Exception exx) {
                        m = instance.getMethod("is" + toUpperCase(field.get(i).getName()), new Class[0]);
                    }
                    obj = m.invoke(objet, new Object[0]);
                    if(obj!=null) {
                        condition.add(obj);
                        indfield.add(i);
                        sql+=" and "+colonne+" = ? ";
                    }
                }
            }
            if(afterwhere!=null){
                sql+=" "+afterwhere;
            }
            ps=c.prepareStatement(sql);
            for(int i=0;i<condition.size();i++) {
                setPreparedStatement( ps, field.get(indfield.get(i)).getType().getName(),i+1,condition.get(i));
            }
            String req=ps.toString();
            o=getResultFromCache(tableName,req);
            if(o==null) {
                rs=executeStatementSelect(ps, "", tableName, instance);
                List<Object> rep=new ArrayList();
                while(rs.next()){
                    obj=Class.forName(instance.getName()).newInstance();
                    for(int i=0;i<field.size();i++) {
                        annot = (Attribut)field.get(i).getAnnotation(Attribut.class);
                        if(annot!=null) {
                            colonne = annot.colonne();
                            m = instance.getMethod("set" + toUpperCase(field.get(i).getName()), field.get(i).getType());
                            getAndSetResult( obj, rs, m, colonne, field.get(i).getType().getName()) ;
                        }
                    }
                    rep.add(obj);
                }
                o =java.lang.reflect.Array.newInstance(instance,rep.size());
                for(int j=0;j<rep.size();j++){
                    java.lang.reflect.Array.set(o,j,rep.get(j));
                }
                Cacheable cachee;
                cachee=(Cacheable)instance.getAnnotation(Cacheable.class);
                if(cachee!=null) {
                    int mindureecache=(cachee).dureeenminute();
                    addToCache(tableName,req,(Object[])o,mindureecache);
                }
            }
        }catch(Exception ex) {
            throw ex;
        }finally {
            if(rs!=null){
                rs.close();
            }
            if(ps!=null) {
                ps.close();
            }
        }
        return (Object[]) o;
    }
    
    ///Pour Verifier si l'Annotation de Table a été bien spécifié
    private static void verifyTable(Class instance) throws Exception {
        try {
            if(instance.getAnnotation(Classe.class)==null) {
                throw new Exception("Aucune Annotation de Table Spécifié !");
            }
        }catch(Exception e) {
            throw e;
        }
    }
    
    ///Pour récupérer le nom de la table Correspondant à la classe 
    private static String getNomTable(Class instance) {
        try {
            return ((Classe) instance.getAnnotation(Classe.class)).table();
        }catch(Exception e) {
            throw e;
        }
    }
    
    ///Pour Executer la requête dans le Statement
    private static ResultSet executeStatementSelect(PreparedStatement ps,String condition,String tableName,Class instance) throws Exception {
        try {
            return ps.executeQuery();
        }catch(Exception e) {
            if(condition==null) {
                throw new Exception("Le nom de table '"+tableName+"', spécifié dans la Classe "+instance.getName()+" n'existe pas !");
            }else {
                throw new Exception("Veuillez vérifier la condition entrée et/ou le nom de table '"+tableName+"', spécifié dans la Classe "+instance.getName());
            }
        }
    }
    
    ///Pour récuperer tous les Fields de la classe , y compris ceux de sa classe mère etc -Methode 1
    private static List<Field> getAllField(Class instance,int columncount,String tablename) throws Exception {
        Class superClasse;
        List<Field> field=new ArrayList();
        superClasse = instance;
        int nbannot=0;
        while (!superClasse.getName().equals("java.lang.Object")) {
             Field[] attribut=superClasse.getDeclaredFields();
             for(int i=0;i<attribut.length;i++) {
                 field.add(attribut[i]);
                 if(attribut[i].getAnnotation(Attribut.class)!=null) {
                     nbannot++;
                 }
             }
             superClasse = superClasse.getSuperclass();
        }
        if(nbannot==0) {
            throw new Exception("Aucune Annotation d'Attributs Spécifiés !");
        }else if(columncount!=nbannot) {
            throw new Exception("Le Nombre d'Annotation d'Attributs trouvé en partant de la Classe "+instance.getName()+" et le Nombre de Colonne dans la Table "+tablename+" ne correspondent pas !");
        }
        return field;
    }
    
    ///Pour récuperer tous les Fields de la classe , y compris ceux de sa classe mère etc -Methode 2
    private static Field[] getAllAttributs(Class classe){
        List<Field> fields = new ArrayList();
        Class mere = classe.getSuperclass();
        Field[] rep;
        if(mere!=null){
            rep=getAllAttributs(mere);
            fields.addAll(Arrays.asList(rep));
        }
        fields.addAll(Arrays.asList(classe.getDeclaredFields()));
        rep=new Field[fields.size()];
        return fields.toArray(rep);
    }
    
    ///Pour récuperer tous les Fields de la classe , y compris ceux de sa classe mère etc
    private static List<Field> getAllField(Class instance) throws Exception {
        Class superClasse;
        List<Field> field=new ArrayList();
        superClasse = instance;
        int nbannot=0;
        while (!superClasse.getName().equals("java.lang.Object")) {
             Field[] attribut=superClasse.getDeclaredFields();
             for(int i=0;i<attribut.length;i++) {
                 field.add(attribut[i]);
                 if(attribut[i].getAnnotation(Attribut.class)!=null) {
                     nbannot++;
                 }
             }
             superClasse = superClasse.getSuperclass();
        }
        if(nbannot==0) {
            throw new Exception("Aucune Annotation d'Attributs Spécifiés !");
        }
        return field;
    }
    
    ///Pour recuperer et Ajouter dans l'Objet obj le resultat obtenu 
    private static void getAndSetResult(Object obj,ResultSet rs,Method m,String colonne,String nomtypefield) throws Exception {
        if (nomtypefield.equals("java.lang.String") ) {
             m.invoke(obj, rs.getString(colonne));
        }else if (nomtypefield.equals("java.lang.Double") ) {
             m.invoke(obj, rs.getDouble(colonne));
        }else if (nomtypefield.equals("int") ) {
             m.invoke(obj, rs.getInt(colonne));
        }else if (nomtypefield.equals("java.sql.Date") || nomtypefield.equals("java.util.Date")) {
             m.invoke(obj, rs.getDate(colonne));
        }else if (nomtypefield.equals("boolean") ) {
             m.invoke(obj, rs.getBoolean(colonne));
        }else if (nomtypefield.equals("float") ) {
             m.invoke(obj, rs.getFloat(colonne));
        }else if (nomtypefield.equals("java.sql.Timestamp") ) {
             m.invoke(obj, rs.getTimestamp(colonne));
        }else if (nomtypefield.equals("java.sql.Time") ) {
            m.invoke(obj, rs.getTime(colonne));
        }
    }

    /**
     *
     * @param arg
     * @return ToUpperCase 
     */
    private static String toUpperCase(String arg) {
        char[] name = arg.toCharArray();
        name[0] = Character.toUpperCase(name[0]);
        arg = String.valueOf(name);
        return arg;
    }
    
    ///Select Pour la pagination oracle seulement
    private static Object[] selectWithPaginationOracleOnly(Class instance,String condition,int nbrlignresult,int debutligne, Connection c) throws Exception {
        Object o=null; Attribut annot;
        ResultSet rs=null; String colonne="";
        PreparedStatement ps=null;
        try {
            verifyTable(instance);  
            String tableName=getNomTable(instance);
            String sql="Select * from "+tableName;
            if(condition!=null){
                sql+=" "+condition;
            }
            ps=c.prepareStatement("SELECT * FROM  ( SELECT a.*, ROWNUM rnum  FROM ( "+sql+" ) a  WHERE ROWNUM < "+(debutligne+nbrlignresult)+" ) WHERE rnum >= "+debutligne);
            String req=ps.toString();
            o=getResultFromCache(tableName,req);
            if(o==null) {
                rs=executeStatementSelect( ps, condition, tableName, instance);
                List<Field> field=getAllField( instance, rs.getMetaData().getColumnCount(), tableName);
                List<Object> rep=new ArrayList();
                Object obj; Method m;
                while(rs.next()){
                    obj=Class.forName(instance.getName()).newInstance();
                    for(int i=0;i<field.size();i++) {
                        annot = (Attribut)field.get(i).getAnnotation(Attribut.class);
                        if(annot!=null) {
                            colonne = annot.colonne();
                            m = instance.getMethod("set" + toUpperCase(field.get(i).getName()), field.get(i).getType());
                            getAndSetResult( obj, rs, m, colonne, field.get(i).getType().getName()) ;
                        }
                    }
                    rep.add(obj);
                }
                o =java.lang.reflect.Array.newInstance(instance,rep.size());
                for(int j=0;j<rep.size();j++){
                    java.lang.reflect.Array.set(o,j,rep.get(j));
                }
                Cacheable cachee;
                cachee=(Cacheable)instance.getAnnotation(Cacheable.class);
                if(cachee!=null) {
                    int mindureecache=(cachee).dureeenminute();
                    addToCache(tableName,req,(Object[])o,mindureecache);
                }
            }
        }catch(Exception ex) {
            throw ex;
        }finally {
            if(rs!=null){
                rs.close();
            }
            if(ps!=null) {
                ps.close();
            }
        }
        return (Object[]) o;
    }
    
    ///Select Pour la pagination oracle seulement
    private static Object[] selectWithPaginationOracleOnly(Object objet,String afterwhere,int nbrlignresult,int debutligne,Connection c) throws Exception {
        Object o=null; Attribut annot;
        ResultSet rs=null; String colonne="";
        PreparedStatement ps=null;
        Class instance=objet.getClass();
        Object obj; Method m;
        try {
            verifyTable(instance);  
            String tableName=getNomTable(instance);
            String sql="Select * from "+tableName+" where 4=4 ";
            List<Field> field=getAllField( instance);
            List<Object> condition=new ArrayList<>();
            List<Integer> indfield=new ArrayList();
            for(int i=0;i<field.size();i++) {
                annot = (Attribut)field.get(i).getAnnotation(Attribut.class);
                if(annot!=null) {
                    colonne = annot.colonne();
                    try {
                        m = instance.getMethod("get" + toUpperCase(field.get(i).getName()), new Class[0]);
                    }catch(Exception exx) {
                        m = instance.getMethod("is" + toUpperCase(field.get(i).getName()), new Class[0]);
                    }
                    obj = m.invoke(objet, new Object[0]);
                    if(obj!=null) {
                        condition.add(obj);
                        indfield.add(i);
                        sql+=" and "+colonne+" = ? ";
                    }
                }
            }
            if(afterwhere!=null){
                sql+=" "+afterwhere;
            }
            ps=c.prepareStatement("SELECT * FROM  ( SELECT a.*, ROWNUM rnum  FROM ( "+sql+" ) a  WHERE ROWNUM < "+(debutligne+nbrlignresult)+" ) WHERE rnum >= "+debutligne);
            for(int i=0;i<condition.size();i++) {
                setPreparedStatement( ps, field.get(indfield.get(i)).getType().getName(),i+1,condition.get(i));
            }
            String req=ps.toString();
            o=getResultFromCache(tableName,req);
            if(o==null) {
                rs=executeStatementSelect( ps, "", tableName, instance);
                List<Object> rep=new ArrayList();
                while(rs.next()){
                    obj=Class.forName(instance.getName()).newInstance();
                    for(int i=0;i<field.size();i++) {
                        annot = (Attribut)field.get(i).getAnnotation(Attribut.class);
                        if(annot!=null) {
                            colonne = annot.colonne();
                            m = instance.getMethod("set" + toUpperCase(field.get(i).getName()), field.get(i).getType());
                            getAndSetResult( obj, rs, m, colonne, field.get(i).getType().getName()) ;
                        }
                    }
                    rep.add(obj);
                }
                o =java.lang.reflect.Array.newInstance(instance,rep.size());
                for(int j=0;j<rep.size();j++){
                    java.lang.reflect.Array.set(o,j,rep.get(j));
                }
                Cacheable cachee;
                cachee=(Cacheable)instance.getAnnotation(Cacheable.class);
                if(cachee!=null) {
                    int mindureecache=(cachee).dureeenminute();
                    addToCache(tableName,req,(Object[])o,mindureecache);
                }
            }
        }catch(Exception ex) {
            throw ex;
        }finally {
            if(rs!=null){
                rs.close();
            }
            if(ps!=null) {
                ps.close();
            }
        }
        return (Object[]) o;
    }
    
    ///Fonction pour effectuer un update avec comme argument un objet; 
    ///La Fonction n'updatera que les attributs de l'objets non null ; Seul les Attribut PK sont mis dans la condition update
    ///Nb: Bien Considérer les attributs de types int car les int sont tjrs initialisé 0 si null => un int ne peut etre null !
    /**
     *
     * @param obj
     * @param enablecommit
     * @param con
     * @throws Exception
     */
    public static void update(Object obj,boolean enablecommit,Connection con)throws Exception{
        PreparedStatement prs=null; Method m;
        Attribut annot; String colonne; Object objet; PrimaryKey pk;
        try{
            Class instance =obj.getClass();
            verifyTable(instance);  
            String tableName=getNomTable(instance);
            String sql="update "+tableName+" set ";
            List<Field> field=getAllField( instance);
            List<Object> condition=new ArrayList<>();
            List<Integer> indfield=new ArrayList();
            List<Object> where=new ArrayList<>();
            List<Integer> indfieldwhere=new ArrayList();
            String wherereq=" ";
            for(int i=0;i<field.size();i++) {
                annot = (Attribut)field.get(i).getAnnotation(Attribut.class);
                if(annot!=null) {
                    colonne = annot.colonne();
                    try {
                        m = instance.getMethod("get" + toUpperCase(field.get(i).getName()), new Class[0]);
                    }catch(Exception exx) {
                        m = instance.getMethod("is" + toUpperCase(field.get(i).getName()), new Class[0]);
                    }
                    objet = m.invoke(obj, new Object[0]);
                    if(objet!=null) {
                            pk= (PrimaryKey)field.get(i).getAnnotation(PrimaryKey.class);
                            if(pk!=null) {
                                if(where.isEmpty()){
                                    wherereq+=" where "+colonne +" = ? ";
                                }else {
                                     wherereq+=" and "+colonne +" = ? ";
                                }
                                where.add(objet);
                                indfieldwhere.add(i);
                            }else {
                                 if(condition.isEmpty()) {
                                    sql+=" "+colonne+" = ? ";
                                 }else {
                                     sql+=" ,"+colonne+" = ? ";
                                 }
                                 condition.add(objet);
                                 indfield.add(i);
                            }
                    }
                }
            }
            sql+=wherereq;
            prs=con.prepareStatement(sql);
            int mo=0;
            for(int i=0;i<condition.size();i++) {
                setPreparedStatement( prs, field.get(indfield.get(i)).getType().getName(),i+1,condition.get(i));
                mo=i+2;
            }
            for(int i=0;i<where.size();i++) {
                setPreparedStatement( prs, field.get(indfieldwhere.get(i)).getType().getName(),mo,where.get(i));
                mo++;
            }
            //System.out.println(prs.toString());
            prs.executeUpdate();
            if(enablecommit) {
                con.commit();
            }
            refreshCache(tableName);
        }
        catch(Exception ex){
            con.rollback();
            throw ex;
        }finally{
            if(prs!=null) {
                prs.close();
            }
        }
    }
    
    ///Fonction pour effectuer un update avec comme argument le nom de table, le noms des attributs à mettre à jour et les valeurs correspondantes
    /**
     *
     * @param nomtable
     * @param columns
     * @param values
     * @param condition
     * @param con
     * @throws Exception
     */
    public static void update(String nomtable,String[] columns,Object[] values,String condition,Connection con)throws Exception{
        PreparedStatement prs=null;
        try{
            String sql="update "+nomtable+" set ";
            Boolean first=true;
            for(String column : columns){
                if(!first){
                    sql+=",";
                }
                sql+=column+"=?";
                first=false;
            }
            if(!condition.equals("")){
                sql+=" where "+condition;
            }
            prs=con.prepareStatement(sql);
            for(int i=0;i<values.length;i++){
                setPreparedStatement(prs,values[i].getClass().getTypeName(),1+i,values[i]);
            }
            prs.executeUpdate();
            refreshCache(nomtable);
        }
        catch(Exception ex){
            throw ex;
        }finally{
            if(prs!=null) {
                prs.close();
            }
        }
    }
    
    ///Fonction pour effectuer un update avec comme argument le nom de table, la requete des colonnes à maj et la condition
    /**
     *
     * @param nomtable
     * @param toupdate
     * @param condition
     * @param con
     * @throws Exception
     */
    public static void update(String nomtable,String toupdate,String condition,Connection con)throws Exception{
        PreparedStatement prs=null;
        try{
            if(toupdate==null || toupdate.trim().equalsIgnoreCase("")) {
                throw new Exception("Requete à mettre à jour non trouvé !");
            }
            String sql="update "+nomtable+" set "+toupdate;
            if(!condition.equals("")){
                sql+=" where "+condition;
            }
            prs=con.prepareStatement(sql);
            prs.executeUpdate();
            refreshCache(nomtable);
        }
        catch(Exception ex){
            con.rollback();
            throw ex;
        }finally{
            if(prs!=null) {
                prs.close();
            }
        }
    }
    
    ///Fonction pour supprimer un element d'une table
    /**
     *
     * @param nomtable
     * @param condition
     * @param con
     * @throws Exception
     */
    public static void delete(String nomtable,String condition,Connection con)throws Exception{
        PreparedStatement prs=null;
        String sql;
        try{
            sql="delete from "+nomtable+" ";
            if(!(condition==null ||  condition.trim().equalsIgnoreCase(""))) {
                sql+=" where "+condition;
            }
            prs=con.prepareStatement(sql);
            prs.executeUpdate();
            refreshCache(nomtable);
        }
        catch(Exception ex){
            throw ex;
        }finally{
            if(prs!=null) {
                prs.close();
            }
        }
    }
    
    ///Fonction pour supprimer un element d'une table
    /**
     *
     * @param objet
     * @param con
     * @throws Exception
     */
    public static void delete(Object objet,Connection con)throws Exception{
        PreparedStatement prs=null;
        String sql; Attribut annot; String colonne;
        Class instance=objet.getClass();
        Object obj; Method m;
        try{
            verifyTable(instance);  
            String tableName=getNomTable(instance);
            sql="delete from "+tableName+" where 4=4 ";
            List<Field> field=getAllField( instance);
            List<Object> condition=new ArrayList<>();
            List<Integer> indfield=new ArrayList();
            for(int i=0;i<field.size();i++) {
                annot = (Attribut)field.get(i).getAnnotation(Attribut.class);
                if(annot!=null) {
                    colonne = annot.colonne();
                    try {
                        m = instance.getMethod("get" + toUpperCase(field.get(i).getName()), new Class[0]);
                    }catch(Exception exx) {
                        m = instance.getMethod("is" + toUpperCase(field.get(i).getName()), new Class[0]);
                    }
                    obj = m.invoke(objet, new Object[0]);
                    if(obj!=null) {
                        condition.add(obj);
                        indfield.add(i);
                        sql+=" and "+colonne+" = ? ";
                    }
                }
            }
            prs=con.prepareStatement(sql);
            for(int i=0;i<condition.size();i++) {
                setPreparedStatement( prs, field.get(indfield.get(i)).getType().getName(),i+1,condition.get(i));
            }
            prs.executeUpdate();
            refreshCache(tableName);
        }
        catch(Exception ex){
            con.rollback();
            throw ex;
        }finally{
            if(prs!=null) {
                prs.close();
            }
        }
    }
    
}
