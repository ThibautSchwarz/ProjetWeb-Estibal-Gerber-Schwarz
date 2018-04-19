<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%--
    La servlet fait : session.setAttribute("customer", customer)
    La JSP récupère cette valeur dans ${customer}
--%>

<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>You are connected</title>
        <link media="all" href="affiche.css" rel="stylesheet" type="text/css">
    </head>

    <body>

        <header>
            <img src="logo33.png" alt="logo" title="Ce site a été créé par Claire ESTIBAL, Charlotte GERBER et Thibaut SCHWARZ" />
            <h1>Bienvenue Admin : ${userName}</h1>
            <form action="<c:url value="/"/>" method="POST"> <input id="bouton" type='submit' name='action' value='Se deconnecter'></form>
        </header>

        <section>
            <a href="<c:url value="protected/admin.jsp"/>">Voir les statistiques</a>
            <p>Vous pouvez à présent consulter les statistiques des clients.</p>
        </section>

        <footer>
            <p>Il y a actuellement ${applicationScope.numberConnected} utilisateurs connectés</p>
        </footer>

    </body>

</html>
