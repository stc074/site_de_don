<header>
<%
if(request.getAttribute("mbr")!=null) {
    Membre mbr=(Membre)request.getAttribute("mbr");
%>
<div class="connexion">
    <div id="formCnx">
        <%
        if(request.getParameter("kermitCnx")!=null&&mbr.getErrorMsg().length()>0) { %>
        <div class="erreur">
            <div>Erreur(s) :</div>
            <br/>
            <div><%= mbr.getErrorMsg() %></div>
        </div>
        <br/><% } %>
        <form action="#formCnx" method="POST">
            <div>Pseudo :</div>
            <input type="text" name="pseudo" value="<%= mbr.getPseudo()%>" size="10" maxlength="20" />
            <div>Mot de passe :</div>
            <input type="password" name="motDePasse" value="" size="10" maxlength="15" />
            <br/>
            <input type="submit" value="Connexion" name="kermitCnx" />
        </form>
    </div>
            <%
            if(mbr.getId()!=0) { %>
            <div>Statut connecté &rarr; <%= mbr.getPseudo() %></div>
            <% } else { %>
            <div>Statut &rarr; Déconnecté</div>
            <% } %>
            <div><a href="./mdp-oublie.html" title="MOT DE PASSE OUBLIÉ" rel="nofollow">Mot de passe oublié</a></div>
</div>
<%
}
%>
            <div class="logo" onclick="javascript:window.location.href='<%= Datas.URLROOT %>';"></div>
            <div class="clearBoth"></div>
</header>