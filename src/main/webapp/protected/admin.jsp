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
            var datas = dataArray;//google.visualization.arrayToDataTable(dataArray);
            datas.sort([{column: 1, desc: true}, {column: 0}]);
            var options = {
                title: title,
                colors : ['#762E7D']
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
                data.addColumn('number', 'chiffres d\'affaire');
                for (let i = 0; i < arrayClients.length; i++) {
                    data.addRow([arrayClients[i], arrayRes[i]]);
                }
                drawChart(data, 'Chiffres d\'affaire par client','salesbycus');
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
                data.addColumn('number', 'chiffres d\'affaire');
                for (let i = 0; i < arrayTypes.length; i++) {
                    data.addRow([arrayTypes[i], arrayRes[i]]);
                }
                drawChart(data, 'Chiffres d\'affaire par type','salesbytype');
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
                data.addColumn('number', 'chiffres d\'affaire');
                for (let i = 0; i < arrayStates.length; i++) {
                    data.addRow([arrayStates[i], arrayRes[i]]);
                }
                drawChart(data, 'Chiffres d\'affaire par état','salesbystate');
        }
        
        function doload(){
            salesByCus();
            salesByType();
            salesByState();
        }
    </script>
</head>
<body >
    <form action="<c:url value="/"/>" method="POST"> 
        <input type='submit' name='action' value='Se deconnecter'>
    </form>
    <h1>Admin :  - Statistiques des commandes</h1>
    <!-- CA par clients -->
    <form id="periode" action="<c:url value="/admin"/>" method="POST" onclick="doload();">
        <fieldset><legend>chiffres d'affaire par client</legend>
            Début de la période : <input id="debut1" name="debut1" type="date" value="2010-01-01"><br>
            Fin de la période : <input id="fin1" name="fin1" type="date" value="2018-01-01"><br>
        </fieldset>
         <fieldset><legend>chiffres d'affaire par type</legend>
            Début de la période : <input id="debut2" name="debut2" type="date" value="2010-01-01"><br>
            Fin de la période : <input id="fin2" name="fin2" type="date" value="2018-01-01"><br>
        </fieldset>
        <fieldset><legend>chiffres d'affaire par état</legend>
            Début de la période : <input id="debut3" name="debut3" type="date" value="2010-01-01"><br>
            Fin de la période : <input id="fin3" name="fin3" type="date" value="2018-01-01"><br>
        </fieldset>
            <input id="modifier" type="submit" name = "action" value ="regler">
    </form>
    <!-- Le graphique apparaît ici -->
    <div  id="res1">${resultats1}</div>
    <div  id="clients">${clients}</div>
    <div id="salesbycus" style="width: 900px; height: 500px;"></div>
    
    <!-- Le graphique apparaît ici -->
    <div  id="res2">${resultats2}</div>
    <div  id="types">${types}</div>
    <div id="salesbytype" style="width: 900px; height: 500px;"></div>
    
    <div  id="res3">${resultats3}</div>
    <div  id="states">${states}</div>
    <div id="salesbystate" style="width: 900px; height: 500px;"></div>
</body>
</html>