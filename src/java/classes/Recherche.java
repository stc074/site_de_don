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
import javax.servlet.http.HttpSession;

/**
 *
 * @author pj
 */
public class Recherche extends Objet {
    private String tagTitle;
    private String tagDescription;
    private String motsCles;
    private long idCategorie;
    private long idSousCategorie;
    private String idRegion;
    private String idDepartement;
    private int idCommune;
    private String condition;
    private int page;

    public Recherche() {
        super();
        this.motsCles="";
        this.idCategorie=0;
        this.idSousCategorie=0;
        this.idRegion="0";
        this.idDepartement="0";
        this.idCommune=0;
        this.page=0;
        this.tagTitle = "Recupe - Annonce de dons";
        this.tagDescription = "Annonces de dons et recupe de proximité.";
    }

    public void reset(HttpServletRequest request) {
        HttpSession session=request.getSession(true);
        session.setAttribute("motsCles", null);
        session.setAttribute("idCategorie", null);
        session.setAttribute("idSousCategorie", null);
        session.setAttribute("idRegion", null);
        session.setAttribute("idDepartement", null);
        session.setAttribute("idCommune", null);
        session.setAttribute("page", null);
    }

    public void initFields(HttpServletRequest request) {
        HttpSession session=request.getSession(true);
        this.motsCles="";
        if(session.getAttribute("motsCles")!=null)
            this.motsCles=Objet.codeHTML(session.getAttribute("motsCles").toString());
        this.idCategorie=0;
        if(session.getAttribute("idCategorie")!=null)
            this.idCategorie=Long.parseLong(session.getAttribute("idCategorie").toString());
        this.idSousCategorie=0;
        if(session.getAttribute("idSousCategorie")!=null)
            this.idSousCategorie=Long.parseLong(session.getAttribute("idSousCategorie").toString());
        this.idRegion="0";
        if(session.getAttribute("idRegion")!=null)
            this.idRegion=Objet.codeHTML(session.getAttribute("idRegion").toString());
        this.idDepartement="0";
        if(session.getAttribute("idDepartement")!=null)
            this.idDepartement=Objet.codeHTML(session.getAttribute("idDepartement").toString());
        this.idCommune=0;
        if(session.getAttribute("idCommune")!=null)
            this.idCommune=Integer.parseInt(session.getAttribute("idCommune").toString());
        this.page=0;
        if(session.getAttribute("page")!=null)
            this.page=Integer.parseInt(session.getAttribute("page").toString());
    }
    public void getGets(HttpServletRequest request) {
        HttpSession session=request.getSession(true);
        if(request.getParameter("idCategorie")!=null) {
            this.idCategorie=Long.parseLong(request.getParameter("idCategorie"));
            if(this.idCategorie!=0)
                session.setAttribute("idCategorie", this.idCategorie);
            else
                session.setAttribute("idCategorie", null);
            this.idSousCategorie=0;
            session.setAttribute("idSousCategorie", null);
            this.page=0;
            session.setAttribute("page", null);
        }
        if(request.getParameter("idSousCategorie")!=null) {
            this.idSousCategorie=Long.parseLong(request.getParameter("idSousCategorie"));
            if(this.idSousCategorie!=0)
                session.setAttribute("idSousCategorie", this.idSousCategorie);
            else
                session.setAttribute("idSousCategorie", null);
            this.page=0;
            session.setAttribute("page", null);
        }
        if(request.getParameter("idRegion")!=null) {
            this.idRegion=request.getParameter("idRegion");
            this.idRegion=Objet.codeHTML(this.getIdRegion());
            if(!this.idRegion.equals("0"))
                session.setAttribute("idRegion", this.getIdRegion());
            else
                session.setAttribute("idRegion", null);
            this.idDepartement="0";
            session.setAttribute("idDepartement", null);
            this.idCommune=0;
            session.setAttribute("idCommune", null);
            this.page=0;
            session.setAttribute("page", null);
        }
        if(request.getParameter("idDepartement")!=null) {
            this.idDepartement=request.getParameter("idDepartement");
            this.idDepartement=Objet.codeHTML(this.getIdDepartement());
            if(!this.idDepartement.equals("0"))
                session.setAttribute("idDepartement", this.getIdDepartement());
            else
                session.setAttribute("idDepartement", null);
            this.idCommune=0;
            session.setAttribute("idCommune", null);
            this.page=0;
            session.setAttribute("page", null);
        }
        if(request.getParameter("idCommune")!=null) {
            this.idCommune=Integer.parseInt(request.getParameter("idCommune"));
            if(this.idCommune!=0)
                session.setAttribute("idCommune", this.getIdCommune());
            else
                session.setAttribute("idCommune", null);
            this.page=0;
            session.setAttribute("page", null);
        }
        if(request.getParameter("page")!=null) {
            this.page=Integer.parseInt(request.getParameter("page"));
            session.setAttribute("page", this.getPage());
        }
    }

