/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author pj
 */
public class Cgu extends Objet {
    private long id;
    private String texte;
    private long timestamp;
    public Cgu() {
        super();
    }

    public void init() {
        try {
            Objet.getConnection();
            String query="SELECT id,texte,timestamp FROM table_cgu LIMIT 0,1";
            Statement state=Objet.getConn().createStatement();
            ResultSet result=state.executeQuery(query);
            result.next();
            this.id=result.getLong("id");
            this.texte=result.getString("texte");
            this.timestamp=result.getLong("timestamp");
            result.close();
            state.close();
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Cgu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Cgu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getPosts(HttpServletRequest request) {
        this.texte=request.getParameter("texte");
        try {
            Objet.getConnection();
            String query="UPDATE table_cgu SET texte=?,timestamp=? WHERE id=?";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setString(1, this.texte);
            Calendar cal=Calendar.getInstance();
            long ts=cal.getTimeInMillis();
            prepare.setLong(2, ts);
            prepare.setLong(3, this.id);
            prepare.executeUpdate();
            prepare.close();
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Cgu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Cgu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * @return the texte
     */
    public String getTexte() {
        return texte;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

}
