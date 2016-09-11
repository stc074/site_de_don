<%@page import="java.io.File"%>
<%@page import="classes.Img"%>
<%@page import="java.util.Calendar"%>
<%@page import="classes.Annonce"%>
<%@include file="./includes.jsp" %>
<%
try {
    Annonce annonce=null;
    String tagTitle="Annonce de don";
    String tagDescription="Annonces de don sur toute la France - faites de la recupe de proximité !";
    if(request.getAttribute("annonce")!=null) {
        annonce=(Annonce)request.getAttribute("annonce");
        tagTitle="Don - Recupe -"+annonce.getTitre();
        tagDescription="Don et récupe de proximité - "+annonce.getTitre()+".";
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
<script src="./js-global/FancyZoom.js" type="text/javascript"></script>
<script src="./js-global/FancyZoomHTML.js" type="text/javascript"></script>
<script type="text/javascript" src="./scripts/scripts.js"></script>
<script type="text/javascript" src="https://apis.google.com/js/plusone.js">
  {lang: 'fr'}
</script>
<%@include file="./analytics.jsp" %>
</head>
    <body onload="setupZoom()">
        <%@include file="./connexion.jsp" %>
        <%@include file="./haut.jsp" %>
        <section>
           <%
           if(request.getAttribute("info")!=null) {
               int info=Integer.parseInt(request.getAttribute("info").toString());
               switch(info) {
                   case 1: %>
                   <br/>
                   <div class="info">Annonce inconnue !</div>
                   <br/>
                   <%
                   break;
                   }
               } else if(annonce!=null) {
                   Calendar cal=Calendar.getInstance();
                   cal.setTimeInMillis(annonce.getTimestamp());
                   %>
                   <div id="annonce">
                       <div class="cadre">
            <br/>
           <ul class="reseauxSoc2">
              <li>
                   <a href="https://twitter.com/share" class="twitter-share-button" data-lang="en">Tweet</a>
                   <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="//platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
               </li>
               <li>
                   <g:plusone></g:plusone>
               </li>
           </ul>
           <p></p>
                   <div><a href="./annonces-de-dons.html" title="REVENIR À LA LISTE">REVENIR A LA LISTE</a></div>
                   <br/>
                   <div><a href="#" title="SIGNALER UN ABUS" onclick="javascript:signalerAbus(<%= annonce.getId() %>);">SIGNALER UN ABUS</a></div>
                   <br/>
                       </div>
                   <br/>
                       <div class="cadrePub">
                           <script type="text/javascript"><!--
google_ad_client = "pub-0393835826855265";
/* 728x90, date de création 23/08/11 */
google_ad_slot = "7589010716";
google_ad_width = 728;
google_ad_height = 90;
//-->
</script>
<script type="text/javascript"
src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script>
                       </div>
                       <br/>
                   <div class="cadre">
                   <h1><%= annonce.getTitre() %></h1>
                       <div>Annonce publiée le, <%= cal.get(cal.DATE) %>-<%= cal.get(cal.MONTH)+1 %>-<%= cal.get(cal.YEAR) %> par <%= annonce.getPseudo() %>.</div>
                       <div><%= annonce.getCategorie() %>&rarr;<%= annonce.getSousCategorie() %>.</div>
                       <div>Localisation : <%= annonce.getCodePostal() %>-<%= annonce.getCommune() %>|<%= annonce.getRegion() %>-<%= annonce.getDepartement() %>.</div>
                   </div>
                   <br/>
                   <div class="cadre3">
                   <h2>Description</h2>
                   <article>
                   <%= annonce.getDescription() %>
                   </article>
                   </div>
                   <br/>
                       <div class="cadrePub">
                           <script type="text/javascript"><!--
google_ad_client = "pub-0393835826855265";
/* 728x90, date de création 23/08/11 */
google_ad_slot = "7589010716";
google_ad_width = 728;
google_ad_height = 90;
//-->
</script>
<script type="text/javascript"
src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script>
                       </div>
                       <br/>
                   <div class="cadre3">
                   <h2>Photos (Cliquez dessus pour voir en taille réelle)</h2>
                       <div class="photosMini">
                   <%
                   int nbPhotos=0;
                   Img img=new Img();
                   for(int i=1;i<=5;i++) {
                       String extension=annonce.getExtensions()[i-1];
                       if(extension.length()>0) {
                           String filename=Datas.DIR+"photos/"+annonce.getId()+"_"+i+extension;
                           String filenameMini2=Datas.DIR+"photos/mini2_"+annonce.getId()+"_"+i+extension;
                           File file=new File(filename);
                           File fileMini2=new File(filenameMini2);
                           if(file.exists()&&fileMini2.exists()) {
                               nbPhotos++;
                               img.getSize(fileMini2);
                               int largeur=img.getWidth();
                               int hauteur=img.getHeight();
                               %>
                               <div class="mini">
                                   <a href="./photo-<%= annonce.getId() %>-<%= i %><%= extension %>" title="<%= annonce.getTitre() %>" zoom="1">
                                       <img src="./photo-mini-2-<%= annonce.getId()%>-<%= i%><%= extension %>" width="<%= largeur%>" height="<%= hauteur%>" alt="photo"/>
                                   </a>
                               </div>
                                       <%
                                       }
                           }
                       }
                     %>
                     <%
                     if(nbPhotos==0) { %>
                     <br/>
                     <div class="info">Aucune photo enregistrée pour cette annonce.</div>
                     <br/>
                     <%
                     }
                   %>
                       </div>
                   </div>
                   <br/>
                    <div class="cadre">
                   <h2>Contacter <%= annonce.getPseudo() %></h2>
                       <div>Pour contacter cet annonceur(<%= annonce.getPseudo() %>) <a href="./contacter-annonceur-<%= annonce.getId() %>.html" title="CONTACTER <%= annonce.getPseudo() %>" rel="nofollow">CLIQUEZ ICI</a></div>
                   </div>
                   <br/>
                       <div class="cadrePub">
                           <script type="text/javascript"><!--
google_ad_client = "pub-0393835826855265";
/* 728x90, date de création 23/08/11 */
google_ad_slot = "7589010716";
google_ad_width = 728;
google_ad_height = 90;
//-->
</script>
<script type="text/javascript"
src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script>
                       </div>
                       <br/>
                       <br/>
                   </div>
                     <%
                   }
    %>
        </section>
       <%@include file="./footer.jsp" %>
    </body>
</html>
       <%
               } catch(Exception ex) { %>
               <div class="contenu">
    <div class="erreur">
        <div>Erreur :</div>
        <br/>
        <div><%= ex.getMessage() %></div>
    </div>
               </div>
    <% } %>
