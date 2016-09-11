<%@include file="./includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Don et recupe - Site de dons - Supprimer mon compte</title>
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
           <h1>Supprimer mon compte</h1>
           <%
           try {
               if(request.getAttribute("info")!=null) {
                   int info=Integer.parseInt(request.getAttribute("info").toString());
                   switch(info) {
                       case 1: %>
                       <br/>
                       <div class="cadre2">
                           <br/>
                           <div class="info">Vous devez être connecté pour pouvoir supprimer votre compte.</div>
                           <br/>
                           <div>
                               <a href="./inscription.html" title="S'INSCRIRE" rel="nofollow">S'INSCRIRE</a>
                           </div>
                           <br/>
                       </div>
                       <br/>
                       <%
                       break;
                       }
                   } else if(request.getAttribute("membre")!=null) {
                       Membre membre=(Membre)request.getAttribute(("membre"));
                       if(membre.getTest()==0) {
                       %>
                       <p>Si vous supprimez votre compte, toutes les données lui étant associées seront également supprimées (annonces & messages).</p>
                       <div class="info">Pour supprimer définitivement votre compte, cliquez sur le bouton ci-dessous :</div>
                       <br/>
                       <div id="form">
                           <fieldset>
                               <legend>Supprimer mon compte</legend>
                               <form action="./supprimer-compte.html#form" method="POST">
                                   <div>En cliquant sur ce bouton, je supprime définitivement mon compte.</div>
                                   <input type="submit" value="Supprimer mon compte" name="kermit" />
                               </form>
                           </fieldset>
                       </div>
                       <%
                       } else if(membre.getTest()==1) { %>
                       <br/>
                       <div class="cadre2">
                           <br/>
                           <div class="info">Votre compte est supprimé !</div>
                           <br/>
                       </div>
                       <br/>
                       <%
                       }
                       }
               } catch(Exception ex) { %>
               <br/>
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
