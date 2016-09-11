<%@page import="classes.Messagerie"%>
<%@include file="./includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Don et recupe - Site de dons - Messagerie</title>
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
           <h1>Messagerie</h1>
           <%
           try {
               if(request.getAttribute("info")!=null) {
                   int info=Integer.parseInt(request.getAttribute("info").toString());
                   switch(info) {
                       case 1: %>
                       <br/>
                       <div class="info">Vous devez être connecté pour pouvoir consulter votre messagerie.</div>
                       <br/>
                       <div><a href="./inscription.html" title="S'INSCRIRE" rel="nofollow">S'INSCRIRE</a></div>
                       <br/>
                       <%
                       break;
                       }
                   } else if(request.getAttribute("messagerie")!=null) {
                       Messagerie messagerie=(Messagerie)request.getAttribute("messagerie");
                       %>
                       <br/>
                       <div class="cadre2">
                           <div>
                               <a href="./messages-recus.html" title="MESSAGES REÇUS" rel="nofollow">Messages reçus</a>
                               <%
                               if(messagerie.getNbRecusNonLus()>0) { %>
                               <span>[<span class="clign"><%= messagerie.getNbRecusNonLus() %></span>]&nbsp;Non lu(s).</span>
                               <% } %>
                           </div>
                       </div>
                           <br/>
                       <div class="cadre2">
                           <div>
                               <a href="./messages-envoyes.html" title="MESSAGES ENVOYÉS" rel="nofollow">Messages envoyés</a>
                               <%
                               if(messagerie.getNbEnvoyesNonLus()>0) { %>
                               <span>[<span class="clign"><%= messagerie.getNbEnvoyesNonLus() %></span>]&nbsp;Non lu(s).</span>
                               <% } %>
                           </div>
                       </div>
                           <br/>
                           <%
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
