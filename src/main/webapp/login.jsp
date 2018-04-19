<%@page contentType="text/html" pageEncoding="UTF-8" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>Page Login</title>
        <meta name="description" content="Ceci est la page de Login de notre site"/>
        <meta name="author" content="Estibal Gerber Schwarz"/>
        <link media="all" href="login.css" rel="stylesheet" type="text/css">
        <link href="https://use.fontawesome.com/releases/v5.0.6/css/all.css" rel="stylesheet">

    </head>
    <body>
        <section id="milieu">
            <img src="logo33.png" alt="logo" title="Ce site a été créé par Claire ESTIBAL, Charlotte GERBER et Thibaut SCHWARTZ" />
            <h1> Veuillez-vous identifier :</h1>
            <div style="color:red">${errorMessage}</div>
            <br>

            <form class="login" action="<c:url value="/" />" method="POST"> <!-- l'action par défaut est l'URL courant, qui va rappeler la servlet -->
                <div>
                    <i class="fas fa-at"></i>
                    <input id ="identifiant" type="text" placeholder="identifiant" name='loginParam'>
                </div>

                <div>
                    <i class="fas fa-key"></i>
                    <input id = "motdepasse" name='passwordParam' type='password' placeholder="mot de passe">
                </div>

                <input id="bouton" type='submit' name='action' value='Se connecter'>

            </form>


            <p id = "listener"> Il y a actuellement ${applicationScope.numberConnected} utilisateurs connectés</p>
        </section>
        <!-- On montre le nombre d'utilisateurs connectés -->
        <!-- Cette information est stockée dans le scope "application" par le listener -->
    </body>
</html>
