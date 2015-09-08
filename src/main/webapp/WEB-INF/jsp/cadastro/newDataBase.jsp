<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../home/navbar.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registrar Servidor</title>

<link href="${pageContext.request.contextPath}/css/bootstrap.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/css/bootstrap-responsive.css"
	rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery.js"
	type="text/javascript"></script>
<script
	src="${pageContext.request.contextPath}/js/jquery.maskedinput-1.3.js"
	type="text/javascript"></script>
<script type="text/javascript">
	jQuery(function($) {
		$("#ip").mask("999.999.999.999");
	});
</script>
</head>
<body>

	<div class="container">
		<div class="content">
			<div class="row">
				<div class="span12">
					<div class="alert alert-info">Caso deseje deseje usar porta e
						query default do banco de dados selecionados deixe os campos Porta
						e Query em branco.</div>
					<c:if test="${not empty errors}">
						<div class="alert">
							<button type="button" class="close" data-dismiss="alert">×</button>
							<c:forEach var="error" items="${errors}">
		   		 ${error.category} - ${error.message}<br />
							</c:forEach>
						</div>
					</c:if>

					<form method="POST" action="<c:url value='/registerDataBase'/>">
						<table align=center>
							<br>
							<tr>
								<td align=right>IP:</td>
								<td><input type="text" name="bancoDados.ip" /></td>
							</tr>
							<tr>
								<td align=right>Hostname:</td>
								<td><input type="text" name="bancoDados.hostname" /></td>
							</tr>
							<tr>
								<td align=right>Instancia:</td>
								<td><input type="text" name="bancoDados.instance" /></td>
							</tr>
							<tr>
								<td align=right>Nome do Banco:</td>
								<td><input type="text" name="bancoDados.db_name" /></td>
							</tr>
							<tr>
								<td align=right>Usuário:</td>
								<td><input type="text" name="bancoDados.user" /></td>
							</tr>
							<tr>
								<td align=right>Senha:</td>
								<td><input type="password" name="bancoDados.pass" /></td>
							</tr>
							<tr>
								<td align="right">Query:</td>
								<td><input type="text" name="bancoDados.queryDate"></td>
							</tr>
							<tr>
								<td align=right>Porta:</td>
								<td><input type="text" name="bancoDados.port" /></td>
							</tr>
							<tr>
								<td align=right>Banco de Dados:</td>
								<td><select name="bancoDados.VENDOR" id="bancoDados.VENDOR">
										<c:forEach items="${VENDOR}" var="VENDOR">
											<option value="<c:out value="${VENDOR}" />">${VENDOR}</option>
										</c:forEach>
								</select></td>
							</tr>

							<tr>
								<td align=right></td>
								<td align=right><button input type=submit value="Salvar"
										class="btn btn-primary">Salvar</button></td>
						</table>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>

