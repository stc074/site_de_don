<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.util.Calendar"%>
<%@page import="classes.Annonce"%>
<%@include file="./includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Don et recupe - Site de dons - Éditer une annonce</title>
<meta name="generator" content="NETBEANS 6.9"/>
<meta name="author" content=""/>
<meta name="keywords" content=""/>
<meta name="description" content="Site de don et recupe - Donnez ce dont vous ne vous servez plus - Récuperation de proximité." />
<meta name="ROBOTS" content="NOINDEX, NOFOLLOW"/>
<meta charset="UTF-8" />
<link rel="icon" type="image/png" href="./GFXs/favicon.png" />
<link href="./CSS/style.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="./scripts/scripts.js"></script>
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
           <h1>Éditer une annonce</h1>
           <%
           try {
               if(request.getAttribute("info")!=null) {
                   int info=Integer.parseInt(request.getAttribute("info").toString());
                   switch(info) {
                       case 1: %>
                       <br/>
                       <div class="info">Vous devez être connecté pour pouvoir éditer vos annonces.</div>
                       <br/>
                       <div><a href="./inscription.html" title="S'INSCRIRE" rel="nofollow">S'inscrire</a></div>
                       <br/>
                       <%
                       break;
                       case 2: %>
                       <br/>
                       <div class="info">Annonce inconnue !</div>
                       <br/>
                       <%
                       break;
                       }
                   } else if(request.getAttribute("annonce")!=null) {
                       Annonce annonce=(Annonce)request.getAttribute("annonce");
                       if(annonce.getTest()==0) {
                       Calendar cal=Calendar.getInstance();
                       cal.setTimeInMillis(annonce.getTimestamp());
                       %>
                       <div class="info">Dernières modifications effectuées le, <%= cal.get(cal.DATE) %>-<%= cal.get(cal.MONTH)+1 %>-<%= cal.get(cal.YEAR) %>.</div>
                       <br/>
                       <div class="info">Pour modifier le contenu de votre annonce, utilisez le formulaire ci-dessous :</div>
                       <br/>
                       <div id="form">
                           <%
                           if(request.getParameter("kermit")!=null&&annonce.getErrorMsg().length()>0) { %>
                           <div class="erreur">
                               <div>Erreur(s) :</div>
                               <br/>
                               <div><%= annonce.getErrorMsg() %></div>
                           </div>
                           <br/>
                           <% } %>
                           <fieldset>
                               <legend>Contenu de mon annonce</legend>
                               <form action="./edit-annonce.html#form" method="POST">
                                   <input type="hidden" name="idAnnonce" value="<%= annonce.getId()%>" />
                                   <div>
                                       <span>Catégorie de votre annonce :</span>
                                       <select name="idCategorie" onchange="javascript:changeCategorie(this.value);">
                                           <option value="0"<% if(annonce.getIdCategorie()==0) out.print(" selected=\"selected\""); %>>Choisissez</option>
                                           <%
                                           Objet.getConnection();
                                           String query="SELECT id,categorie FROM table_categories ORDER BY categorie ASC";
                                           Statement state=Objet.getConn().createStatement();
                                           ResultSet result=state.executeQuery(query);
                                           while(result.next()) {
                                               long idCategorie=result.getLong("id");
                                               String categorie=result.getString("categorie");
                                               %>
                                               <option value="<%= idCategorie %>"<% if(annonce.getIdCategorie()==idCategorie) out.print(" selected=\"selected\""); %>><%= categorie %></option>
                                               <%
                                               }
                                               result.close();
                                               state.close();
                                               %>
                                       </select>
                                   </div>
                                       <div id="innerSousCategories">
                                           <span>Sous-catégorie de votre annonce :</span>
                                           <select name="idSousCategorie">
                                               <option value="0"<% if(annonce.getIdSousCategorie()==0) out.print(" selected=\"selected\""); %>>Choisissez</option>
                                               <%
                                               if(annonce.getIdCategorie()!=0) {
                                                   query="SELECT id,sous_categorie FROM table_sous_categories WHERE id_categorie=? ORDER BY sous_categorie";
                                                   PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                                                   prepare.setLong(1, annonce.getIdCategorie());
                                                   result=prepare.executeQuery();
                                                   while(result.next()) {
                                                       long idSousCategorie=result.getLong("id");
                                                       String sousCategorie=result.getString("sous_categorie");
                                                       %>
                                                       <option value="<%= idSousCategorie %>"<% if(annonce.getIdSousCategorie()==idSousCategorie) out.print(" selected=\"selected\""); %>><%= sousCategorie %></option>
                                                       <%
                                                       }
                                                   result.close();
                                                   prepare.close();
                                                   }
                                                   %>
                                           </select>
                                       </div>
                                           <div>Courte description de votre annonce :</div>
                                           <input type="text" name="titre" value="<%= annonce.getTitre()%>" size="30" maxlength="80" />
                                           <div>Plus longue DESCRIPTION de votre annonce :</div>
                                           <textarea name="description" id="description" rows="4" cols="20"><%= annonce.getDescription()%></textarea>
                                           <br/>
                                           <div class="captcha"></div>
                                           <div class="captchaDroite">
                                               <span>&rarr;Copier le code SVP&rarr;</span>
                                               <input type="text" name="captcha" value="" size="5" maxlength="5" />
                                           </div>
                                           <br/>
                                           <br/>
                                           <input type="submit" value="Valider" name="kermit" />
                                           <br/>
                               </form>
                           </fieldset>
                       </div>
                        <script type="text/javascript">
                            CKEDITOR.replace( 'description' );
                        </script>                       <%
                       Objet.closeConnection();
                       } else if(annonce.getTest()==1) {
                           annonce.blank();
                           %>
                           <script type="text/javascript">
                               window.location.href="./edit-annonce-photos.html";
                           </script>
                           <%
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
