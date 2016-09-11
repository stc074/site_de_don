/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author pj
 */
public class Annonce extends Objet {

    private long idCategorie;
    private long idSousCategorie;
    private String titre;
    private String description;
    private String captcha;
    private long id;
    private Membre membre;
    private long timestamp;
    private String uri;
    private long idMembre;
    private String[] extensions;
    private String pseudo;
    private String categorie;
    private String sousCategorie;
    private String region;
    private String departement;
    private String codePostal;
    private String commune;

    public Annonce(Membre membre) {
        super();
        this.membre=membre;
        this.idCategorie=0;
        this.idSousCategorie=0;
        this.titre="";
        this.description="";
    }

    public Annonce(long idAnnonce) {
        super();
        this.id=idAnnonce;
        this.extensions=new String[5];
    }

    public void getPostsDepot(HttpServletRequest request) {
        this.idCategorie=Long.parseLong(request.getParameter("idCategorie"));
        this.idSousCategorie=Long.parseLong(request.getParameter("idSousCategorie"));
        this.titre=request.getParameter("titre");
        this.titre=Objet.codeHTML(this.titre);
        this.description=request.getParameter("description");
        this.description=Objet.codeHTML2(this.description);
        this.captcha=request.getParameter("captcha").toLowerCase();
        this.captcha=Objet.codeHTML(this.captcha);
    }
    public void verifPostsDepot(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(true);
            if (this.idCategorie == 0) {
                this.setErrorMsg("Veuillez choisir la CATÉGORIE de votre annonce SVP.<br/>");
            }
            if (this.idSousCategorie == 0) {
                this.setErrorMsg("Veuillez choisir la SOUS-CATÉGORIE de votre annonce SVP.<br/>");
            }
            if (this.titre.length() == 0) {
                this.setErrorMsg("Champ COURTE DESCRIPTION vide.<br/>");
            } else if (this.titre.length() > 80) {
                this.setErrorMsg("Champ COURTE DESCRIPTION trop long.<br/>");
            }
            if (this.description.length() == 0) {
                this.setErrorMsg("Champ DESCRIPTION vide.<br/>");
            } else if (this.description.length() > 5000) {
                this.setErrorMsg("Champ DESCRIPTION trop long.<br/>");
            }
            if (this.captcha.length() == 0) {
                this.setErrorMsg("Champ CODE ANTI-ROBOT vide.<br/>");
            } else if (this.captcha.length() != 5) {
                this.setErrorMsg("Le champ CODE ANTI-ROBOT doit faire 5 caractères.<br/>");
            } else if (session.getAttribute("captcha") == null) {
                this.setErrorMsg("Session CODE ANTI-ROBOT dépassée.<br/>");
            } else if (!session.getAttribute("captcha").toString().equals(Objet.getEncoded(this.captcha))) {
                this.setErrorMsg("Mauvais CODE ANTI-ROBOT.<br/>");
            }
            if(this.getErrorMsg().length()==0) {
                try {
                    getMembre().initMembre();
                    Objet.getConnection();
                    String query="INSERT INTO table_annonces (id_membre,id_categorie,id_sous_categorie,titre,description,timestamp,last_timestamp) VALUES (?,?,?,?,?,?,?)";
                    PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                    Calendar cal=Calendar.getInstance();
                    long ts=cal.getTimeInMillis();
                    prepare.setLong(1, this.getMembre().getId());
                    prepare.setLong(2, this.idCategorie);
                    prepare.setLong(3, this.idSousCategorie);
                    prepare.setString(4, this.titre);
                    prepare.setString(5, this.description);
                    prepare.setLong(6, ts);
                    prepare.setLong(7, ts);
                    prepare.executeUpdate();
                    prepare.close();
                    query="SELECT LAST_INSERT_ID() AS idAnnonce FROM table_annonces WHERE id_membre=?";
                    prepare=Objet.getConn().prepareStatement(query);
                    prepare.setLong(1, this.getMembre().getId());
                    ResultSet result=prepare.executeQuery();
                    result.next();
                    this.id=result.getLong("idAnnonce");
                    result.close();
                    prepare.close();
                    session.setAttribute("idAnnonce", this.getId());
                    session.setAttribute("captcha", null);
                    query="UPDATE table_index SET etat=? WHERE id=?";
                    prepare=Objet.getConn().prepareStatement(query);
                    prepare.setInt(1, 1);
                    prepare.setInt(2, 1);
                    prepare.executeUpdate();
                    prepare.close();
                    this.setTest(1);
                    if(!(this.idCategorie==8||this.testMotsClesOut(this.titre)==true)) {
                        this.titre="Donne "+this.titre;
                    }
                    this.uri="annonce-"+this.id+"-"+Objet.encodeTitre(this.titre)+".html";
                    Mail mail=new Mail(this.getMembre().getEmail(), this.getMembre().getPseudo(), "Annonce publiée !");
                    mail.initMailAnnonce1(this.getMembre().getPseudo(), this.titre, this.getUri(), this.getId());
                    mail.send();
                    Objet.closeConnection();
                } catch (NamingException ex) {
                    Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
                    this.setErrorMsg("ERREUR INTERNE : "+ex.getMessage()+".<br/>");
                } catch (SQLException ex) {
                    Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
                    this.setErrorMsg("ERREUR INTERNE : "+ex.getMessage()+".<br/>");
                }
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTERNE : "+ex.getMessage()+".<br/>");
        }
    }

