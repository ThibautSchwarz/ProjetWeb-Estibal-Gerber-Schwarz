<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <title>Statistiques</title>
    <link rel="icon" href="data:,">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <!-- On charge l'API Google -->
    <script src="https://www.google.com/jsapi" ></script>
    <!-- On charge JQuery -->
    <script  src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js" ></script>
    <script>
        google.load("visualization", "1", {packages: ["corechart"]});
        google.setOnLoadCallback(doload);
        // Après le chargement de la page, on fait l'appel AJAX
        function drawChart(dataArray, title, chartname) {
            var datas = dataArray;
            datas.sort([{column: 1, desc: true}, {column: 0}]);
            var options = {
                title: title,
                colors: ['#762E7D']
            };
            var chart = new google.visualization.ColumnChart(document.getElementById(chartname));
            chart.draw(datas, options);
        }

        function salesByCus() {
            let res = [], clients = [];
            res = $("#res1").text();
            clients = $("#clients").text();
            let arrayRes = res.split(",").map(Number);
            let arrayClients = clients.split(",");
            // On met le descriptif des données
            let data = new google.visualization.DataTable();
            data.addColumn('string', 'clients');
            data.addColumn('number', 'chiffres d\'affaires');
            for (let i = 0; i < arrayClients.length; i++) {
                data.addRow([arrayClients[i], arrayRes[i]]);
            }
            drawChart(data, 'Chiffres d\'affaires par client', 'salesbycus');
        }

        function salesByType() {
            let res = [], types = [];
            res = $("#res2").text();
            types = $("#types").text();
            let arrayRes = res.split(",").map(Number);
            let arrayTypes = types.split(",");
            // On met le descriptif des données
            let data = new google.visualization.DataTable();
            data.addColumn('string', 'types de produits');
            data.addColumn('number', 'chiffres d\'affaires');
            for (let i = 0; i < arrayTypes.length; i++) {
                data.addRow([arrayTypes[i], arrayRes[i]]);
            }
            drawChart(data, 'Chiffres d\'affaires par type', 'salesbytype');
        }

        function salesByState() {
            let res = [], states = [];
            res = $("#res3").text();
            states = $("#states").text();
            let arrayRes = res.split(",").map(Number);
            let arrayStates = states.split(",");
            // On met le descriptif des données
            let data = new google.visualization.DataTable();
            data.addColumn('string', 'états');
            data.addColumn('number', 'chiffres d\'affaires');
            for (let i = 0; i < arrayStates.length; i++) {
                data.addRow([arrayStates[i], arrayRes[i]]);
            }
            drawChart(data, 'Chiffres d\'affaires par état', 'salesbystate');
        }

        function doload() {
            salesByCus();
            salesByType();
            salesByState();
        }
    </script>
    <style>
        @import url(https://fonts.googleapis.com/css?family=Sarala);
        @import url(https://fonts.googleapis.com/css?family=Fira+Sans);


        body{
            margin: 0px;
            padding: 0px;
            font-family: "Sarala", sans-serif;
            background-image: url("NY2.jpg");
            background-color: #F0EEE4;
            background-repeat: repeat;
            background-size: auto;
            background-position: top;
        }
        header{
            background-color: rgba(255,255,255,0.9);
            display: flex;
            justify-content: space-around;
            align-items: center; /*centre sur l'axe secondaire : vertical*/
            font-family: "Fira+Sans", sans-serif;
            color: #762E7D;
        }
        #bouton{/*Bouton se déconnecter*/
            border: none;
            color: white;
            background-color: #762E7D;
            font-size: 1em;
            padding-right: 17%;
            padding-left: 17%;
            padding-top: 10%;
            padding-bottom: 10%;
            cursor: pointer; /*change le curseur*/
        }
        #bouton:hover{
            color: #762E7D;
            background-color: #D3DBDE;
        }
        #periode{/*Ensemble des dates à saisir*/
            border: 2px solid #330E3B;
            background-color: rgba(255,255,255,0.7);
            margin-top: 1%;
            margin-right: 30%;
            margin-left: 30%;
            margin-bottom: 1%;
            padding-left: 5%;
            padding-bottom: 1%;
            font-size: 0.8em;
        }
        #modifier{/*Bouton Régler*/
            border: 2px solid #330E3B;
            color: white;
            background-color: #762E7D;
            margin-top: 3%;
            padding-top: 2%;
            padding-bottom: 2%;
            padding-right: 5%;
            padding-left: 5%;
            cursor: pointer;
        }
        #modifier:hover{
            color: #762E7D;
            background-color: #D3DBDE;
        }
        label{
            display: block;
            width: 200px;
            float: left;
        }
        .graphique {
            margin-right: 1%;
        }
        #ensembleGraphique{
            display: flex;
            justify-content: space-around;
        }
        #res1, #res2, #res3, #clients, #types, #states{
            display: none;
        }
        legend{
            
            font-size: 1.3em;
        }
        fieldset{
            border: none;
        }
        form{
            padding-top: 1%;
        }
        /*---------------------------------------------RESPONSIVE---------------------------------------------*/
        @media screen and (min-width: 1500px){

            .graphique {
                /* La transition s'applique à la fois sur la largeur et la hauteur, avec une durée d'une seconde. */
                -webkit-transition: all 1s ease; /* Safari et Chrome */
                -moz-transition: all 1s ease; /* Firefox */
                -ms-transition: all 1s ease; /* Internet Explorer 9 */
                -o-transition: all 1s ease; /* Opera */
                transition: all 1s ease;
                margin-top: 0.5%;
            }
            .graphique:hover {
                /* L'image est grossie 1.5 fois */
                -webkit-transform:scale(1.5); /* Safari et Chrome */
                -moz-transform:scale(1.5); /* Firefox */
                -ms-transform:scale(1.5); /* Internet Explorer 9 */
                -o-transform:scale(1.5); /* Opera */
                transform:scale(1.5);
                z-index: 1; /*Dans le cas où le graphe se superpose sur ses voisins, il sera au premier plan*/
            }
        }

        @media screen and (max-width: 1500px){
            header img{
                display: none;
            }
            #periode{
                margin-right: 20%;
                margin-left: 20%;
            }
            #ensembleGraphique{
                overflow-x: scroll; /*Scroll les graphiques*/
            }
        }

        @media screen and (max-width: 1000px){
            header h1{
                font-size: 1.5em;
            }
        }

        @media screen and (max-width: 800px){
            header h1{
                display: none;
            }
            header img{
                display: flex;
            }
            #bouton{
                font-size: 0.8em;
                padding-right: 12%;
                padding-left: 12%;
                padding-top: 8%;
                padding-bottom: 8%;
            }
            #periode{
                margin-right: 10%;
                margin-left: 10%;
            }
        }
    </style>
