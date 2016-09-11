/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 *
 * @author pj
 */
public class ListeRechercheAnnonces {
    
    private int[] idCategories;
    private String[] categories;
    private String[] idRegions;
    private String[] regions;
    
    public void initListe() {
        try {
            Objet.getConnection();
            String query="SELECT COUNT(id) AS nbCategories FROM table_categories";
            Statement state=Objet.getConn().createStatement();
            ResultSet result=state.executeQuery(query);
            result.next();
            int nbCategories=result.getInt("nbCategories");
            result.close();
            state.close();
            this.idCategories=new int[nbCategories];
            this.categories=new String[nbCategories];
            query="SELECT id,categorie FROM table_categories ORDER BY categorie ASC";
            state=Objet.getConn().createStatement();
            result=state.executeQuery(query);
            int i=0;
            while(result.next()) {
                this.idCategories[i]=result.getInt("id");
                this.categories[i]=result.getString("categorie");
                i++;
            }
            result.close();
            state.close();
            query="SELECT COUNT(id) AS nbRegions FROM table_regions";
            state=Objet.getConn().createStatement();
            result=state.executeQuery(query);
            result.next();
            int nbRegions=result.getInt("nbRegions");
            result.close();
            state.close();
            this.idRegions=new String[nbRegions];
            this.regions=new String[nbRegions];
            query="SELECT id_region,region FROM table_regions ORDER BY region ASC";
            state=Objet.getConn().createStatement();
            result=state.executeQuery(query);
            i=0;
            while(result.next()) {
                this.idRegions[i]=result.getString("id_region");
                this.regions[i]=result.getString("region");
                i++;
            }
            result.close();
            state.close();
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(ListeRechercheAnnonces.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ListeRechercheAnnonces.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the idCategories
     */
    public int[] getIdCategories() {
        return idCategories;
    }

    /**
     * @return the categories
     */
    public String[] getCategories() {
        return categories;
    }

    /**
     * @return the idRegions
     */
    public String[] getIdRegions() {
        return idRegions;
    }

    /**
     * @return the regions
     */
    public String[] getRegions() {
        return regions;
    }
    
}
