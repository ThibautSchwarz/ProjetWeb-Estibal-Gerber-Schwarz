<%-- 
    Document   : client
    Created on : 27 févr. 2018, 23:13:40
    Author     : 
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Edition des bons de ${userName} (AJAX)</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- On charge jQuery -->
        <script	src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <!-- On charge le moteur de template mustache https://mustache.github.io/ -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/mustache.js/0.8.1/mustache.min.js"></script>
        <script>

            $(document).ready(// Exécuté à la fin du chargement de la page
                    function () {
                        // On montre la liste des commandes du client
                        showOrders();
                    }
            );

            function showOrders() {
                // On fait un appel AJAX pour chercher les commandes passées par le client
                $.ajax({
                    url: "allPurchaseOrders",
                    data: $("#username").text(), // le nom du client qui est aussi le nom de session, est passé en paramètre
                    dataType: "json",
                    error: showError,
                    success: // La fonction qui traite les résultats
                            function (result) {
                                // Le code source du template est dans la page
                                var template = $('#ordersTemplate').html();
                                // On combine le template avec le résultat de la requête
                                var processedTemplate = Mustache.to_html(template, result);
                                // On affiche la liste des options dans le select
                                $('#orders').html(processedTemplate);
                            }
                });
            }


            // Ajouter une commande
            function addOrder() {
                $("#orderForm").submit();
                $.ajax({
                    url: "addOrder",
                    data: $("#orderForm").serialize(), // serialize() renvoie tous les paramètres saisis dans le formulaire
                    dataType: "json",
                    success: // La fonction qui traite les résultats
                            function (result) {// quand la commande est affichée on actualise le tableau de commandes
                                showOrders();// actualisation du tableau
                             },
                    error: showError
                });
                return false;
            }

            // modification d'une commande
            function updateOrder() {
                $("#orderForm").submit();
                $.ajax({
                    url: "updateOrder",
                    data: $("#orderForm").serialize(),
                    dataType: "json",
                    success: // La fonction qui traite les résultats
                            function (result) {
                                showOrders();// actualisation du tableau
                            },
                    error: showError
                });
                return false;
            }

            // Supprimer une commande
            function deleteOrder(num) {
                $.ajax({
                    url: "deleteOrder",
                    data: {"num": num},
                    dataType: "json",
                    success:
                            function (result) {
                                showOrders();
                                alert(result);
                            },
                    error: showError
                });
                return false;
            }

            // Fonction qui traite les erreurs de la requête
            function showError(xhr, status, message) {
                console.log(JSON.parse(xhr.responseText).message);
            }

        </script>
        <!-- un CSS pour formatter la table -->
        <link rel="stylesheet" type="text/css" href="css/tableformat.css">
        <link media="all" href="css/client.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <header>
            <img src="logo33.png" alt="logo" title="Ce site a été créé par Claire ESTIBAL, Charlotte GERBER et Thibaut SCHWARZ" />
            <h1>Edition des bons de commande de ${userName}</h1>
            <form action="<c:url value="/"/>" method="POST">
                <input type='submit' name='action' value='Se deconnecter'>
            </form>
        </header>

        <div id="username" >${userName}</div>

        <!-- On montre le formulaire de saisie -->
        <div id="orders"></div> <!-- La zone où les résultats vont s'afficher -->

        <section>
            <form id="orderForm">
                <h2>Saisie d'un bon de commande</h2>
                <div id="formulaire">
                    <label for="num"> No de commande : </label><input id="num" name="num" pattern="\d+" title="Doit être une suite de chiffres"><br>
                    <label for="prodid"> Id produit : </label><input id="prodid" name="prodid"  pattern="\d+" title="Doit être une suite de chiffres"><br>
                    <label for="qt">Quantité : </label><input id="qt" name="qt" min="1"   step="1" title="Entrez un nombre"><br> 
                    <label for="shipcost"> Frais de transport</label><input id="shipcost" name="shipcost" pattern="\d+" title="Entrez un nombre"><br>
                    <label for="comp"> Compagnie de livraison : </label><input name="comp" id="comp" type="text"><br>
                </div>
                <div>
                    <input id="username" name="username" type="hidden" value="${userName}">
                    <input class="boutonBasique" type="reset" id="annuler" value="Annuler">
                    <input class="boutonBasique" id="ajouter" type="button"  value="Ajouter" onClick='addOrder();'>
                    <input class="boutonBasique" id="modifier" type="button"  value ="Modifier" onClick="updateOrder();">
                </div>
            </form>
        </section>

        <!-- Le template qui sert à formatter la liste des codes -->
        <script id="ordersTemplate" type="text/template">
            <TABLE>
            <tr><th>num de commande</th><th>id client</th><th>id produit</th><th>quantité</th><th>frais de transport</th><th>date d'achat</th><th>date d'expedition</th><th>transport</th><th>Action</th></tr>
            {{! Pour chaque enregistrement }}
            {{#records}}
            {{! Une ligne dans la table }}
            <TR><TD>{{ordernum}}</TD><TD>{{cusId}}</TD><TD>{{productId}}</TD><TD>{{quantity}}</TD><TD>{{cost}}</TD><TD>{{salesDate}}</TD><TD>{{shippingDate}}</TD><TD>{{Company}}</TD><TD><button class="boutonTableau" onclick="deleteOrder('{{ordernum}}')">Supprimer</button></TD></TR>
            {{/records}}
            </TABLE>
        </script>
    </body>
</html>

