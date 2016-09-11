/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 *
 * @author pj
 */
public class ListeAnnonce extends Objet {
    private long[] id;
    private String[] titre;
    private String[] uri;
    private String[] extension;
    private int[] index;
    private int[] largeurs;
    private int[] hauteurs;
    private String[] ligne1;
    private String[] ligne2;
    private String[] ligne3;
    private int nbAnnonces;
    private int nbMembres;
    
    public ListeAnnonce() {
        this.id=new long[Datas.NBINDEX];
        this.titre=new String[Datas.NBINDEX];
        this.uri=new String[Datas.NBINDEX];
        this.extension=new String[Datas.NBINDEX];
        this.index=new int[Datas.NBINDEX];
        this.largeurs=new int[Datas.NBINDEX];
        this.hauteurs=new int[Datas.NBINDEX];
        this.ligne1=new String[Datas.NBINDEX];
        this.ligne2=new String[Datas.NBINDEX];
        this.ligne3=new String[Datas.NBINDEX];
        this.nbAnnonces=0;
        this.nbMembres=0;
    }
    
    public void initListe() {
        try {
            this.titre=new String[Datas.NBINDEX];
            Objet.getConnection();
            String query="SELECT COUNT(id) AS nbannonces FROM table_annonces";
            Statement state=Objet.getConn().createStatement();
            ResultSet result=state.executeQuery(query);
            result.next();
            this.nbAnnonces=result.getInt("nbAnnonces");
            result.close();
            state.close();
            query="SELECT COUNT(id) AS nbMembres FROM table_membres";
            state=Objet.getConn().createStatement();
            result=state.executeQuery(query);
            result.next();
            this.nbMembres=result.getInt("nbMembres");
            result.close();
            state.close();
            query="SELECT t1.id,t1.id_categorie,t1.titre,t1.extension1,t1.extension2,t1.extension3,t1.extension4,t1.extension5,t1.timestamp,t2.pseudo,t3.categorie,t4.sous_categorie,t5.region,t6.departement,t7.commune,t7.code_postal FROM table_annonces AS t1,table_membres AS t2,table_categories AS t3,table_sous_categories AS t4,table_regions AS t5,table_departements AS t6,table_communes AS t7 WHERE t1.etat='1' AND t2.id=t1.id_membre AND t3.id=t1.id_categorie AND t4.id=t1.id_sous_categorie AND t5.id_region=t2.id_region AND t6.id_departement=t2.id_departement AND t7.id=t2.id_commune ORDER BY t1.timestamp DESC LIMIT 0,"+Datas.NBINDEX;
            state=Objet.getConn().createStatement();
            result=state.executeQuery(query);
            int i=0;
            Calendar cal=Calendar.getInstance();
            while(result.next()) {
                this.id[i]=result.getLong("id");
                long idCat=result.getLong("id_categorie");
                String titreAnnonce=result.getString("titre");
                if(idCat==8||this.testMotsClesOut(titreAnnonce)==true)
                    this.titre[i]=titreAnnonce;
                else
                    this.titre[i]="Donne "+titreAnnonce.toLowerCase();
                this.uri[i]="./annonce-"+this.getId()[i]+"-"+Objet.encodeTitre(this.getTitre()[i])+".html";
                String extensions[]=new String[5];
                extensions[0]=result.getString("extension1");
                extensions[1]=result.getString("extension2");
                extensions[2]=result.getString("extension3");
                extensions[3]=result.getString("extension4");
                extensions[4]=result.getString("extension5");
                int nbPhotos=0;
                Img img=new Img();
                for(int j=1;j<=5;j++) {
                    String ext=extensions[j-1];
                    if(ext.length()>0&&nbPhotos==0) {
                        String filenameMini1=Datas.DIR+"photos/mini1_"+this.getId()[i]+"_"+j+ext;
                        File fileMini1=new File(filenameMini1);
                        if(fileMini1.exists()) {
                            this.extension[i]=ext;
                            this.index[i]=j;
                            nbPhotos++;
                            img.getSize(fileMini1);
                            this.largeurs[i]=img.getWidth();
                            this.hauteurs[i]=img.getHeight();
                        }
                    }
                }
                if(nbPhotos==0)
                    this.extension[i]="";
                long timestamp=result.getLong("timestamp");
                cal.setTimeInMillis(timestamp);
                String pseudo=result.getString("pseudo");
                if(idCat==8)
                    this.ligne1[i]="Demande déposée le "+cal.get(Calendar.DATE)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.YEAR)+", par "+pseudo+".";
                else
                    this.ligne1[i]="Annonce déposée le "+cal.get(Calendar.DATE)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.YEAR)+", par "+pseudo+".";
                String categorie=result.getString("categorie");
                String sousCategorie=result.getString("sous_categorie");
                this.ligne2[i]=categorie+"&rarr;"+sousCategorie+".";
                String region=result.getString("region");
                String departement=result.getString("departement");
                String commune=result.getString("commune");
                String codePostal=result.getString("code_postal");
                if(idCat==8)
                    this.ligne3[i]="Localisation de la demande : "+codePostal+"-"+commune+"|"+region+"-"+departement+".";
                else
                    this.ligne3[i]="Localisation : "+codePostal+"-"+commune+"|"+region+"-"+departement+".";
                i++;
            }
            result.close();
            state.close();
            query="UPDATE table_index SET etat=? WHERE id=?";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setInt(1, 0);
            prepare.setInt(2, 1);
            prepare.executeUpdate();
            prepare.close();
            Objet.closeConnection();
        } catch(Exception ex) {
            Logger.getLogger(ListeAnnonce.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg(ex.getMessage());
        }
    }

    public boolean testNelleAnnonce() {
        boolean flag=false;
        try {
            Objet.getConnection();
            String query="SELECT etat FROM table_index WHERE id=?";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setInt(1, 1);
            ResultSet result=prepare.executeQuery();
            result.next();
            int etat=result.getInt("etat");
            if(etat==1)
                flag=true;
            result.close();
            prepare.close();
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(ListeAnnonce.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ListeAnnonce.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }
    /**
     * @return the id
     */
    public long[] getId() {
        return id;
    }

    /**
     * @return the titre
     */
    public String[] getTitre() {
        return titre;
    }

    /**
     * @return the uri
     */
    public String[] getUri() {
        return uri;
    }

    /**
     * @return the extension
     */
    public String[] getExtension() {
        return extension;
    }

    /**
     * @return the largeurs
     */
    public int[] getLargeurs() {
        return largeurs;
    }

    /**
     * @return the hauteurs
     */
    public int[] getHauteurs() {
        return hauteurs;
    }

    /**
     * @return the ligne1
     */
    public String[] getLigne1() {
        return ligne1;
    }

    /**
     * @return the ligne2
     */
    public String[] getLigne2() {
        return ligne2;
    }

    /**
     * @return the ligne3
     */
    public String[] getLigne3() {
        return ligne3;
    }

    /**
     * @return the index
     */
    public int[] getIndex() {
        return index;
    }

    /**
     * @return the nbAnnonces
     */
    public int getNbAnnonces() {
        return nbAnnonces;
    }

    /**
     * @return the nbMembres
     */
    public int getNbMembres() {
        return nbMembres;
    }
}
