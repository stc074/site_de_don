<%@page import="java.util.Calendar"%>
<%@page import="classes.Message"%>
<%@include file="./includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Don et recupe - Site de dons - Message envoyé</title>
<meta name="generator" content="NETBEANS 6.9"/>
<meta name="author" content=""/>
<meta name="keywords" content=""/>
<meta name="description" content="Site de don et recupe - Donnez ce dont vous ne vous servez plus - Récuperation de proximité." />
<meta name="ROBOTS" content="NOINDEX, NOFOLLOW"/>
<meta charset="UTF-8" />
<link rel="icon" type="image/png" href="./GFXs/favicon.png" />
<link href="./CSS/style.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="./ckeditor/ckeditor.js"></script>
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
           <h1>Message envoyé</h1>
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
                       case 2: %>
                       <div class="cadre2">
                       <br/>
                       <div class="info">Message inconnu !</div>
                       <br/>
                       </div>
                       <br/>
                       <%
                       break;
                       }
                   } else if(request.getAttribute("message")!=null) {
                       Message message=(Message)request.getAttribute("message");
                       //out.println(message.getErrorMsg());
                       Calendar cal=Calendar.getInstance();
                       %>
                       <h2>Annonce :: <%= message.getTitreAnnonce() %></h2>
                       <%
                       if(message.getIdPrec()!=0) {
                           cal.setTimeInMillis(message.getTimestampPrec());
                           %>
                           <div class="cadre2">
                               <br/>
                           <div class="info"><%= message.getPseudoDestinataire() %> vous avez écrit, le <%= cal.get(cal.DATE) %>-<%= cal.get(cal.MONTH)+1 %>-<%= cal.get(cal.YEAR) %> :</div>
                           <h2><%= message.getTitrePrec() %></h2>
                           <%= message.getContenuPrec() %>
                           </div>
                           <br/>
                           <%
                           cal.setTimeInMillis(message.getTimestamp());
                           %>
                           <div class="cadre2">
                               <br/>
                           <div class="info">Vous avez répondu à <%= message.getPseudoDestinataire() %>, le <%= cal.get(cal.DATE) %>-<%= cal.get(cal.MONTH)+1 %>-<%= cal.get(cal.YEAR) %> :</div>
                           <%
                           } else {
                           cal.setTimeInMillis(message.getTimestamp());
                           %>
                           <div class="cadre2">
                               <br/>
                           <div class="info">Vous avez écrit à <%= message.getPseudoDestinataire() %>, le <%= cal.get(cal.DATE) %>-<%= cal.get(cal.MONTH)+1 %>-<%= cal.get(cal.YEAR) %> :</div>
                           <%
                           }
                       %>
                       <h2><%= message.getTitre() %></h2>
                       <%= message.getContenu() %>
                           </div>
                       <br/>
                       <%
                       if(message.getIdPrec()!=0) {
                       if(message.getTest()==0) { %>
                       <div class="info">Pour re-répondre à <%= message.getPseudoDestinataire() %>, utilisez le formulaire ci-dessous :</div>
                       <br/>
                       <div id="form">
                           <%
                           if(request.getParameter("kermit")!=null&&message.getErrorMsg().length()>0) { %>
                           <div class="erreur">
                               <div>Erreur(s) :</div>
                               <br/>
                               <div><%= message.getErrorMsg() %></div>
                           </div>
                           <br/>
                           <% } %>
                           <fieldset>
                               <legend>Re-répondre à <%= message.getPseudoDestinataire() %> :</legend>
                               <form action="./message-envoye.html#form" method="POST">
                                   <input type="hidden" name="idMessage" value="<%= message.getId()%>" />
                                   <div>Objet de votre message :</div>
                                   <input type="text" name="titreMsg" value="<%= message.getTitreMsg()%>" size="30" maxlength="40" />
                                   <div>Contenu de votre message :</div>
                                   <textarea name="contenuMsg" id="contenuMsg" rows="4" cols="20"><%= message.getContenuMsg()%></textarea>
                                   <br/>
                                   <div class="captcha"></div>
                                   <div class="captchaDroite">
                                       <span>&rarr;Copiez le code SVP&rarr;</span>
                                       <input type="text" name="captcha" value="" size="5" maxlength="5" />
                                   </div>
                                   <br/>
                                   <br/>
                                   <input type="submit" value="Valider" name="kermit" />
                                   <br/>
                               </form>
                           </fieldset>
                             <script type="text/javascript">
                            CKEDITOR.replace( 'contenuMsg' );
                        </script>
                       </div>
                                   <%
                                   } else if(message.getTest()==1) { %>
                                   <br/>
                                   <div class="info">Message envoyé à <%= message.getPseudoDestinataire() %>.</div>
                                   <br/>
                                   <%
                                   message.blank();
                                   }
                       }
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
