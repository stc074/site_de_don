<%@page import="classes.ListeRechercheAnnonces"%>
<%@page import="java.io.File"%>
<%@page import="classes.Img"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="classes.Recherche"%>
<%@include file="./includes.jsp" %>
<%
try {
    Recherche recherche=null;
    String tagTitle="Récupérer";
    String tagDescription="Récupération sur toute la France - faites de la recupe de proximité !";
    if(request.getAttribute("recherche")!=null) {
        recherche=(Recherche)request.getAttribute("recherche");
        tagTitle=recherche.getTagTitle();
        tagDescription=recherche.getTagDescription();
        }
    %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title><%= tagTitle %></title>
<meta name="generator" content="NETBEANS 6.9"/>
<meta name="author" content=""/>
<meta name="keywords" content=""/>
<meta name="description" content="<%= tagDescription %>" />
<meta charset="UTF-8" />
<link rel="icon" type="image/png" href="./GFXs/favicon.png" />
<link href="./CSS/style.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="https://apis.google.com/js/plusone.js">
  {lang: 'fr'}
</script>
<%@include file="./analytics.jsp" %>
</head>
    <body>
<div id="fb-root"></div>
<script>
    (function(d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/fr_FR/all.js#xfbml=1";
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));
</script>
        <%@include file="./connexion.jsp" %>
        <%@include file="./haut.jsp" %>
        <section>
           <h1>Annonces - récupération</h1>
           <%
           if(recherche!=null&&request.getAttribute("liste")!=null) {
           ListeRechercheAnnonces liste=(ListeRechercheAnnonces)request.getAttribute("liste");
           Objet.getConnection();
    %>
           <div class="info">Pour affiner votre recherche, utilisez le formulaire ci-dessous :</div>
           <br/>
           <nav>
           <div id="form">
               <fieldset>
                   <legend>Ma recherche</legend>
                   <form action="./annonces-de-dons.html#form" method="POST">
                       <div>
                           <span>Je recherche : </span>
                           <input type="text" name="motsCles" value="<%= recherche.getMotsCles()%>" size="40" maxlength="300" />
                       </div>
                       <div>
                           <span>Catégorie :</span>
                           <select name="idCategorie" onchange="javascript:window.location.href='./annonces-1-'+this.value+'.html';">
                               <option value="0"<% if(recherche.getIdCategorie()==0) out.print(" selected=\"selected\""); %>>Toutes</option>
                               <%
                               for(int i=0; i<liste.getIdCategories().length; i++) { %>
                               <option value="<%= liste.getIdCategories()[i] %>"<% if(recherche.getIdCategorie()==liste.getIdCategories()[i]) out.print(" selected=\"selected\""); %>><%= liste.getCategories()[i] %></option>
                               <%
                                                             }
                                   %>
                           </select>
                           <span>Sous-catégorie :</span>
                           <select name="idSousCategorie" onchange="javascript:window.location.href='./annonces-2-'+this.value+'.html';">
                               <option value="0"<% if(recherche.getIdSousCategorie()==0) out.print(" selected=\"selected\""); %>>Toutes</option>
                               <%
                               if(recherche.getIdCategorie()!=0) {
                                   String query="SELECT id,sous_categorie FROM table_sous_categories WHERE id_categorie=? ORDER BY sous_categorie";
                                   PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                                   prepare.setLong(1, recherche.getIdCategorie());
                                   ResultSet result=prepare.executeQuery();
                                   while(result.next()) {
                                       long idSousCategorie=result.getLong("id");
                                       String sousCategorie=result.getString("sous_categorie");
                                       %>
                                       <option value="<%= idSousCategorie %>"<% if(recherche.getIdSousCategorie()==idSousCategorie) out.print(" selected=\"selected\""); %>><%= sousCategorie %></option>
                                       <%
                                       }
                                   result.close();
                                   prepare.close();
                                   }
                                   %>
                           </select>
                       </div>
                           <div>
                               <span>Localisation &rarr; </span>
                               <span>Région :</span>
                               <select name="idRegion" onchange="javascript:window.location.href='./annonces-3-'+this.value+'.html';">
                                   <option value="0"<% if(recherche.getIdRegion().equals("0")) out.print(" selected=\"selected\""); %>>Toutes</option>
                                   <%
                                   for(int i=0; i<liste.getIdRegions().length; i++) { %>
                                   <option value="<%= liste.getIdRegions()[i] %>"<% if(recherche.getIdRegion().equals(liste.getIdRegions()[i])) out.print(" selected=\"selected\""); %>><%= liste.getRegions()[i] %></option>
                                   <% } %>
                                </select>
                               <span>Département : </span>
                               <select name="idDepartement" onchange="javascript:window.location.href='./annonces-4-'+this.value+'.html';">
                                   <option value="0"<% if(recherche.getIdDepartement().equals("0")) out.print(" selected=\"selected\""); %>>Tous</option>
                                   <%
                                   if(!recherche.getIdRegion().equals("0")) {
                                       String query="SELECT id_departement,departement FROM table_departements WHERE id_region=? ORDER BY id_departement";
                                       PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                                       prepare.setString(1, recherche.getIdRegion());
                                       ResultSet result=prepare.executeQuery();
                                       while(result.next()) {
                                           String idDepartement=result.getString("id_departement");
                                           String departement=result.getString("departement");
                                           %>
                                           <option value="<%= idDepartement %>"<% if(recherche.getIdDepartement().equals(idDepartement)) out.print(" selected=\"selected\""); %>><%= idDepartement%>&rarr;<%= departement %></option>
                                           <%
                                           }
                                       result.close();
                                       prepare.close();
                                       }
                                       %>
                               </select>
                               <span>Commune : </span>
                               <select name="idCommune" onchange="javascript:window.location.href='./annonces-5-'+this.value+'.html';">
                                   <option value="0"<% if(recherche.getIdCommune()==0) out.print(" selected=\"selected\""); %>>Toutes</option>
                                   <%
                                   if((!recherche.getIdDepartement().equals("0"))&&(!recherche.getIdRegion().equals("0"))) {
                                       String query="SELECT id,commune,code_postal FROM table_communes WHERE id_region=? AND id_departement=? ORDER BY commune";
                                       PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                                       prepare.setString(1, recherche.getIdRegion());
                                       prepare.setString(2, recherche.getIdDepartement());
                                       ResultSet result=prepare.executeQuery();
                                       while(result.next()) {
                                           int idCommune=result.getInt("id");
                                           String codePostal=result.getString("code_postal");
                                           String commune=result.getString("commune");
                                           %>
                                           <option value="<%= idCommune %>"<% if(recherche.getIdCommune()==idCommune) out.print(" selected=\"selected\""); %>><%= codePostal %>-<%= commune %></option>
                                           <%
                                           }
                                       result.close();
                                       prepare.close();
                                       }
                                       %>
                               </select>
                           </div>
                               <br/>
                               <input type="submit" value="Rechercher" name="kermit" />
                               <input type="submit" value="Vider le formulaire" name="reset" />
                   </form>
               </fieldset>
           </div>
           </nav>
                           <%
                           String query="SELECT COUNT(t1.id) AS nbAnnonces FROM table_annonces AS t1,table_membres AS t2"+recherche.getCondition()+" AND t2.id=t1.id_membre";
                           Statement state=Objet.getConn().createStatement();
                           ResultSet result=state.executeQuery(query);
                           result.next();
                           int nbAnnonces=result.getInt("nbAnnonces");
                           if(nbAnnonces==0) { %>
                           <p></p>
           <ul class="reseauxSoc">
               <li>
                   <a href="https://twitter.com/share" class="twitter-share-button" data-lang="en">Tweet</a>
                   <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="//platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
               </li>
               <li>
                   <g:plusone></g:plusone>
               </li>
               <li>
                   <div class="fb-like" data-send="true" data-layout="button_count" data-width="450" data-show-faces="true"></div>
               </li>
           </ul>
           <p></p>
            <div class="listeAnnoncePub">
                  <script type="text/javascript"><!--
google_ad_client = "pub-0393835826855265";
/* 728x90, date de création 23/08/11 */
google_ad_slot = "2251233682";
google_ad_width = 728;
google_ad_height = 90;
//-->
</script>
<script type="text/javascript"
src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script>
            </div>
           <br/>
                           <%
                           } else { %>
                           <h2><%= nbAnnonces %> annonce(s) pour cette recherche</h2>
           <ul class="reseauxSoc">
               <li>
                   <a href="https://twitter.com/share" class="twitter-share-button" data-lang="en">Tweet</a>
                   <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="//platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
               </li>
               <li>
                   <g:plusone></g:plusone>
               </li>
               <li>
                   <div class="fb-like" data-send="true" data-layout="button_count" data-width="450" data-show-faces="true"></div>
               </li>
           </ul>
                           <p></p>
            <div class="listeAnnoncePub">
                  <script type="text/javascript"><!--
google_ad_client = "pub-0393835826855265";
/* 728x90, date de création 23/08/11 */
google_ad_slot = "2251233682";
google_ad_width = 728;
google_ad_height = 90;
//-->
</script>
<script type="text/javascript"
src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script>
            </div>
                           <p></p>
                          <%
                           int nbPages=(int)(Math.ceil((double)nbAnnonces/(double)Datas.NBANNONCESPAGE));
                           /*query="SELECT COUNT(t1.id) AS nbAnnoncesPage FROM table_annonces AS t1,table_membres AS t2"+recherche.getCondition()+" AND t1.id_membre=t2.id LIMIT "+(recherche.getPage()*Datas.NBANNONCESPAGE)+","+Datas.NBANNONCESPAGE;
                           out.println(query);
                           state=Objet.getConn().createStatement();
                           result=state.executeQuery(query);
                           result.next();*/
                           int nbAnnoncesPages=0;
                           if(nbAnnonces<Datas.NBANNONCESPAGE)
                               nbAnnoncesPages=nbAnnonces;
                           else if((recherche.getPage()+1)<nbPages)
                               nbAnnoncesPages=Datas.NBANNONCESPAGE;
                           else if((recherche.getPage()+1)==nbPages)
                               nbAnnoncesPages=nbAnnonces-((nbPages-1)*Datas.NBANNONCESPAGE);
                           if((recherche.getPage()+1)<nbPages)
                               nbAnnoncesPages=Datas.NBANNONCESPAGE;
                           else
                               nbAnnoncesPages=nbAnnonces-(Datas.NBANNONCESPAGE*recherche.getPage());
                           //int nbAnnoncesPages=result.getInt("nbAnnoncesPage");
                           //out.println(nbAnnoncesPages);
                           result.close();
                           state.close();
                           query="SELECT t1.id,t1.id_categorie,t1.titre,t1.extension1,t1.extension2,t1.extension3,t1.extension4,t1.extension5,t1.timestamp,t2.pseudo,t3.categorie,t4.sous_categorie,t5.region,t6.departement,t7.code_postal,t7.commune FROM table_annonces AS t1,table_membres AS t2,table_categories AS t3,table_sous_categories AS t4,table_regions AS t5,table_departements AS t6,table_communes AS t7"+recherche.getCondition()+" AND t2.id=t1.id_membre AND t3.id=t1.id_categorie AND t4.id=t1.id_sous_categorie AND t5.id_region=t2.id_region AND t6.id_departement=t2.id_departement AND t7.id=t2.id_commune ORDER BY t1.timestamp DESC LIMIT "+(recherche.getPage()*Datas.NBANNONCESPAGE)+","+nbAnnoncesPages;
                           //out.println(query);
                           state=Objet.getConn().createStatement();
                           result=state.executeQuery(query);
                           Calendar cal=Calendar.getInstance();
                           int j=0;
                           while(result.next()) {
     if(nbAnnoncesPages>5&&j==Math.floor(nbAnnoncesPages/2)) { %>
                                     <div class="listeAnnoncePub">
                   <script type="text/javascript"><!--
google_ad_client = "pub-0393835826855265";
/* 728x90, date de création 23/08/11 */
google_ad_slot = "2251233682";
google_ad_width = 728;
google_ad_height = 90;
//-->
</script>
<script type="text/javascript"
src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script>
               </div>
           <p></p>
           <%
                     }
                               j++;
                              long idAnnonce=result.getLong("id");
                               long idCat=result.getLong("id_categorie");
                               String titre=result.getString("titre");
                               if(!(idCat==8||recherche.testMotsClesOut(titre)==true))
                                   titre="Donne "+titre.toLowerCase();
                               String extensions[]=new String[5];
                               extensions[0]=result.getString("extension1");
                               extensions[1]=result.getString("extension2");
                               extensions[2]=result.getString("extension3");
                               extensions[3]=result.getString("extension4");
                               extensions[4]=result.getString("extension5");
                               long timestamp=result.getLong("timestamp");
                               String pseudo=result.getString("pseudo");
                               String categorie=result.getString("categorie");
                               String sousCategorie=result.getString("sous_categorie");
                               String region=result.getString("region");
                               String departement=result.getString("departement");
                               String codePostal=result.getString("code_postal");
                               String commune=result.getString("commune");
                               cal.setTimeInMillis(timestamp);
                               String uri="./annonce-"+idAnnonce+"-"+Objet.encodeTitre(titre)+".html";
           %>
                               <div class="listeAnnonce" onclick="javascript:window.location.href='<%= uri %>#annonce';">
                                   <div class="listeAnnonceGauche">
                                       <%
                                       int nbPhotos=0;
                                       Img img=new Img();
                                       for(int i=1;i<=5;i++) {
                                           String extension=extensions[i-1];
                                           if(extension.length()>0&&nbPhotos==0) {
                                               String filenameMini1=Datas.DIR+"photos/mini1_"+idAnnonce+"_"+i+extension;
                                               File fileMini1=new File(filenameMini1);
                                               if(fileMini1.exists()) {
                                                   nbPhotos++;
                                                   img.getSize(fileMini1);
                                                   int largeur=img.getWidth();
                                                   int hauteur=img.getHeight();
                                                   %>
                                                   <img src="./photo-mini-1-<%= idAnnonce%>-<%= i%><%= extension %>" width="<%= largeur%>" height="<%= hauteur%>" alt="mini"/>
                                                   <%
                                                   }
                                               }
                                           }
                                           if(nbPhotos==0) { %>
                                           <img src="./GFXs/miniature.png" width="100" height="100" alt="mini"/>
                                           <% } %>
                                   </div>
                                   <div class="listeAnnonceDroite">
                                       <h1>
                                           <a href="<%= uri %>#annonce" title="<%= titre.toUpperCase() %>"><%= titre %></a>
                                       </h1>
                                       <p>
                                       <% if(idCat==8) { %>
                                       Demande déposée par <%= pseudo %>, le <%= cal.get(cal.DATE) %>-<%= cal.get(cal.MONTH)+1 %>-<%= cal.get(cal.YEAR) %>.
                                       <% } else { %>
                                       Annonce déposée par <%= pseudo %>, le <%= cal.get(cal.DATE) %>-<%= cal.get(cal.MONTH)+1 %>-<%= cal.get(cal.YEAR) %>.
                                       <% } %>
                                       <br/>
                                       <%= categorie %>&rarr;<%= sousCategorie %>.
                                       <br/>
                                       <% if(idCat==8) { %>
                                       Localisation de la demande : <%= codePostal %>-<%= commune %> | <%= region %>-<%= departement %>.
                                       <% } else { %>
                                       Localisation : <%= codePostal %>-<%= commune %> | <%= region %>-<%= departement %>.
                                       <% } %>
                                       <br/>
                                   </div>
                                   <div class="clearBoth"></div>
                               </div>
                               <br/>
                               <%
                               }
                           result.close();
                           state.close();
                           int prem=recherche.getPage()-5;
                           if(prem<0)
                               prem=0;
                           int der=recherche.getPage()+5;
                           if(der>=nbPages)
                               der=nbPages-1;
                           %>
                           <div class="pages">
                               <span>Pages d'annonces :</span>
                               <%
                           for(int i=prem;i<=der;i++) {
                               if(i==recherche.getPage()) { %>
                               <span>[<span class="clign"><%= i+1 %></span>]</span>
                               <%
                               } else { %>
                               <span>[<a href="./annonces-6-<%= i %>.html#form" title="PAGE #<%= i+1 %>"><%= i+1 %></a>]</span>
                               <%
                               }
                               }
                               %>
                           </div>
           <br/>
              <p></p>
                                     <div class="listeAnnoncePub">
                   <script type="text/javascript"><!--
google_ad_client = "pub-0393835826855265";
/* 728x90, date de création 23/08/11 */
google_ad_slot = "2251233682";
google_ad_width = 728;
google_ad_height = 90;
//-->
</script>
<script type="text/javascript"
src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script>
               </div>
           <p></p>
           <%
                                }
                           Objet.closeConnection();
                           }
    %>
        </section>
       <%@include file="./footer.jsp" %>
    </body>
</html>
       <%
               } catch(Exception ex) { %>
               <section>
    <div class="erreur">
        <div>Erreur :</div>
        <br/>
        <div><%= ex.getMessage() %></div>
    </div>
               </section>>
    <% } %>
