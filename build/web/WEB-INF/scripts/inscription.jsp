<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@include file="./includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Don et recupe - Site de dons - Inscription</title>
<meta name="generator" content="NETBEANS 6.9"/>
<meta name="author" content=""/>
<meta name="keywords" content=""/>
<meta name="description" content="Site de don et recupe - Donnez ce dont vous ne vous servez plus - Récuperation de proximité." />
<meta name="ROBOTS" content="NOINDEX, NOFOLLOW"/>
<meta charset="UTF-8" />
<link rel="icon" type="image/png" href="./GFXs/favicon.png" />
<link href="./CSS/style.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="./scripts/scripts.js"></script>
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
           <h1>Don et recupe - Inscription</h1>
           <div class="info">Pour vous inscrire, utilisez le formulaire ci-dessous.</div>
           <br/>
           <%
           try {
           if(request.getAttribute("membre")!=null) {
            Membre membre=(Membre)request.getAttribute("membre");
            if(membre.getTest()==0) {
            %>
           <div id="form">
               <%
               if(request.getParameter("kermit")!=null&&membre.getErrorMsg().length()>0) { %>
               <div class="erreur">
                   <div>Erreur(s) :</div>
                   <br/>
                   <div><%= membre.getErrorMsg() %></div>
               </div>
               <br/><% } %>
               <fieldset>
                   <legend>Formulaire d'inscription</legend>
               <form action="./inscription.html#form" method="POST">
                   <div>Choisissez un PSEUDONYME (Caractères alphanumériques) :</div>
                   <input type="text" name="pseudo" value="<%= membre.getPseudo()%>" size="15" maxlength="20" />
                   <div>Saisissez votre ADRESSE EMAIL :</div>
                   <input type="text" name="email" value="<%= membre.getEmail()%>" size="40" maxlength="200" />
                   <div>Votre MOT DE PASSE (Carcatères alphanumériques) :</div>
                   <input type="password" name="motDePasse" value="" size="15" maxlength="15" />
                   <div>Confirmation du MOT DE PASSE :</div>
                   <input type="password" name="motDePasse2" value="" size="15" maxlength="15" />
                   <div>Localisation :</div>
                   <div>
                       <span>Votre région :</span>
                       <select name="idRegion" onchange="javascript:changeRegion(this.value);">
                           <option value="0"<% if(membre.getIdRegion().equals("0")) out.print(" selected=\"selected\""); %>>Choisissez</option>
                           <%
                           Objet.getConnection();
                           String query="SELECT id_region,region FROM table_regions ORDER BY region";
                           Statement state=Objet.getConn().createStatement();
                           ResultSet result=state.executeQuery(query);
                           while(result.next()) {
                               String idRegion=result.getString("id_region");
                               String region=result.getString("region");
                               %>
                               <option value="<%= idRegion %>"<% if(membre.getIdRegion().equals(idRegion)) out.print(" selected=\"selected\""); %>><%= region %></option>
                               <%
                               }
                           result.close();
                           state.close();
                           %>
                       </select>
                   </div>
                       <div id="innerDepartements">
                           <span>Votre département :</span>
                           <select name="idDepartement" onchange="javascript:changeDepartement(this.value);">
                               <option value="0"<% if(membre.getIdDepartement().equals("0")) out.print(" selected=\"selected\""); %>>Choisissez</option>
                               <%
                               if(!membre.getIdRegion().equals("0")) {
                                   query="SELECT id_departement,departement FROM table_departements WHERE id_region=? ORDER BY id_departement";
                                   PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                                   prepare.setString(1, membre.getIdRegion());
                                   result=prepare.executeQuery();
                                   while(result.next()) {
                                       String idDepartement=result.getString("id_departement");
                                       String departement=result.getString("departement");
                                       %>
                                       <option value="<%= idDepartement %>"<% if(membre.getIdDepartement().equals(idDepartement)) out.print(" selected=\"selected\""); %>><%= idDepartement+"&rarr;"+departement %></option>
                                       <%
                                       }
                                   result.close();
                                   prepare.close();
                                   }
                                   %>
                           </select>
                           <div id="innerCommunes">
                               <span>Votre commune :</span>
                               <select name="idCommune">
                                   <option value="0"<% if(membre.getIdCommune()==0) out.print(" selected=\"selected\""); %>>Choisissez</option>
                                   <%
                                   if(!membre.getIdDepartement().equals("0")) {
                                       query="SELECT id,commune,code_postal FROM table_communes WHERE id_departement=? ORDER BY commune";
                                       PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                                       prepare.setString(1, membre.getIdDepartement());
                                       result=prepare.executeQuery();
                                       while(result.next()) {
                                           int idCommune=result.getInt("id");
                                           String commune=result.getString("commune");
                                           String codePostal=result.getString("code_postal");
                                           %>
                                           <option value="<%= idCommune %>"<% if(membre.getIdCommune()==idCommune) out.print(" selected=\"selected\""); %>><%= codePostal+"&rarr;"+commune %></option>
                                           <%
                                           }
                                       result.close();
                                       prepare.close();
                                       }
                           %>
                               </select>
                           </div>
                       </div>
                               <br/>
                               <div>
                                   <input type="radio" name="cgus" value="1" checked="checked" id="cgus" />
                                   <label for="cgus">En validant ce formulaire, je déclare avoir pris connaissance des conditions générales d'utilisation de ce site et les accepter.</label>
                               </div>
                               <br/>
                                <div>
                                    <div class="captcha"></div>
                                    <div class="captchaDroite">
                                    <span>&rarr;Copier le code SVP &rarr;</span>
                                    <input type="text" name="captcha" value="" size="5" maxlength="5" />
                                    </div>
                                </div>
                               <br/>
                               <input type="submit" value="S'inscrire" name="kermit" />
                               <br/>
               </form>
               </fieldset>
           </div>
           <%
           Objet.closeConnection();
           } else if(membre.getTest()==1) {
               membre.blank();
               %>
               <br/>
               <div class="info">Vous êtes désormais inscrit(e).</div>
               <br/>
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