</head>
<body >
    <header>
        <img src="logo33.png" alt="logo" title="Ce site a été créé par Claire ESTIBAL, Charlotte GERBER et Thibaut SCHWARZ" />
        <h1>Admin : Statistiques des commandes</h1>
        <form action="<c:url value="/"/>" method="POST"> <input id="bouton" type='submit' name='action' value='Se deconnecter'></form>
    </header>

    <form id="periode" action="<c:url value="/admin"/>" method="POST" onclick="doload();">
        <fieldset><legend>chiffres d'affaires par client</legend>
            <label for="deb1">Début de la période : </label><input id="debut1" name="debut1" type="date" value="2010-01-01" required><br>
            <label for="fin1">Fin de la période : </label><input id="fin1" name="fin1" type="date" value="2018-01-01"><br>
        </fieldset>

        <fieldset><legend>chiffres d'affaires par type</legend>
            <label for="deb2">Début de la période :</label><input id="debut2" name="debut2" type="date" value="2010-01-01" required><br>
            <label for="fin2">Fin de la période : </label><input id="fin2" name="fin2" type="date" value="2018-01-01"><br>
        </fieldset>

        <fieldset><legend>chiffres d'affaires par état</legend>
            <label for="deb3">Début de la période :</label><input id="debut3" name="debut3" type="date" value="2010-01-01" required><br>
            <label for="fin3">Fin de la période : </label><input id="fin3" name="fin3" type="date" value="2018-01-01"><br>
        </fieldset>

        <input id="modifier" type="submit" name = "action" value ="regler">
    </form>

    <section id="ensembleGraphique">
        <section class="graphique">
            <!-- Le graphique apparaît ici -->
            <div  id="res1">${resultats1}</div>
            <div  id="clients">${clients}</div>
            <div id="salesbycus" style="width: 450px; height: 280px;"></div>
        </section>

        <section class="graphique">
            <!-- Le graphique apparaît ici -->
            <div  id="res2">${resultats2}</div>
            <div  id="types">${types}</div>
            <div id="salesbytype" style="width: 450px; height: 280px;"></div>
        </section>

        <section class="graphique">
            <div  id="res3">${resultats3}</div>
            <div  id="states">${states}</div>
            <div id="salesbystate" style="width: 450px; height: 280px;"></div>
        </section>
    </section>
</body>
</html>