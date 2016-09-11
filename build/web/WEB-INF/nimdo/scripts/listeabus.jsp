<%@include file="./includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Don et recupe - Site de dons - Liste des abus</title>
<meta name="generator" content="NETBEANS 6.9"/>
<meta name="author" content=""/>
<meta name="date" content=""/>
<meta name="copyright" content=""/>
<meta name="keywords" content=""/>
<meta name="description" content="Site de don et recupe - Donnez ce dont vous ne vous servez plus - Récuperation de proximité." />
<meta name="ROBOTS" content="NOINDEX, NOFOLLOW"/>
<meta charset="UTF-8" />
<link href="../CSS/style.css" type="text/css" rel="stylesheet" />
</head>
    <body>
        <%@include file="./haut.jsp" %>
        <section>
            <h1>Liste des abus</h1>
            <%
            try {
                Objet.getConnection();
                String query="SELECT t1.id,t1.timestamp,t2.titre FROM table_abus AS t1,table_annonces as t2 WHERE t2.id=t1.id_annonce ORDER BY t1.timestamp DESC";
                Statement state=Objet.getConn().createStatement();
                ResultSet result=state.executeQuery(query);
                int nbAbus=0;
                Calendar cal=Calendar.getInstance();
                while(result.next()) {
                    nbAbus++;
                    long idAbus=result.getLong("id");
                    long timestamp=result.getLong("timestamp");
                    String titre=result.getString("titre");
                    cal.setTimeInMillis(timestamp);
                    %>
                    <div>
                        <a href="./abus-<%= idAbus %>.html" rel="nofollow"><%= titre %></a>
                        <span>&rarr;Abus signalé le, <%= cal.get(cal.DATE) %>-<%= cal.get(cal.MONTH)+1 %>-<%= cal.get(cal.YEAR) %>.</span>
                    </div>
                    <br/>
                    <%
                    }
                result.close();
                state.close();
                if(nbAbus==0) { %>
                <br/>
                <div class="info">Aucun abus enregistré.</div>
                <br/>
                <%
                }
                Objet.closeConnection();
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
