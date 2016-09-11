<%@include file="./includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Don et recupe - Site de dons - AJOUTER CATEGORIE</title>
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
            <h1>Ajouter une catégorie</h1>
            <%
            try {
                if(request.getAttribute("categorie")!=null) {
                    Categorie categorie=(Categorie)request.getAttribute("categorie");
                    if(categorie.getTest()==1) { %>
                    <br/>
                    <div class="info">Catégorie "<%= categorie.getCategorie() %>" enregistrée !</div>
                    <br/>
                    <% } %>
            <br/>
            <div id="form">
                <%
                if(request.getParameter("kermit")!=null&&categorie.getErrorMsg().length()>0) { %>
                <div class="erreur">
                    <div>Erreur(s) :</div>
                    <br/>
                    <div><%= categorie.getErrorMsg() %></div>
                </div><% } %>
                <form action="AjouterCategorie#form" method="POST">
                    <div>Catégorie :</div>
                    <input type="text" name="categorie" value="" size="20" maxlength="40" />
                    <br/>
                    <input type="submit" value="Valider" name="kermit" />
                </form>
            </div>
                <h2>Catégories enregistrées</h2>
                    <%
                    Objet.getConnection();
                    String query="SELECT categorie FROM table_categories ORDER BY categorie ASC";
                    Statement state=Objet.getConn().createStatement();
                    ResultSet result=state.executeQuery(query);
                    int nbCategories=0;
                    while(result.next()) {
                        nbCategories++;
                        String cat=result.getString("categorie");
                        %>
                        <div><%= cat %></div>
                        <br/>
                        <%
                        }
                    result.close();
                    state.close();
                    if(nbCategories==0) { %>
                    <br/>
                    <div class="info">Aucune catégorie enregistrée.</div>
                    <br/>
                    <% }
                    }
                    } catch(Exception ex) { %>
                    <br/>
                    <div class="erreur">
                        <div><%= ex.getMessage() %></div>
                    </div>
                    <br/>
                    <% } %>
        </section>
    </body>
</html>
