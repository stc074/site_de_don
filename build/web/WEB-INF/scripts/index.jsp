<%@page import="classes.ListeAnnonce"%>
<%@page import="classes.Img"%>
<%@page import="java.io.File"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@include file="./includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Site de don et de récupération - Donnez vos vieux objets !</title>
<meta name="generator" content="NETBEANS 6.9"/>
<meta name="author" content=""/>
<meta name="keywords" content=""/>
<meta name="description" content="Don et récupération - Donnez ce dont vous ne vous servez plus - Récupérez des objets près de chez vous." />
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
            <header>
            </header>
           <%
          try {
               if(request.getAttribute("liste")!=null) {
                   ListeAnnonce liste=(ListeAnnonce)request.getAttribute("liste");
                   %>
           <article>
           <h1>Don</h1>
           <p>Donner est un geste altruiste, Plus écologique et surtout, plus sympathique ! Si vous voulez donner cliquez sur 'FAIRE UN DON' en haut sur le menu.</p>
           <p>Le saviez vous ? la générosité permettrait d'accroître son esperance de vie (Selon une etude scientifique récente).</p>
           <h1>Récupération</h1>
           <p>La récuperation est un vrai mode de vie, mais cela peut être a l'occasion, consultez nos annonces, vous trouverez surement votre bonheur près de chez vous.<br/>
              Pour de la récupération de proximité cliquez sur 'LES ANNONCES' sur le menu en haut.</p>
           <div class="cadre2">
               <div>Nombre d'annonces : <%= liste.getNbAnnonces() %>.</div>
               <div>Nombre d'inscrit(e)s : <%= liste.getNbMembres() %>.</div>
           </div>
           </article>
            <h2>Les dernières annonces</h2>
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
                    //out.println(liste.getErrorMsg());
                   int nbAnnonces=0;
                   for(int i=0;i<Datas.NBINDEX;i++) {
                       if(liste.getTitre()[i]!=null) {
                           if(i==Datas.NBINDEX/2) { %>
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
                            nbAnnonces++;
                       %>
                       <div class="listeAnnonce" onclick="javascipt:window.location.href='<%= liste.getUri()[i] %>#annonce';">
                           <div class="listeAnnonceGauche">
                               <%
                               if(liste.getExtension()[i].length()>0) {
                                   %>
                                   <img src="./photo-mini-1-<%= liste.getId()[i] %>-<%= liste.getIndex()[i] %><%= liste.getExtension()[i]%>" width="<%= liste.getLargeurs()[i] %>" height="<%= liste.getHauteurs()[i] %>" alt="miniature"/>
                                   <%
                                                                     } else { %>
                                                                     <img src="./GFXs/miniature.png" width="100" height="100" alt="miniature"/>
                                                                     <%
                                                                                                                                         }
                   %>
                           </div>
                           <div class="listeAnnonceDroite">
                               <h1>
                                   <a href="<%= liste.getUri()[i] %>#annonce" title="<%= liste.getTitre()[i] %>"><%= liste.getTitre()[i] %></a>
                               </h1>
                                   <p>
                                   <%= liste.getLigne1()[i] %>
                                   <br/>
                                   <%= liste.getLigne2()[i] %>
                                   <br/>
                                   <%= liste.getLigne3()[i] %>
                                   <br/>
                                   </p>
                           </div>
                       </div>
                           <br/>
                           <%
                                                     }
                                                     }
                   if(nbAnnonces==0) { %>
                   <br/>
                   <div class="cadre2">
                       <div class="info">Désolé, aucune annonce enregistrée.</div>
                   </div>
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
                                }
                %>
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
                <br/>
                <div class="lienAnnonces">
                    <a href="./annonces.html" title="TOUTES LES ANNONCES">TOUTES LES ANNONCES</a>
                </div>
                <p></p>
                 <%
               } catch(Exception ex) { %>
               <div class="erreur">
                   <div>Erreur :</div>
                   <br/>
                   <div><%= ex.getMessage() %></div>
               </div>
               <% } %>
       </section>
       <%@include file="./footer.jsp" %>
    </body>
</html>
