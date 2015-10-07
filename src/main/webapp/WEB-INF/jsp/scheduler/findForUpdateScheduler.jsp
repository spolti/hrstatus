<%@page import="br.com.hrstatus.security.Crypto"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../home/navbar.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Atualizar Servidor</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/css/bootstrap-responsive.css"
	rel="stylesheet">
</head>
<body>
	<div class="container">
		<div class="content">
			<div class="row">
				<div class="span12">
					<c:if test="${not empty errors}">
						<div class="alert">
							<button type="button" class="close" data-dismiss="alert">×</button>
							<c:forEach var="error" items="${errors}">
	   		 					${error.category} - ${error.message}<br />
							</c:forEach>
						</div>
					</c:if>

					<form method="POST" action="<c:url value='/updateScheduler'/>">
						<table align=center>
							<br>

							<tr>
								<td align=right>ID:</td>
								<td><input name="scheduler.schedulerId" value="${scheduler.schedulerId}"
									readonly="readonly" /></td>
							</tr>
							<tr>
								<td align=right>Nome Agendamento: </td>
								<td><input name="scheduler.schedulerName"
									value="${scheduler.schedulerName}" readonly="readonly" /></td>
							</tr>
							<tr>
								<td align=right>Agendamento Default:</td>
								<td><input type="radio" name="isDefaultScheduler" value="true" readonly="readonly" ${isDefault} />Sim
									<input type="radio" name="isDefaultScheduler" value="false" readonly="readonly" ${isNotDefault} />Não</td>
							</tr>
							<tr>
								<td align=right>Agendamento Diário:</td>
								<td><input type="radio" name="isEveryday" value="true" readonly="readonly" ${isEveryday} />Sim
									<input type="radio" name="isEveryday" value="false" readonly="readonly" ${isNotEveryday} />Não</td>
							</tr>
							<tr>
								<td align=right>Agendamento Ativo:</td>
								<td><input type="radio" name="isEnabled" value="true" ${isEnabled} />Sim
									<input type="radio" name="isEnabled" value="false" ${isNotEnabled} />Não</td>
							</tr>

							<tr>
								<td align=right></td>
								<td align=left><button align="right" input type=submit
										value="Salvar" class="btn btn-primary">Salvar</button></td>
						</table>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>