    public void initEdit(long idAnnonce) {
        this.id=idAnnonce;
        try {
            Objet.getConnection();
            String query="SELECT id_categorie,id_sous_categorie,titre,description,timestamp FROM table_annonces WHERE id=? AND id_membre=? LIMIT 0,1";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, this.id);
            prepare.setLong(2, this.getMembre().getId());
            ResultSet result=prepare.executeQuery();
            result.next();
            this.idCategorie=result.getLong("id_categorie");
            this.idSousCategorie=result.getLong("id_sous_categorie");
            this.titre=result.getString("titre");
            this.description=result.getString("description");
            this.timestamp=result.getLong("timestamp");
            result.close();
            prepare.close();
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
            this.id=0;
        } catch (SQLException ex) {
            Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
            this.id=0;
        }
    }

    public void verifPostsEdit(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(true);
            if (this.idCategorie == 0) {
                this.setErrorMsg("Veuillez choisir une CATÉGORIE SVP.<br/>");
            }
            if (this.idSousCategorie == 0) {
                this.setErrorMsg("Veuillez choisir une SOUS-CATÉGORIE SVP.<br/>");
            }
            if (this.titre.length() == 0) {
                this.setErrorMsg("Champ COURTE DESCRIPTION vide.<br/>");
            } else if (this.titre.length() > 80) {
                this.setErrorMsg("Champ COURTE DESCRIPTION trop long.<br/>");
            }
            if (this.description.length() == 0) {
                this.setErrorMsg("Champ DESCRIPTION vide.<br/>");
            } else if (this.description.length() > 5000) {
                this.setErrorMsg("Champ DESCRIPTION trop long.<br/>");
            }
            if (this.captcha.length() == 0) {
                this.setErrorMsg("Champ CODE ANTI-ROBOT vide.<br/>");
            } else if (this.captcha.length() != 5) {
                this.setErrorMsg("Mauvaise longueur du champ CODE ANTI-ROBOT.<br/>");
            } else if (session.getAttribute("captcha") == null) {
                this.setErrorMsg("Session CODE ANTI-ROBOT dépassée.<br/>");
            } else if (!session.getAttribute("captcha").toString().equals(Objet.getEncoded(this.captcha))) {
                this.setErrorMsg("Mauvais CODE-ANTI-ROBOT.<br/>");
            }
            if(this.getErrorMsg().length()==0) {
                try {
                    Objet.getConnection();
                    String query="UPDATE table_annonces SET id_categorie=?,id_sous_categorie=?,titre=?,description=?,timestamp=?,last_timestamp=? WHERE id=? AND id_membre=?";
                    PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                    prepare.setLong(1, this.idCategorie);
                    prepare.setLong(2, this.idSousCategorie);
                    prepare.setString(3, this.titre);
                    prepare.setString(4, this.description);
                    Calendar cal=Calendar.getInstance();
                    long ts=cal.getTimeInMillis();
                    prepare.setLong(5, ts);
                    prepare.setLong(6, ts);
                    prepare.setLong(7, this.id);
                    prepare.setLong(8, this.getMembre().getId());
                    prepare.executeUpdate();
                    prepare.close();
                    query="UPDATE table_index SET etat=? WHERE id=?";
                    prepare=Objet.getConn().prepareStatement(query);
                    prepare.setInt(1, 1);
                    prepare.setInt(2, 1);
                    prepare.executeUpdate();
                    prepare.close();
                    Objet.closeConnection();
                    if(!(this.idCategorie==8||this.testMotsClesOut(this.titre)==true)) {
                        this.titre="Donne "+this.titre;
                    }
                    this.uri="annonce-"+this.id+"-"+Objet.encodeTitre(this.titre)+".html";
                    this.getMembre().initMembre();
                    Mail mail=new Mail(this.getMembre().getEmail(), this.getMembre().getPseudo(), "Annonce modifiée !");
                    mail.initMailAnnonceModif1(this.getMembre().getPseudo(), this.titre, this.getUri(), this.id);
                    mail.send();
                    session.setAttribute("captcha", null);
                    session.setAttribute("idAnnonce", this.id);
                    this.setTest(1);
                } catch (NamingException ex) {
                    Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
                    this.setErrorMsg("ERREUR INTERNE : "+ex.getMessage());
                } catch (SQLException ex) {
                    Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
                    this.setErrorMsg("ERREUR INTERNE : "+ex.getMessage());
                }

            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTERNE : "+ex.getMessage());
        }
    }

    public void effaceAnnonce(long idAnnonce, long idMembre) {
        try {
            Objet.getConnection();
            this.id = idAnnonce;
            this.idMembre = idMembre;
            String query="SELECT extension1,extension2,extension3,extension4,extension5 FROM table_annonces WHERE id=? AND id_membre=? LIMIT 0,1";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, this.id);
            prepare.setLong(2, this.idMembre);
            ResultSet result=prepare.executeQuery();
            result.next();
            this.extensions=new String[5];
            extensions[0]=result.getString("extension1");
            extensions[1]=result.getString("extension2");
            extensions[2]=result.getString("extension3");
            extensions[3]=result.getString("extension4");
            extensions[4]=result.getString("extension5");
            result.close();
            prepare.close();
            for(int i=1;i<=5;i++) {
                String extension=extensions[i-1];
                if(extension.length()>0) {
                    String filename=Datas.DIR+"photos/"+this.id+"_"+i+extension;
                    String filenameMini1=Datas.DIR+"photos/mini1_"+this.id+"_"+i+extension;
                    String filenameMini2=Datas.DIR+"photos/mini2_"+this.id+"_"+i+extension;
                    File file=new File(filename);
                    File fileMini1=new File(filenameMini1);
                    File fileMini2=new File(filenameMini2);
                    if(file.exists()) {
                        file.delete();
                    }
                    if(fileMini1.exists()) {
                        fileMini1.delete();
                    }
                    if(fileMini2.exists()) {
                        fileMini2.delete();
                    }
                }
            }
            query="DELETE FROM table_messages WHERE id_annonce=? AND (id_membre_expediteur=? OR id_membre_destinataire=?)";
            prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, this.id);
            prepare.setLong(2, this.idMembre);
            prepare.setLong(3, this.idMembre);
            prepare.executeUpdate();
            prepare.close();
            query="DELETE FROM table_annonces WHERE id=? AND id_membre=?";
            prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, this.id);
            prepare.setLong(2, this.idMembre);
            prepare.executeUpdate();
            prepare.close();
            query="UPDATE table_index SET etat=? WHERE id=?";
            prepare=Objet.getConn().prepareStatement(query);
            prepare.setInt(1, 1);
            prepare.setInt(2, 1);
            prepare.executeUpdate();
            prepare.close();
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void effaceAnnonce2(long idAnnonce, long idMembre) throws SQLException {
            long idA = idAnnonce;
            long idM = idMembre;
            String query="SELECT extension1,extension2,extension3,extension4,extension5 FROM table_annonces WHERE id=? AND id_membre=? LIMIT 0,1";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, idA);
            prepare.setLong(2, idM);
            ResultSet result=prepare.executeQuery();
            result.next();
            this.extensions=new String[5];
            extensions[0]=result.getString("extension1");
            extensions[1]=result.getString("extension2");
            extensions[2]=result.getString("extension3");
            extensions[3]=result.getString("extension4");
            extensions[4]=result.getString("extension5");
            result.close();
            prepare.close();
            for(int i=1;i<=5;i++) {
                String extension=extensions[i-1];
                if(extension.length()>0) {
                    String filename=Datas.DIR+"photos/"+idA+"_"+i+extension;
                    String filenameMini1=Datas.DIR+"photos/mini1_"+idA+"_"+i+extension;
                    String filenameMini2=Datas.DIR+"photos/mini2_"+idA+"_"+i+extension;
                    File file=new File(filename);
                    File fileMini1=new File(filenameMini1);
                    File fileMini2=new File(filenameMini2);
                    if(file.exists()) {
                        file.delete();
                    }
                    if(fileMini1.exists()) {
                        fileMini1.delete();
                    }
                    if(fileMini2.exists()) {
                        fileMini2.delete();
                    }
                }
            }
            query="DELETE FROM table_messages WHERE id_annonce=? AND (id_membre_expediteur=? OR id_membre_destinataire=?)";
            prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, idA);
            prepare.setLong(2, idM);
            prepare.setLong(3, idM);
            prepare.executeUpdate();
            prepare.close();
            query="DELETE FROM table_annonces WHERE id=? AND id_membre=?";
            prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, idA);
            prepare.setLong(2, idM);
            prepare.executeUpdate();
            prepare.close();
            query="UPDATE table_index SET etat=? WHERE id=?";
            prepare=Objet.getConn().prepareStatement(query);
            prepare.setInt(1, 1);
            prepare.setInt(2, 1);
            prepare.executeUpdate();
            prepare.close();
    }

    public void initAnnonce() {
        try {
            Objet.getConnection();
            /*Calendar cal=Calendar.getInstance();
            long ts=cal.getTimeInMillis();
            String query="UPDATE table_annonces SET last_timestamp=? WHERE id=?";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, ts);
            prepare.setLong(2, this.id);
            prepare.executeUpdate();
            prepare.close();*/
            String query="SELECT t1.id_categorie,t1.titre,t1.description,t1.extension1,t1.extension2,t1.extension3,t1.extension4,t1.extension5,t1.timestamp,t1.etat,t2.pseudo,t3.categorie,t4.sous_categorie,t5.region,t6.departement,t7.code_postal,t7.commune FROM table_annonces AS t1,table_membres AS t2,table_categories AS t3,table_sous_categories AS t4,table_regions AS t5,table_departements AS t6,table_communes AS t7 WHERE t1.id=? AND t2.id=t1.id_membre AND t3.id=t1.id_categorie AND t4.id=t1.id_sous_categorie AND t5.id_region=t2.id_region AND t6.id_departement=t2.id_departement AND t7.id=t2.id_commune LIMIT 0,1";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, this.id);
            ResultSet result=prepare.executeQuery();
            result.next();
            this.idCategorie=result.getLong("id_categorie");
            this.titre=result.getString("titre");
            if(!(this.idCategorie==8||this.testMotsClesOut(this.titre)==true)) {
                this.titre="Donne "+this.titre;
            }
            this.description=result.getString("description");
            this.extensions[0]=result.getString("extension1");
            this.extensions[1]=result.getString("extension2");
            this.extensions[2]=result.getString("extension3");
            this.extensions[3]=result.getString("extension4");
            this.extensions[4]=result.getString("extension5");
            this.timestamp=result.getLong("timestamp");
            this.pseudo=result.getString("pseudo");
            this.categorie=result.getString("categorie");
            this.sousCategorie=result.getString("sous_categorie");
            this.region=result.getString("region");
            this.departement=result.getString("departement");
            this.codePostal=result.getString("code_postal");
            this.commune=result.getString("commune");
            this.uri="annonce-"+this.id+"-"+Objet.encodeTitre(this.titre)+".html";
            result.close();
            prepare.close();
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
            this.id=0;
        } catch (SQLException ex) {
            Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
            this.id=0;
        }
    }

    public void initAbus() throws NamingException, SQLException {
        String query="SELECT t1.titre,t1.description,t1.timestamp,t2.pseudo FROM table_annonces AS t1,table_membres AS t2 WHERE t1.id=? AND t2.id=t1.id_membre LIMIT 0,1";
        PreparedStatement prepare=Objet.getConn().prepareStatement(query);
        prepare.setLong(1, this.id);
        ResultSet result=prepare.executeQuery();
        result.next();
        this.titre=result.getString("titre");
        this.description=result.getString("description");
        this.timestamp=result.getLong("timestamp");
        this.pseudo=result.getString("pseudo");
    }

    public void testLocalisation() {
        this.getMembre().setIdRegion("0");
        try {
            Objet.getConnection();
            String query = "SELECT id_region FROM table_membres WHERE id=? LIMIT 0,1";
            PreparedStatement prepare = Objet.getConn().prepareStatement(query);
            prepare.setLong(1, this.getMembre().getId());
            ResultSet result=prepare.executeQuery();
            result.next();
            this.getMembre().setIdRegion(result.getString("id_region"));
            result.close();
            prepare.close();
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void effaceOlds() {
        try {
            Objet.getConnection();
            Calendar cal=Calendar.getInstance();
            long ts=cal.getTimeInMillis()-((long)(1000.0*60.0*60.0*24.0*30.0*6.0));
            String query="SELECT id,id_membre FROM table_annonces WHERE last_timestamp<?";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, ts);
            ResultSet result=prepare.executeQuery();
            while(result.next()) {
                long idAnnonce=result.getLong("id");
                long idMbr=result.getLong("id_membre");
                effaceAnnonce2(idAnnonce, idMbr);
             }
            result.close();
            prepare.close();
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Annonce.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public void blank() {
        super.blank();
        this.idCategorie=0;
        this.idSousCategorie=0;
        this.titre="";
        this.description="";
    }

    /**
     * @return the idCategorie
     */
    public long getIdCategorie() {
        return idCategorie;
    }

    /**
     * @return the idSousCategorie
     */
    public long getIdSousCategorie() {
        return idSousCategorie;
    }

    /**
     * @return the titre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @return the extensions
     */
    public String[] getExtensions() {
        return extensions;
    }

    /**
     * @return the pseudo
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * @return the categorie
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * @return the sousCategorie
     */
    public String getSousCategorie() {
        return sousCategorie;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @return the departement
     */
    public String getDepartement() {
        return departement;
    }

    /**
     * @return the codePostal
     */
    public String getCodePostal() {
        return codePostal;
    }

    /**
     * @return the commune
     */
    public String getCommune() {
        return commune;
    }

    /**
     * @return the membre
     */
    public Membre getMembre() {
        return membre;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }


}
