<%@page import="classes.Cgu"%>
<%@include file="./includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Don et recupe - Site de dons - CGU</title>
<meta name="generator" content="NETBEANS 6.9"/>
<meta name="author" content=""/>
<meta name="date" content=""/>
<meta name="copyright" content=""/>
<meta name="keywords" content=""/>
<meta name="description" content="Site de don et recupe - Donnez ce dont vous ne vous servez plus - Récuperation de proximité." />
<meta name="ROBOTS" content="NOINDEX, NOFOLLOW"/>
<meta charset="UTF-8" />
<link href="../CSS/style.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>
</head>
    <body>
        <%@include file="./haut.jsp" %>
        <section>
            <h1>CGU</h1>
            <%
            try {
                if(request.getAttribute("cgu")!=null) {
                    Cgu cgu=(Cgu)request.getAttribute("cgu");
                    Calendar cal=Calendar.getInstance();
                    cal.setTimeInMillis(cgu.getTimestamp());
                    %>
                    <div class="info">Dernière modif, le <%= cal.get(cal.DATE) %>-<%= cal.get(cal.MONTH)+1 %>-<%= cal.get(cal.YEAR) %>.</div>
                    <br/>
                    <div id="form">
                        <fieldset>
                            <legend>CGU</legend>
                            <form action="./edit-cgu.html#form" method="POST">
                                <div>Texte des c.g.u. :</div>
                                <textarea name="texte" id="texte" rows="4" cols="20"><%= cgu.getTexte()%></textarea>
                                <br/>
                                <input type="submit" value="Valider" name="kermit" />
                            </form>
                        </fieldset>
                    </div>
                        <script type="text/javascript">
                            CKEDITOR.replace( 'texte' );
                        </script>
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
    </body>
</html>
