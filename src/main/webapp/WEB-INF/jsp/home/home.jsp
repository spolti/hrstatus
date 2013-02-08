<!-- 
    Copyright (C) 2012  Filippe Costa Spolti

	This file is part of Hrstatus.

    Hrstatus is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 -->

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="navbar.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script
	src="${pageContext.request.contextPath}/js/RGraph.common.core.js"></script>
<script
	src="${pageContext.request.contextPath}/js/RGraph.common.tooltips.js"></script>
<script
	src="${pageContext.request.contextPath}/js/RGraph.common.dynamic.js"></script>
<script
	src="${pageContext.request.contextPath}/js/RGraph.common.effects.js"></script>
<script src="${pageContext.request.contextPath}/js/RGraph.pie.js"></script>
<script src="${pageContext.request.contextPath}/js/RGraph.bar.js"></script>


<title>HR Status Home</title>
</head>
<body>

	<div id="myCarousel" class="carousel slide">
		<!-- alterado na linha 399 -->
		<!-- Carousel items -->
		<div class="carousel-inner">

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
												!style="border:1px solid #ccc">[No canvas support]</canvas>
											<script>
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
					
					
					            /**
					            * The ontooltip event runs when a tooltip is shown and creates the Pie chart in the tooltip
					            */
					            //bar8.ontooltip = function (obj)
					            //{
					            //    var pie_data = [
					            //                    [${serversLinuxOK},${serversUnixOK},${serversWindowsOK}],
					            //                    [${serversLinuxNOK},${serversUnixNOK},${serversWindowsNOK}]
					            //                   ]
					                               			
		
					            //    var tooltip = RGraph.Registry.Get('chart.tooltip');
					           //     var index   = tooltip.__index__;
					
					               // var pie = new RGraph.Pie('tooltip_canvas', pie_data[index]);
					           //     pie.Set('chart.labels', ['Linux','Unix','Windows']);
					            //    pie.Set('chart.gutter.top', 10);
					           //     pie.Set('chart.gutter.bottom', 25);
					               // pie.Draw();
					           // }
					        }
   					 </script></td>
									</tr>
									<tr>
										<td colspan="2" align="center"><canvas id="cvs2"
												width="900" height="200">[No canvas support]</canvas> <script>
						        var graph3 = function ()
						        {
						
						            var bar = new RGraph.Bar('cvs2', [[${serversLinuxOK},${serversLinuxNOK}],[${serversUnixOK},${serversUnixNOK}],[${serversWindowsOK},${serversWindowsNOK}],[${otherOK},${otherNOK}]]);
						            //var bar = new RGraph.Bar('cvs2', [[${serversLinuxOK},${serversLinuxNOK}],[${serversUnixOK},${serversUnixNOK}],[${serversWindowsOK},serversWindowsNOK]]);
						            bar.Set('chart.labels', ['Linux', 'Unix', 'Windows','Outros']);
						            bar.Set('chart.tooltips', ['Linux OK', 'Linux não OK', 'Unix OK', 'Unix não OK', 'Windows OK', 'Windows não OK','Outros OK','Outros não OK']);
						            bar.Set('chart.tooltips.event', 'onmousemove');
						            bar.Set('chart.ymax', 100);
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

							<div class="active item" align="center">

								<div class="row">

									<div class="span3"></div>

									<div class="span2">
										<form method="GET"
											action="<c:url value='/home/showByStatus/OK'/>">
											<button input="" type="submit" value="Servidores OK"
												class="btn btn-primary">Servidores OK</button>
										</form>

									</div>

									<div class="span2">
										<div class="btn-group">
											<a class="btn btn-primary dropdown-toggle"
												data-toggle="dropdown" href="#"> Iniciar Verificação <span
												class="caret"></span>
											</a>
											<ul class="dropdown-menu">
												<li><a
													href="<c:url value='/home/startVerification/full' />">
														Verificação Completa </a></li>

												<li><a
													href="<c:url value='/home/startVerification/notFull' />">
														Verificação Não Completa </a></li>

											</ul>
										</div>
									</div>

									<div class="span2">
										<form method="GET"
											action="<c:url value='/home/showByStatus/NOK'/>">
											<button input="" type="submit" value="Servidores N OK"
												class="btn btn-primary">Servidores N OK</button>
										</form>
									</div>
									<div class="span3"></div>
								</div>

								<table class="table table-condensed">
									<thead>
										<tr>
											<td>ID</td>
											<td>Servidor</td>
											<td>IP</td>
											<td>SO</td>
											<td>Client Time</td>
											<td>Server Time</td>
											<td>Diference (s)</td>
											<td>Status</td>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="server" items="${server}">
											<tr class="${server.trClass}">
												<td>${server.id}</td>
												<td>${server.hostname}</td>
												<td>${server.ip}</td>
												<td>${server.SO}</td>
												<td>${server.clientTime}</td>
												<td>${server.serverTime}</td>
												<td>${server.difference}</td>
												<td>${server.status}</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
								<c:if test="${not empty errors}">
									<div class="alert">
										<button type="button" class="close" data-dismiss="alert">×</button>
										<c:forEach var="error" items="${errors}">
		   		 				${error.category} - ${error.message}<br />
										</c:forEach>
									</div>
								</c:if>
							</div>
							<!-- Caso seja necessário inserir novo ítem ao carrossel, inserir aqui.  -->
						</div>
					</div>
				</div>
			</div>


		</div>
		<!-- Carousel nav -->
		<a class="carousel-control left" href="#myCarousel" data-slide="prev">&lsaquo;</a>
		<a class="carousel-control right" href="#myCarousel" data-slide="next">&rsaquo;</a>
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