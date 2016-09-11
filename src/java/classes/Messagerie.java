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
public class Messagerie extends Objet {

    private int nbRecusNonLus;
    private int nbEnvoyesNonLus;
    private long idMembreDestinataire;
    private long idMembreExpediteur;

    public Messagerie() {
        super();
        this.nbRecusNonLus=0;
        this.nbEnvoyesNonLus=0;
    }

    public void calculNbRecusNonLus(long idMembre) {
        this.idMembreDestinataire=idMembre;
        try {
            Objet.getConnection();
            String query="SELECT COUNT(id) AS nbMessages FROM table_messages WHERE id_membre_destinataire=? AND etat='0'";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, this.idMembreDestinataire);
            ResultSet result=prepare.executeQuery();
            result.next();
            this.nbRecusNonLus=result.getInt("nbMessages");
            result.close();
            prepare.close();
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Messagerie.class.getName()).log(Level.SEVERE, null, ex);
            this.nbRecusNonLus=0;
        } catch (SQLException ex) {
            Logger.getLogger(Messagerie.class.getName()).log(Level.SEVERE, null, ex);
            this.nbRecusNonLus=0;
        }
    }
    public void calculNbEnvoyesNonLus(long idMembre) {
        this.idMembreExpediteur=idMembre;
        try {
            Objet.getConnection();
            String query="SELECT COUNT(id) AS nbMessages FROM table_messages WHERE id_membre_expediteur=? AND etat='0'";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, this.idMembreExpediteur);
            ResultSet result=prepare.executeQuery();
            result.next();
            this.nbEnvoyesNonLus=result.getInt("nbMessages");
            result.close();
            prepare.close();
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Messagerie.class.getName()).log(Level.SEVERE, null, ex);
            this.nbRecusNonLus=0;
        } catch (SQLException ex) {
            Logger.getLogger(Messagerie.class.getName()).log(Level.SEVERE, null, ex);
            this.nbRecusNonLus=0;
        }
    }
    public void effaceMessageRecu(long idMessage, long idMembre) {
        try {
            Objet.getConnection();
            String query="UPDATE table_messages SET id_prec='0' WHERE id_prec=? AND id_membre_expediteur=?";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, idMessage);
            prepare.setLong(2, idMembre);
            prepare.executeUpdate();
            prepare.close();
            query="DELETE FROM table_messages WHERE id=? AND id_membre_destinataire=?";
            prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, idMessage);
            prepare.setLong(2, idMembre);
            prepare.executeUpdate();
            prepare.close();
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Messagerie.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Messagerie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void effaceMessageEnvoye(long idMessage, long idMembre) {
        try {
            Objet.getConnection();
            String query="UPDATE table_messages SET id_prec='0' WHERE id_prec=? AND id_membre_destinataire=?";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, idMessage);
            prepare.setLong(2, idMembre);
            prepare.executeUpdate();
            prepare.close();
            query="DELETE FROM table_messages WHERE id=? AND id_membre_expediteur=?";
            prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, idMessage);
            prepare.setLong(2, idMembre);
            prepare.executeUpdate();
            prepare.close();
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Messagerie.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Messagerie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void effaceOlds() {
        try {
            Objet.getConnection();
            Calendar cal=Calendar.getInstance();
            long ts=cal.getTimeInMillis()-(long)(1000.0*60.0*60.0*24.0*30.0*6.0);
            String query="SELECT id FROM table_messages WHERE timestamp<?";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, ts);
            ResultSet result=prepare.executeQuery();
            while(result.next()) {
                long idMessage=result.getLong("id");
                String query2="UPDATE table_messages SET id_prec='0' WHERE id_prec=?";
                PreparedStatement prepare2=Objet.getConn().prepareStatement(query2);
                prepare2.setLong(1, idMessage);
                prepare2.executeUpdate();
                prepare2.close();
                query2="DELETE FROM table_messages WHERE id=?";
                prepare2=Objet.getConn().prepareStatement(query2);
                prepare2.setLong(1, idMessage);
                prepare2.executeUpdate();
                prepare2.close();
            }
            result.close();
            prepare.close();
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Messagerie.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Messagerie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the nbRecusNonLus
     */
    public int getNbRecusNonLus() {
        return nbRecusNonLus;
    }

    /**
     * @return the nbEnvoyesNonLus
     */
    public int getNbEnvoyesNonLus() {
        return nbEnvoyesNonLus;
    }

}
