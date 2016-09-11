<%@page import="java.io.File"%>
<%@page import="classes.Img"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="classes.AnnoncePhotos"%>
<%@include file="./includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Don et recupe - Site de dons - Déposer une annonce (Contenu)</title>
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
           <%
           try {
               if(request.getAttribute("info")!=null) {
                   int info=Integer.parseInt(request.getAttribute("info").toString());
                   switch(info) {
                       case 1: %>
                       <br/>
                       <div class="info">Vous devez être connecté pour pouvoir uploader des images.</div>
                       <br/>
                       <div><a href="./deposer-une-annonce.html" title="S'IDENTIFIER" rel="nofollow">S'identifier</a></div>
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
                       AnnoncePhotos annonce=(AnnoncePhotos)request.getAttribute("annonce");
                       %>
                       <h1><%= annonce.getTitre() %></h1>
                       <h2>Photos déjà enregistrées</h2>
                       <div class="cadre3">
                       <div class="photosMini">
                       <%
                       Objet.getConnection();
                       String query="SELECT extension1,extension2,extension3,extension4,extension5 FROM table_annonces WHERE id=? AND id_membre=? LIMIT 0,1";
                       PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                       prepare.setLong(1, annonce.getId());
                       prepare.setLong(2, annonce.getMembre().getId());
                       ResultSet result=prepare.executeQuery();
                       result.next();
                       String extensions[]=new String[5];
                       extensions[0]=result.getString("extension1");
                       extensions[1]=result.getString("extension2");
                       extensions[2]=result.getString("extension3");
                       extensions[3]=result.getString("extension4");
                       extensions[4]=result.getString("extension5");
                       result.close();
                       prepare.close();
                       int nbPhotos=0;
                       Img img=new Img();
                       for(int i=1;i<=5;i++) {
                           String extension=extensions[i-1];
                           if(extension.length()>0) {
                               String filenameMini2=Datas.DIR+"/photos/mini2_"+annonce.getId()+"_"+i+extension;
                               File fileMini2=new File(filenameMini2);
                               if(fileMini2.exists()) {
                                   nbPhotos++;
                                   img.getSize(fileMini2);
                                   int largeur=img.getWidth();
                                   int hauteur=img.getHeight();
                                   %>
                                   <div class="mini">
                                   <img src="./photo-mini-2-<%= annonce.getId()%>-<%= i%><%= extension %>" width="<%= largeur%>" height="<%= hauteur%>" alt="Miniature"/>
                                   </div>
                                   <%
                                   }
                               }
                           }
                       %>
                       </div>
                       </div>
                       <%
                       Objet.closeConnection();
                       if(nbPhotos==0) { %>
                       <br/>
                       <div class="info">Aucune photo encore enregistrée.</div>
                       <br/>
                       <%
                       }
                       %>
                       <p>Vous pouvez si vous le voulez uploader jusqu'à 5 photos, dans le cas de gros fichier nous vous conseillons de les uploader les un après les autres.</p>
                       <div class="info">Pour uploader des photos, utilisez le formulaire ci-dessous :</div>
                       <div id="form">
                           <%
                           if(annonce.getErrorMsg().length()>0) { %>
                           <div class="erreur">
                               <div>Erreur(s) :</div>
                               <br/>
                               <div><%= annonce.getErrorMsg() %></div>
                           </div>
                               <br/><%
                           }
                       if(annonce.getTest()==1) { %>
                       <br/>
                       <div class="info">Photo(s) enregistrée(s).</div>
                       <br/>
                       <% } %>
                           <fieldset>
                               <legend>Photos de mon annonce</legend>
                           <form action="./deposer-annonce-photos.html#form" method="POST" enctype="multipart/form-data">
                               <div>1° photo :</div>
                               <input type="file" name="1" value="" />
                               <div>2° photo :</div>
                               <input type="file" name="2" value="" />
                               <div>3° photo :</div>
                               <input type="file" name="3" value="" />
                               <div>4° photo :</div>
                               <input type="file" name="4" value="" />
                               <div>5° photo :</div>
                               <input type="file" name="5" value="" />
                               <br/>
                               <input type="submit" value="Valider" name="kermit" />
                           </form>
                           </fieldset>
                       </div>
                       <%
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
