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

					<form method="POST" action="<c:url value='/updateDataBase'/>">
						<table align=center>
							<br>
							<input type="hidden" name="server.lastCheck" value="${dataBase.lastCheck}" />
							<input type="hidden" name="server.clientTime" value="${dataBase.clientTime}" />
							<input type="hidden" name="server.serverTime" value="${dataBase.serverTime}" />
							<input type="hidden" name="server.status" value="${dataBase.status}" />
							<input type="hidden" name="server.trClass" value="${dataBase.trClass}" />

							<tr>
								<td align=right>Data Base ID:</td>
								<td><input name="dataBase.id" value="${dataBase.id}"
									readonly="readonly" /></td>
							</tr>
							<tr>
								<td align=right>IP:</td>
								<td><input type="text" name="dataBase.ip"
									value="${dataBase.ip}" /></td>
							</tr>
							<tr>
								<td align=right>Hostname:</td>
								<td><input type="text" name="dataBase.hostname"
									value="${dataBase.hostname}" /></td>
							</tr>
							<tr>
								<td align=right>Instancia:</td>
								<td><input type="text" name="dataBase.instance"
									value="${dataBase.instance}" /></td>
							</tr>
							<tr>
								<td align=right>Usuário:</td>
								<td><input type="text" name="dataBase.user"
									value="${dataBase.user}" /></td>
							</tr>
							<tr>
								<td align=right>Senha:</td>
								<td><input type="password" name="dataBase.pass"
									value="${dataBase.pass}" /></td>
							</tr>
							<tr>
								<td align=right>Query:</td>
								<td><input type="text" name="dataBase.queryDate"
									value="${dataBase.queryDate}" /></td>
							</tr>
							<tr>
								<td align=right>Porta:</td>
								<td><input type="text" name="dataBase.port"
									value="${dataBase.port}" /></td>
							</tr>
							<tr>
								<td align=right>Banco de Dados:</td>
								<td><select name="dataBase.VENDOR" id="dataBase.VENDOR">
										<c:forEach items="${VENDOR}" var="VENDOR">
											<option value="<c:out value="${VENDOR}" />">${VENDOR}</option>
										</c:forEach>
								</select></td>

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