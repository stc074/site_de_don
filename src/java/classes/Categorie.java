/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author pj
 */
public class Categorie extends Objet {

    private String categorie;
    private long idCategorie;
    private String sousCategorie;

    public Categorie() {
        super();
        this.categorie="";
        this.idCategorie=0;
    }
    public void getPosts(HttpServletRequest request) {
        this.categorie=request.getParameter("categorie");
        this.categorie=Objet.codeHTML(this.getCategorie());
    }

    public void verifPosts() {
        try {
            Objet.getConnection();
            if (this.getCategorie().length() == 0) {
                this.setErrorMsg("Champ CATÉGORIE vide.<br/>");
            } else if (this.getCategorie().length() > 40) {
                this.setErrorMsg("Champ CATÉGORIE trop long.<br/>");
            } else {
                String query = "SELECT COUNT(id) AS nb FROM table_categories WHERE categorie=?";
                PreparedStatement prepare = Objet.getConn().prepareStatement(query);
                prepare.setString(1, this.getCategorie());
                ResultSet result = prepare.executeQuery();
                result.next();
                int nb = result.getInt("nb");
                if (nb != 0) {
                    this.setErrorMsg("Désolé, cette CATÉGORIE existe déjà.<br/>");
                }
                result.close();
                prepare.close();
            }
            if(this.getErrorMsg().length()==0) {
                String query="INSERT INTO table_categories (categorie) VALUES (?)";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setString(1, this.getCategorie());
                prepare.executeUpdate();
                prepare.close();
                this.setTest(1);
            }
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Categorie.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTERNE : "+ex.getMessage()+".<br/>");
        } catch (SQLException ex) {
            Logger.getLogger(Categorie.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTERNE : "+ex.getMessage()+".<br/>");
        }
    }

    public void getGets(HttpServletRequest request) {
        this.idCategorie=Long.parseLong(request.getParameter("idCategorie"));
    }
    public void initCat() {
        if(this.idCategorie!=0) {
            try {
                Objet.getConnection();
                String query="SELECT categorie FROM table_categories WHERE id=? LIMIT 0,1";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setLong(1, this.idCategorie);
                ResultSet result=prepare.executeQuery();
                result.next();
                this.categorie=result.getString("categorie");
                result.close();
                prepare.close();
                Objet.closeConnection();
            } catch (NamingException ex) {
                Logger.getLogger(Categorie.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Categorie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void getPostsSousCategorie(HttpServletRequest request) {
        this.idCategorie=Long.parseLong(request.getParameter("idCategorie"));
        this.sousCategorie=request.getParameter("sousCategorie");
        this.sousCategorie=Objet.codeHTML(this.sousCategorie);
    }

    public void verifPostsSousCategorie() {
        try {
            Objet.getConnection();
            if(this.idCategorie==0)
                this.setErrorMsg("Choisissez une CATÉGORIE SVP.<br/>");
            if(this.sousCategorie.length()==0)
                this.setErrorMsg("Champ SOUS-CATÉGORIE vide.<br/>");
            else if(this.sousCategorie.length()>40)
                this.setErrorMsg("Champ SOUS-CATEGORIE trop long.<br/>");
            else {
                String query="SELECT COUNT(id) AS nb FROM table_sous_categories WHERE id_categorie=? AND sous_categorie=?";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setLong(1, this.idCategorie);
                prepare.setString(2, this.sousCategorie);
                ResultSet result=prepare.executeQuery();
                result.next();
                int nb=result.getInt("nb");
                if(nb!=0)
                    this.setErrorMsg("Désolé, cette SOUS-CATÉGORIE existe déjà.<br/>");
                result.close();
                prepare.close();
            }
            if(this.getErrorMsg().length()==0) {
                String query="INSERT INTO table_sous_categories (id_categorie,sous_categorie) VALUES (?,?)";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setLong(1, this.idCategorie);
                prepare.setString(2, this.sousCategorie);
                prepare.executeUpdate();
                prepare.close();
            }
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Categorie.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTERNE : "+ex.getMessage()+".<br/>");
        } catch (SQLException ex) {
            Logger.getLogger(Categorie.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTERNE : "+ex.getMessage()+".<br/>");
        }
    }

    @Override
    public void blank() {
        super.blank();
        this.categorie="";
        this.idCategorie=0;
    }

    /**
     * @return the categorie
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * @return the idCategorie
     */
    public long getIdCategorie() {
        return idCategorie;
    }

}
