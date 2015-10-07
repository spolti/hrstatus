<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../home/navbar.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Configuração Clientes</title>

</head>
<body>

	<div class="container">
		<div class="content">
			<div class="row">
				<div class="span12">
					<table class="table table-striped" id="resultTable">
						<thead>
							<tr>
								<th>ID Agendamento</th>
								<th>Nome Agendamento</th>
								<th>Ativo?</th>
								<th>Agendamento Default?</th>
								<th>Executar todos os dias?</th>
								<th>Executado?</th>
								<th>Iniciado em</th>
								<th>Terminado em</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="schedulerHistory" items="${schedulerHistory}">
								<tr>
									<td>${schedulerHistory.schedulerId}</td>
									<td>${schedulerHistory.schedulerName}</td>
									<td>${schedulerHistory.enabled}</td>
									<td>${schedulerHistory.defaultScheduler}</td>
									<td>${schedulerHistory.everyday}</td>
									<td>${schedulerHistory.finished}</td>
									<td>${schedulerHistory.startedAt}</td>
									<td>${schedulerHistory.finishedAt}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

</body>
</html>