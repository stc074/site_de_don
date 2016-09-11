/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 *
 * @author pj
 */
public class Abus extends Objet {
    private long idAnnonce;
    private long id;
    private long timestamp;
    private Annonce annonce;
    private long idMembre;

    public Abus() {
        super();
    }
    public Abus(long idAbus) {
        super();
        this.id=idAbus;
        try {
            Objet.getConnection();
            String query="SELECT id_annonce,timestamp FROM table_abus WHERE id=? LIMIT 0,1";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, this.id);
            ResultSet result=prepare.executeQuery();
            result.next();
            this.idAnnonce=result.getLong("id_annonce");
            this.timestamp=result.getLong("timestamp");
            result.close();
            prepare.close();
            this.annonce=new Annonce(this.idAnnonce);
            annonce.initAbus();
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Abus.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTENE : "+ex.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(Abus.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTENE : "+ex.getMessage());
        }
    }
    public void signaleAbus(long idAnnonce) {
        this.idAnnonce=idAnnonce;
        try {
            Objet.getConnection();
            String query="SELECT COUNT(id) AS nb FROM table_abus WHERE id_annonce=?";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, this.getIdAnnonce());
            ResultSet result=prepare.executeQuery();
            result.next();
            int nb=result.getInt("nb");
            result.close();
            prepare.close();
            if(nb==0) {
                query="INSERT INTO table_abus (id_annonce,timestamp) VALUES (?,?)";
                prepare=Objet.getConn().prepareStatement(query);
                prepare.setLong(1, this.getIdAnnonce());
                Calendar cal=Calendar.getInstance();
                long ts=cal.getTimeInMillis();
                prepare.setLong(2, ts);
                prepare.executeUpdate();
                prepare.close();
                query="SELECT LAST_INSERT_ID() AS idAbus FROM table_abus WHERE id_annonce=?";
                prepare=Objet.getConn().prepareStatement(query);
                prepare.setLong(1, this.idAnnonce);
                result=prepare.executeQuery();
                result.next();
                this.id=result.getLong("idAbus");
                result.close();
                prepare.close();
                Mail mail=new Mail(Datas.EMAILADMIN, "ADMINISTRATION", "Un nouvel abus !");
                mail.initMailAbus(this.getId());
                mail.send();
            }
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Abus.class.getName()).log(Level.SEVERE, null, ex);
            this.idAnnonce=0;
        } catch (SQLException ex) {
            Logger.getLogger(Abus.class.getName()).log(Level.SEVERE, null, ex);
            this.idAnnonce=0;
        }
    }

    public void ignoreAbus(long idAbus) {
        this.id=idAbus;
        try {
            Objet.getConnection();
            String query="DELETE FROM table_abus WHERE id=?";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, this.id);
            prepare.executeUpdate();
            prepare.close();
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Abus.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Abus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void effaceAbus(long idAbus) {
        this.id=idAbus;
        try {
            Objet.getConnection();
            String query="SELECT t1.id_annonce,t2.id_membre FROM table_abus AS t1,table_annonces AS t2 WHERE t1.id=? AND t2.id=t1.id_annonce LIMIT 0,1";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, this.id);
            ResultSet result=prepare.executeQuery();
            result.next();
            this.idAnnonce=result.getLong("id_annonce");
            this.idMembre=result.getLong("id_membre");
            result.close();
            prepare.close();
            query="DELETE FROM table_abus WHERE id=?";
            prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, this.id);
            prepare.executeUpdate();
            prepare.close();
            Objet.closeConnection();
            this.annonce=new Annonce(this.idAnnonce);
            this.annonce.effaceAnnonce(this.idAnnonce, this.idMembre);
        } catch (NamingException ex) {
            Logger.getLogger(Abus.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Abus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the idAnnonce
     */
    public long getIdAnnonce() {
        return idAnnonce;
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
     * @return the annonce
     */
    public Annonce getAnnonce() {
        return annonce;
    }

}
