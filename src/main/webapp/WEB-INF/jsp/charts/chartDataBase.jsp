<%@ include file="../home/navbar.jsp"%>

<!--Load the AJAX API-->
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
			// Load the Visualization API and the piechart package.
     		google.load('visualization', '1.0', {'packages':['corechart']});
			// Set a callback to run when the Google Visualization API is loaded.
    		google.setOnLoadCallback(drawChart);

	       // Callback that creates and populates a data table,
	       // instantiates the pie chart, passes in the data and
	       // draws it.
	       function drawChart() {
		        // Create the data table.
		        var data = new google.visualization.DataTable();
		        data.addColumn('string', 'Topping');
		        data.addColumn('number', 'Slices');
		        data.addRows([
					['MySql', ${mysql}],
					['Oracle', ${oracle}],
					['PostgreSql', ${postgresql}]
			     ]);
		  // Set chart options
		  var options = {'title':'Consolidado Total Banco de Dados',
		                 'width':400,
		                 'height':300,
		                 'backgroundColor':"transparent"};
		 // Instantiate and draw our chart, passing in some options.
		 var chart = new google.visualization.PieChart(document.getElementById('chart_div_db'));
		       chart.draw(data, options);
		 }
</script>

<script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['SO', 'Time Ok', 'Time Not Ok'],
          ['Mysql', ${dbMysqlOK}, ${dbMysqlNOK}],
          ['Oracle',  ${dbOracleOK}, ${dbOracleNOK}],
          ['PostgreSql', ${dbPostgreOK}, ${dbPostgreNOK}]
        ]);

        var options = {
          title: 'Relação Servidores Atualizados/Desatualizados',
          hAxis: {title: 'Consolidado', titleTextStyle: {color: 'red'}},
          'backgroundColor':"transparent",
          'width':1100,
		  'height':250
        };

        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div_db_ok_notOk'));
        chart.draw(data, options);
      }
</script>

<script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['Task', 'OK/Não OK'],
          ['Banco de dados Atalizados', ${databaseOK}],
          ['Banco de dados Desatualizados', ${databaseNOK}]
        ]);

        var options = {
          title: 'Comparativo OK/Não Ok',
          pieHole: 0.4,
          'backgroundColor':"transparent",
          'width':400,
		  'height':300
        };

        var chart = new google.visualization.PieChart(document.getElementById('chart_div_db_comparativo'));
        chart.draw(data, options);
      }
</script>

<div class="container">
	<div class="content">
		<div class="row">
			<div class="span12">
				<div class="item" align="center">
					<p align="center">
						<b>Banco de Dados - Consolidado </b>
					</p>
					<table>
						<tr>
							<td align="center"><div id="chart_div_db"></div></td>
							<td align="center"><div id="chart_div_db_comparativo"></div></td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<div id="chart_div_db_ok_notOk"></div>
							</td>
						</tr>
	</table>
				</div>
			</div>
		</div>
	</div>
</div>

</body>
</html>