    public void initTags(HttpServletRequest request) {
        try {
            HttpSession session=request.getSession(true);
            this.tagTitle = "Récupérer gratuitement des objets prés de chez vous";
            this.tagDescription = "Récupération de proximité - La récupe prés de chez vous !";
            Objet.getConnection();
            if (request.getParameter("idSousCategorie") != null && this.idSousCategorie!=0) {
                String query="SELECT t1.sous_categorie,t1.id_categorie,t2.categorie FROM table_sous_categories AS t1,table_categories AS t2 WHERE t1.id=? AND t2.id=t1.id_categorie LIMIT 0,1";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setLong(1, this.idSousCategorie);
                ResultSet result=prepare.executeQuery();
                result.next();
                String sousCategorie=result.getString("sous_categorie");
                this.idCategorie=result.getInt("id_categorie");
                session.setAttribute("idCategorie", this.idCategorie);
                String categorie=result.getString("categorie");
                result.close();
                prepare.close();
                this.tagTitle="Récupérer des objets - "+categorie+" - "+sousCategorie;
                this.tagDescription="Récupération GRATUITE de proximité - "+categorie+" - "+sousCategorie+".";
            } else if(request.getParameter("idCategorie")!=null && this.idCategorie!=0) {
                String query="SELECT categorie FROM table_categories WHERE id=? LIMIT 0,1";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setLong(1, this.idCategorie);
                ResultSet result=prepare.executeQuery();
                result.next();
                String categorie=result.getString("categorie");
                result.close();
                prepare.close();
                this.tagTitle="Récupérer des objets - "+categorie;
                this.tagDescription="Récupération GRATUITE de proximité - "+categorie+".";
            } else if(request.getParameter("idRegion")!=null&&!this.idRegion.equals("0")) {
                String query="SELECT region FROM table_regions WHERE id_region=? LIMIT 0,1";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setString(1, this.idRegion);
                ResultSet result=prepare.executeQuery();
                result.next();
                String region=result.getString("region");
                result.close();
                prepare.close();
                this.tagTitle="Récupérer des objets - Région "+region;
                this.tagDescription="Récupération GRATUITE de proximité en région "+region+".";
            } else if(request.getParameter("idDepartement")!=null&&!this.idDepartement.equals("0")) {
                String query="SELECT id_region,departement FROM table_departements WHERE id_departement=? LIMIT 0,1";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setString(1, this.idDepartement);
                ResultSet result=prepare.executeQuery();
                result.next();
                this.idRegion=result.getString("id_region");
                session.setAttribute("idRegion", this.idRegion);
                String departement=result.getString("departement");
                result.close();
                prepare.close();
                this.tagTitle="Récupérer des objets - Département - "+this.idDepartement+"-"+departement;
                this.tagDescription="Récupération GRATUITE de proximité - Département - "+idDepartement+"-"+departement+".";
            } else if(request.getParameter("idCommune")!=null&&this.idCommune!=0) {
                String query="SELECT id_region,id_departement,code_postal,commune FROM table_communes WHERE id=? LIMIT 0,1";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setInt(1, this.idCommune);
                ResultSet result=prepare.executeQuery();
                result.next();
                this.idRegion=result.getString("id_region");
                this.idDepartement=result.getString("id_departement");
                session.setAttribute("idRegion", this.idRegion);
                session.setAttribute("idDepartement", this.idDepartement);
                String codePostal=result.getString("code_postal");
                String commune=result.getString("commune");
                result.close();
                prepare.close();
                this.tagTitle="Récupérer des objets - "+codePostal+"&rarr;"+commune;
                this.tagDescription="Récupération GRATUITE de proximité - "+codePostal+"-"+commune+".";
            }
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Recherche.class.getName()).log(Level.SEVERE, null, ex);
            this.tagTitle = "Récupérer gratuitement des objets";
            this.tagDescription = "Récupération de proximité - La récupe prés de chez vous !";
       } catch (SQLException ex) {
            Logger.getLogger(Recherche.class.getName()).log(Level.SEVERE, null, ex);
            this.tagTitle = "Récupérer gratuitement des objets";
            this.tagDescription = "Récupération de proximité - La récupe prés de chez vous !";
        }
    }

    public void getPosts(HttpServletRequest request) {
        this.motsCles=request.getParameter("motsCles");
        this.motsCles=Objet.codeHTML(this.motsCles);
        this.idCategorie=Long.parseLong(request.getParameter("idCategorie"));
        this.idSousCategorie=Long.parseLong(request.getParameter("idSousCategorie"));
        this.idRegion=request.getParameter("idRegion");
        this.idRegion=Objet.codeHTML(this.idRegion);
        this.idDepartement=request.getParameter("idDepartement");
        this.idDepartement=Objet.codeHTML(this.idDepartement);
        this.idCommune=Integer.parseInt(request.getParameter("idCommune"));
    }

    public void verifPosts(HttpServletRequest request) {
        HttpSession session=request.getSession(true);
        if(this.motsCles.length()>0) {
            if(this.motsCles.length()>300) {
                this.motsCles="";
                session.setAttribute("motsCles", null);
            } else {
                session.setAttribute("motsCles", this.motsCles);
            }
        } else {
            session.setAttribute("motsCles", null);
        }
        if(this.idCategorie!=0)
            session.setAttribute("idCategorie", this.idCategorie);
        else
            session.setAttribute("idCategorie", null);
        if(this.idSousCategorie!=0)
            session.setAttribute("idSousCategorie", this.idSousCategorie);
        else
            session.setAttribute("idSousCategorie", null);
        if(!this.idRegion.equals("0"))
            session.setAttribute("idRegion", this.idRegion);
        else
            session.setAttribute("idRegion", null);
        if(!this.idDepartement.equals("0"))
            session.setAttribute("idDepartement", this.idDepartement);
        else
            session.setAttribute("idDepartement", null);
        if(this.idCommune!=0)
            session.setAttribute("idCommune", this.idCommune);
        else
            session.setAttribute("idCommune", null);
    }

    public void initCondition() {
        this.condition=" WHERE t1.etat='1'";
        if(this.motsCles.length()>0) {
            String motsCles2=this.motsCles;
            for(String article:Datas.arrayArticles) {
                motsCles2=motsCles2.replaceAll(article, " ");
            }
            String array[]=motsCles2.split(" ");
            for(String mot:array)
                this.condition+=" AND (t1.titre LIKE '%"+mot+"%' OR t1.description LIKE '%"+mot+"%')";
        }
        if(this.idCategorie!=0)
            this.condition+=" AND t1.id_categorie='"+this.idCategorie+"'";
        if(this.idSousCategorie!=0)
            this.condition+=" AND t1.id_sous_categorie='"+this.idSousCategorie+"'";
        if(!this.idRegion.equals("0"))
            this.condition+=" AND t2.id_region='"+this.idRegion+"'";
        if(!this.idDepartement.equals("0"))
            this.condition+=" AND t2.id_departement='"+this.idDepartement+"'";
        if(this.idCommune!=0)
            this.condition+=" AND t2.id_commune='"+this.idCommune+"'";
    }

    /**
     * @return the tagTitle
     */
    public String getTagTitle() {
        return tagTitle;
    }

    /**
     * @return the tagDescription
     */
    public String getTagDescription() {
        return tagDescription;
    }

    /**
     * @return the motsCles
     */
    public String getMotsCles() {
        return motsCles;
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
     * @return the idRegion
     */
    public String getIdRegion() {
        return idRegion;
    }

    /**
     * @return the idDepartement
     */
    public String getIdDepartement() {
        return idDepartement;
    }

    /**
     * @return the idCommune
     */
    public int getIdCommune() {
        return idCommune;
    }

    /**
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * @return the page
     */
    public int getPage() {
        return page;
    }

}
