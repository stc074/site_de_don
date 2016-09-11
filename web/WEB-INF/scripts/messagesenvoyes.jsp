<%@page import="java.util.Calendar"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@include file="./includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Don et recupe - Site de dons - Messages envoyés</title>
<meta name="generator" content="NETBEANS 6.9"/>
<meta name="author" content=""/>
<meta name="keywords" content=""/>
<meta name="description" content="Site de don et recupe - Donnez ce dont vous ne vous servez plus - Récuperation de proximité." />
<meta name="ROBOTS" content="NOINDEX, NOFOLLOW"/>
<meta charset="UTF-8" />
<link rel="icon" type="image/png" href="./GFXs/favicon.png" />
<link href="./CSS/style.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="https://apis.google.com/js/plusone.js">
  {lang: 'fr'}
</script>
<%@include file="./analytics.jsp" %>
</head>
    <body>
        <%@include file="./connexion.jsp" %>
        <%@include file="./haut.jsp" %>
        <section>
           <br/>
<div>
<a name="fb_share" type="button_count" share_url="<%= Datas.URLROOT %>">Cliquez pour partager !!!</a><script src="http://static.ak.fbcdn.net/connect.php/js/FB.Share" type="text/javascript"></script>
</div>
           <br/>
           <g:plusone></g:plusone>
           <br/>
           <h1>Messages envoyés</h1>
           <%
           try {
               if(request.getAttribute("info")!=null) {
                   int info=Integer.parseInt(request.getAttribute("info").toString());
                   switch(info) {
                       case 1: %>
                       <div class="cadre2">
                       <br/>
                       <div class="info">Vous devez être connecté pour pouvoir consulter vos messages.</div>
                       <br/>
                       <div><a href="./inscription.html" title="S'INSCRIRE" rel="nofollow">S'INSCRIRE</a></div>
                       <br/>
                       </div>
                       <br/>
                       <%
                       break;
                       }
                   } else if(request.getAttribute("idMembre")!=null) {
                       long idMembre=Long.parseLong(request.getAttribute("idMembre").toString());
                       Objet.getConnection();
                       String query="SELECT t1.id,t1.titre,t1.timestamp,t1.etat,t2.titre AS titreAnnonce,t3.pseudo FROM table_messages AS t1,table_annonces AS t2,table_membres AS t3 WHERE t2.id=t1.id_annonce AND t3.id=t1.id_membre_destinataire AND t1.id_membre_expediteur=? ORDER BY t1.timestamp DESC";
                       PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                       prepare.setLong(1, idMembre);
                       ResultSet result=prepare.executeQuery();
                       int nbMessages=0;
                       while(result.next()) {
                           nbMessages++;
                           long idMessage=result.getLong("id");
                           String titre=result.getString("titre");
                           long timestamp=result.getLong("timestamp");
                           int etat=result.getInt("etat");
                           String titreAnnonce=result.getString("titreAnnonce");
                           String pseudo=result.getString("pseudo");
                           Calendar cal=Calendar.getInstance();
                           cal.setTimeInMillis(timestamp);
                           %>
                           <div class="cadre2">
                               <a href="./message-envoye-<%= idMessage %>.html" title="CONSULTER CE MESSAGE" rel="nofollow"><%= titre %></a>
                               <%
                               if(etat==0) { %>
                               <span>[<span class="clign">Non consulté</span>]</span>
                               <% } %>
                               <span>&nbsp;Envoyé le <%= cal.get(cal.DATE) %>-<%= cal.get(cal.MONTH)+1 %>-<%= cal.get(cal.YEAR) %> à <%= pseudo %>,</span>
                               <span>&nbsp;[Annonce :: <%= titreAnnonce %>]</span>
                               <span>&rArr;<a href="efface-message-2-<%= idMessage %>.html" title="ÉFFACER CE MESSAGE" rel="nofollow">Éffacer</a></span>
                           </div>
                           <br/>
                           <%
                           }
                       result.close();
                       prepare.close();
                       if(nbMessages==0) { %>
                       <br/>
                       <div class="info">Aucun message envoye dans votre boite.</div>
                       <br/>
                       <%
                       }
                       Objet.closeConnection();
                       }
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
