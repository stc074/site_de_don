<%@include file="./includes.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Don et recupe - Site de dons - AJOUTER SOUS CATEGORIE</title>
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
            <h1>Ajouter une Sous-catégorie</h1>
            <%
            try {
                if(request.getAttribute("categorie")!=null) {
                    Categorie categorie=(Categorie)request.getAttribute("categorie");
                    %>
                    <div id="form">
                        <%
                        if(request.getParameter("kermit")!=null&&categorie.getErrorMsg().length()>0) { %>
                        <div class="erreur">
                            <div>Erreur(s) :</div>
                            <br/>
                            <div><%= categorie.getErrorMsg() %></div>
                        </div><% } %>
                        <form action="./AjouterSousCategorie#form" method="POST">
                            <div>
                                <span>Catégorie : </span>
                                <select name="idCategorie" onchange="javascript:window.location.href='./AjouterSousCategorie?idCategorie='+this.value;">
                                    <option value="0"<% if(categorie.getIdCategorie()==0) out.print(" selected=\"selected\""); %>>Choisissez</option>
                                    <%
                                    Objet.getConnection();
                                    String query="SELECT id,categorie FROM table_categories ORDER BY categorie ASC";
                                    Statement state=Objet.getConn().createStatement();
                                    ResultSet result=state.executeQuery(query);
                                    while(result.next()) {
                                        long idCategorie=result.getLong("id");
                                        String cat=result.getString("categorie");
                                        %>
                                        <option value="<%= idCategorie %>"<% if(categorie.getIdCategorie()==idCategorie) out.print(" selected=\"selected\""); %>><%= cat %></option>
                                        <%
                                        }
                                    result.close();
                                    state.close();
                                    %>
                                </select>
                            </div>
                                <div>Sous-catégorie :</div>
                                <input type="text" name="sousCategorie" value="" size="20" maxlength="40" />
                                <br/>
                                <input type="submit" value="Valider" name="kermit" />
                        </form>
                    </div>
                                    <%
                                    if(categorie.getIdCategorie()!=0) {
                                        %>
                                        <h2>Sous catégories de la catégorie <%= categorie.getCategorie() %></h2>
                                        <%
                                        query="SELECT sous_categorie FROM table_sous_categories WHERE id_categorie=? ORDER BY sous_categorie ASC";
                                        PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                                        prepare.setLong(1, categorie.getIdCategorie());
                                        result=prepare.executeQuery();
                                        int nbSousCats=0;
                                        while(result.next()) {
                                            nbSousCats++;
                                            String sousCategorie=result.getString("sous_categorie");
                                            %>
                                            <div><%= sousCategorie %></div>
                                            <br/>
                                            <%
                                            }
                                        result.close();
                                        prepare.close();
                                        if(nbSousCats==0) { %>
                                        <br/>
                                        <div class="info">Aucune sous catégorie dans la catégorie "<%= categorie.getCategorie() %>".</div>
                                        <br/>
                                        <%
                                        }
                                        }
                                    Objet.closeConnection();
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
