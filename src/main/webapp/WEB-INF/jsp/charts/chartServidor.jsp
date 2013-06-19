<%@ include file="../home/navbar.jsp"%>

<div class="container">
	<div class="content">
		<div class="row">
			<div class="span12">
				<div class="item" align="center">
					<p align="center">
						<b> Consolidado </b>
					</p>
					<table>
						<tr>
							<td align="center"><canvas id="cvs"
									!style="border: 1px solid #ccc">[No canvas support]</canvas> <script>
								var graph1 = function() {
								var data = [ ${linux} , ${windows}, ${unix}, ${other}];
								var pie = new RGraph.Pie('cvs', data);
								pie.Set('chart.labels', [ 'Linux', 'Windows','Unix','Outros' ]);
								pie.Set('chart.tooltips', [ 'Linux', 'Windows','Unix','Outros']);
								pie.Set('chart.tooltips.event', 'onmousemove');
								pie.Set('chart.colors', [ '#EC0033', '#A0D300',
										'#FFCD00', '#00B869', '#999999', '#FF7300',
										'#004CB0' ]);
								pie.Set('chart.strokestyle', 'white');
								pie.Set('chart.linewidth', 3);
								pie.Set('chart.shadow', true);
								pie.Set('chart.shadow.offsetx', 2);
								pie.Set('chart.shadow.offsety', 2);
								pie.Set('chart.shadow.blur', 3);
								pie.Set('chart.exploded', 7);
		
								for ( var i = 0; i < data.length; ++i) {
									pie.Get('chart.labels')[i] = pie
											.Get('chart.labels')[i]
											+ ', ' + data[i] + '%';
								}
		
								pie.Draw();
							}
						</script></td>

							<td align="center"><canvas id="cvs1">[No canvas
											support]</canvas> <script>
					        var graph2 = function () 
					        {
					            var bar8 = new RGraph.Bar('cvs1', [${serversOK},${serversNOK}])
					            bar8.Set('chart.labels', ['Servidores OK','Servidores Não OK']);
					            //bar8.Set('chart.tooltips', function (index) {var label = bar8.Get('chart.labels')[index];return '<h2 style="text-align: center">' + label + '</h2><canvas id="tooltip_canvas" width="250" height="110"></canvas>';});
					            bar8.Draw();
					        }
   					 </script></td>
						</tr>
						<tr>
							<td colspan="2" align="center"><canvas id="cvs2" width="900"
									height="200">[No canvas support]</canvas> <script>
						        var graph3 = function ()
						        {						
						            var bar = new RGraph.Bar('cvs2', [[${serversLinuxOK},${serversLinuxNOK}],[${serversUnixOK},${serversUnixNOK}],[${serversWindowsOK},${serversWindowsNOK}],[${otherOK},${otherNOK}]]);
						            //var bar = new RGraph.Bar('cvs2', [[${serversLinuxOK},${serversLinuxNOK}],[${serversUnixOK},${serversUnixNOK}],[${serversWindowsOK},serversWindowsNOK]]);
						            bar.Set('chart.labels', ['Linux', 'Unix', 'Windows','Outros']);
						            bar.Set('chart.tooltips', ['Linux OK', 'Linux não OK', 'Unix OK', 'Unix não OK', 'Windows OK', 'Windows não OK','Outros OK','Outros não OK']);
						            bar.Set('chart.tooltips.event', 'onmousemove');
						            bar.Set('chart.ymax', ${totalServer});
						            bar.Set('chart.strokestyle', 'white');
						            bar.Set('chart.linewidth', 2);
						            bar.Set('chart.shadow', true);
						            bar.Set('chart.shadow.offsetx', 0);
						            bar.Set('chart.shadow.offsety', 0);
						            bar.Set('chart.shadow.blur', 10);
						            bar.Set('chart.hmargin.grouped', 2);
						            
						            bar.Set('chart.title', '');
						            bar.Set('chart.gutter.bottom', 20);
						            bar.Set('chart.gutter.left', 40);
						            bar.Set('chart.gutter.right', 15);
						            bar.Set('chart.colors', [
						                                     RGraph.LinearGradient(bar, 0,225,0,0, 'white', 'rgba(255, 176, 176, 0.5)'),
						                                     RGraph.LinearGradient(bar, 0,225,0,0, 'white', 'rgba(153, 208, 249,0.5)')
						                                    ]); 
						            bar.Set('chart.background.grid.autofit.numhlines', 5);
						            bar.Set('chart.background.grid.autofit.numvlines', 3);
						            
						            // This draws the chart
						            RGraph.Effects.Fade.In(bar, {'duration': 250});        
						        }
    </script></td>
						</tr>


					</table>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
		window.onload = function() {
			graph1();
			graph2();
			graph3(); 
		}
	</script>
</body>
</html>


