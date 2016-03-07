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
					['Unix', ${unix}],
					['Windows', ${windows}]
			     ]);
		  // Set chart options
		  var options = {'title':'Consolidado Total Servidores',
		                 'width':400,
		                 'height':300,
		                 'backgroundColor':"transparent"};
		 // Instantiate and draw our chart, passing in some options.
		 var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
		       chart.draw(data, options);
		 }
</script>
<script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['SO', 'Hora Ok', 'Hora N�o Ok'],
		  ['Unix',  ${serversUnixOK}, ${serversUnixNOK}],
          ['Windows',  ${serversWindowsOK}, ${serversWindowsNOK}]
        ]);

        var options = {
          title: 'Rela��o Servidores Atualizados/Desatualizados',
          hAxis: {title: 'Consolidado', titleTextStyle: {color: 'red'}},
          'backgroundColor':"transparent",
          'width':1100,
		  'height':250
        };

        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div_ok_notOk'));
        chart.draw(data, options);
      }
</script>

<script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['Task', 'OK/N�o OK'],
          ['Servidores Atalizados', ${serversOK}],
          ['Servidores desatualizados', ${serversNOK}]
        ]);

        var options = {
          title: 'Comparativo OK/N�o Ok',
          pieHole: 0.4,
          'backgroundColor':"transparent",
          'width':400,
		  'height':300
        };

        var chart = new google.visualization.PieChart(document.getElementById('chart_div_comparativo'));
        chart.draw(data, options);
      }
</script>

<div class="container">
	<div class="content">
		<div class="row">
			<div class="span12">
				<div class="item" align="center">
					<p align="center">
						<b>Servidores - Consolidado </b>
					</p>
					<table>
						<tr>
							<td align="center"><div id="chart_div"></div></td>

							<td align="center"><div id="chart_div_comparativo"></div>
							</td>
   					 </tr>
						<tr>
							<td align="center" colspan="2">
								<div id="chart_div_ok_notOk"></div>
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