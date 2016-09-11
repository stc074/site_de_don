<%@include file="./includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Don et recupe - Site de dons - Abus</title>
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
            <h1>Abus</h1>
            <%
            try {
                if(request.getAttribute("abus")!=null) {
                    Abus abus=(Abus)request.getAttribute("abus");
                    out.println(abus.getErrorMsg());
                    %>
                    <div><a href="./ignore-abus-<%= abus.getId() %>.html" rel="nofollow">IGNORER CET ABUS</a></div>
                    <br/>
                    <div><a href="./efface-abus-<%= abus.getId() %>.html" rel="nofollow">EFFACER CETTE ANNONCE</a></div>
                    <br/>
                    <h2><%= abus.getAnnonce().getTitre() %></h2>
                    <%= abus.getAnnonce().getDescription() %>
                    <%
                    }
                } catch(Exception ex) { %>
                <div class="erreur">
                    <div>Erreur :</div>
                    <br/>
                    <div><%= ex.getMessage() %></div>
                </div>
                <% }%>
        </section>
    </body>
</html>
