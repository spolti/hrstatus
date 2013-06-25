<%@ include file="navbar.jsp"%>

<div id="myCarousel" class="carousel slide">
	<!-- alterado na linha 399 -->
	<!-- Carousel items -->
	<div class="carousel-inner">

		<div class="container">
			<div class="content">
				<div class="row">
					<div class="span12">
						<div class="item" align="center">

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
									<div class="btn-group">
										<a class="btn btn-primary dropdown-toggle"
											data-toggle="dropdown" href="#"> Servidores Não OK <span
											class="caret"></span>
										</a>
										<ul class="dropdown-menu">
											<li><a href="<c:url value='/home/showByStatus/NOK' />">
													Listar Servidores Desatualizados </a></li>

											<li>
												<div id="dynamicURL">
													<a href="montadodinamicamente"> Atualizar Selecionados</a>
												</div>
											</li>

											<li><a href="<c:url value='/updateTimeAllClients' />">
													Atualizar todos </a></li>

										</ul>
									</div>
								</div>

								<div class="span3"></div>
							</div>

							<table class="table table-condensed" id="resultTable">
								<thead>
									<tr>
										<th>ID</th>
										<th>Servidor</th>
										<th>IP</th>
										<th>SO</th>
										<th>Client Time</th>
										<th>Server Time</th>
										<th>Diference (s)</th>
										<th>Status</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="server" items="${server}">
										<tr class="${server.trClass}">
											<td>${server.id}</td>
											<c:if test="${server.trClass == 'success'}">
												<td>${server.hostname}</td>
											</c:if>
											<c:if test="${server.trClass == 'error'}">
												<td><a
													href="<c:url value='/singleServerToVerify/${server.id}' />">${server.hostname}</a></td>
											</c:if>
											<td>${server.ip}</td>
											<td>${server.SO}</td>
											<td>${server.clientTime}</td>
											<td>${server.serverTime}</td>
											<td>${server.difference}</td>
											<td>${server.status}</td>
											<td><c:if test="${server.trClass == 'error'}">
													<div class="find_1">
														<input type="checkbox" value="${server.id}" />
													</div>
												</c:if></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>

							<c:if test="${not empty errors}">
								<div class="alert">
									<button type="button" class="close" data-dismiss="alert">×</button>
									<c:forEach var="error" items="${errors}">
		   		 							${error.message}<br />
									</c:forEach>
								</div>
							</c:if>

							<c:if test="${not empty info}">
								<div class="alert">
									<button type="button" class="close" data-dismiss="alert">×</button>
									${info}
								</div>
							</c:if>
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
									<div class="btn-group">
										<a class="btn btn-primary dropdown-toggle"
											data-toggle="dropdown" href="#"> Servidores Não OK <span
											class="caret"></span>
										</a>
										<ul class="dropdown-menu">
											<li><a href="<c:url value='/home/showByStatus/NOK' />">
													Listar Servidores Desatualizados </a></li>

											<li>
												<div id="dynamicURL">
													<a href="montadodinamicamente"> Atualizar Selecionados</a>
												</div>
											</li>

											<li><a href="<c:url value='/updateTimeAllClients' />">
													Atualizar todos </a></li>

										</ul>
									</div>
								</div>

								<div class="span3"></div>
							</div>

							<table class="table table-condensed" id="resultTable">
								<thead>
									<tr>
										<th>ID</th>
										<th>Servidor</th>
										<th>IP</th>
										<th>SO</th>
										<th>Client Time</th>
										<th>Server Time</th>
										<th>Diference (s)</th>
										<th>Status</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="server" items="${server}">
										<tr class="${server.trClass}">
											<td>${server.id}</td>
											<c:if test="${server.trClass == 'success'}">
												<td>${server.hostname}</td>
											</c:if>
											<c:if test="${server.trClass == 'error'}">
												<td><a
													href="<c:url value='/singleServerToVerify/${server.id}' />">${server.hostname}</a></td>
											</c:if>
											<td>${server.ip}</td>
											<td>${server.SO}</td>
											<td>${server.clientTime}</td>
											<td>${server.serverTime}</td>
											<td>${server.difference}</td>
											<td>${server.status}</td>
											<td><c:if test="${server.trClass == 'error'}">
													<div class="find_1">
														<input type="checkbox" value="${server.id}" />
													</div>
												</c:if></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>

							<c:if test="${not empty errors}">
								<div class="alert">
									<button type="button" class="close" data-dismiss="alert">×</button>
									<c:forEach var="error" items="${errors}">
		   		 							${error.message}<br />
									</c:forEach>
								</div>
							</c:if>

							<c:if test="${not empty info}">
								<div class="alert">
									<button type="button" class="close" data-dismiss="alert">×</button>
									${info}
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
</body>
</html>