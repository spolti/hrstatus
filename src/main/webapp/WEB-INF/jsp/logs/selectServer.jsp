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
								<th>ID</th>
								<th>Servidor</th>
								<th>Diretório de logs</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="servidor" items="${server}">
								<tr>
									<td>${servidor.id}</td>
									<td><a href="<c:url value='/listLogFiles/${servidor.hostname}' />" value="${servidor.hostname}">${servidor.hostname}</a></td>
									<td>${servidor.logDir}</td>
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