<%@include file="./includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Don et recupe - Site de dons - Mon compte</title>
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
           <h1>Don et recupe - Mon compte</h1>
           <%
           try {
               if(request.getAttribute("info")!=null) {
                   int info=Integer.parseInt(request.getAttribute("info").toString());
                   switch(info) {
                       case 1: %>
                       <br/>
                       <div class="info">Vous devez être connecté pour pouvoir accéder à cette page.</div>
                       <br/>
                       <div class="cadre2">
                           <div><a href="./inscription.html" title="S'INSCRIRE" rel="nofollow">S'inscrire</a></div>
                       </div>
                       <br/>
                       <%
                       break;
                       case 2: %>
                       <div class="cadre2">
                           <div><a href="./infos-personnelles.html" title="INFOS PERSOS" rel="nofollow">Infos personnelles</a></div>
                       </div>
                       <br/>
                       <div class="cadre2">
                           <div><a href="./mes-annonces.html" title="MES ANNONCES" rel="nofollow">Mes annonces</a></div>
                       </div>
                       <br/>
                       <div class="cadre2">
                           <div><a href="./messagerie.html" title="MA MESSAGERIE" rel="nofollow">Ma messagerie</a></div>
                       </div>
                       <br/>
                       <div class="cadre2">
                           <div><a href="./supprimer-compte.html" title="SUPPRIMER MON COMPTE" rel="nofollow">Supprimer définitivement mon compte</a></div>
                       </div>
                       <br/>
                       <%
                       break;
                       }
                   }
               } catch(Exception ex) { %>
               <br/>
               <div class="erreur">
                   <div><%= ex.getMessage() %></div>
               </div>
               <br/>
               <% } %>
        </section>
       <%@include file="./footer.jsp" %>
    </body>
</html>
