<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../home/navbar.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registrar Usuário</title>

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

</head>
<body>

	<div class="container">
		<div class="content">
			<div class="row">
				<div class="span12">
					<div class="alert alert-info">Caso deseje que o sistema crie
						uma senha aleatória deixe os Password e Repita Password em branco.</div>
					<c:if test="${not empty errors}">
						<div class="alert">
							<button type="button" class="close" data-dismiss="alert">×</button>
							<c:forEach var="error" items="${errors}">
		   		 				${error.category} - ${error.message}<br />
							</c:forEach>
						</div>
					</c:if>

					<form method="POST" action="<c:url value='/registerUser'/>">
						<table align="center">
							<br>
							<tr>
								<td align=right>Nome:</td>
								<td><input type="text" name="user.nome" /></td>
							</tr>
							<tr>
								<td align=right>Username:</td>
								<td><input type="text" name="user.username" /></td>
							</tr>
							<tr>
								<td align="right">Password:</td>
								<td><input type="password" name="user.password" /></td>
							</tr>
							<tr>
								<td align="right">Repita password:</td>
								<td><input type="password" name="user.confirmPass" /></td>
							</tr>
							<tr>
								<td align=right>Ativo:</td>
								<td><input type="radio" name="user.enabled" value="1" />Sim
									<input type="radio" name="user.enabled" value="0" checked />Não</td>
							</tr>
							<tr>
								<td align=right>E-mail:</td>
								<td><input type="text" name="user.mail" /></td>
							</tr>
							<tr>
								<td align=right>Perfil:</td>
								<td><select name="user.authority">
										<option value="ROLE_ADMIN">Admin</option>
										<option value="ROLE_USER">User</option>
										<option value="ROLE_REST">User</option>
								</select></td>
							</tr>
							<tr>
								<td align=right>Selecione os Servidores que o usuário terá
									permissão de extrair logs:</td>
								<td><select name="idServer[]" size="${count}" multiple>
										<c:forEach var="servidor" items="${server}">
											<option value="${servidor.id}">${servidor.hostname}</option>
										</c:forEach>
										<option style="display: none" value="notNull" selected></option>
								</select></td>
							</tr>

							<tr>
								<td align=right>Selecionar todos os servidores:</td>
								<td><input type="checkbox" name="checkall"></td>
							</tr>
							<tr>
								<td align=right></td>
								<td align=right><button input type=submit value="Salvar"
										class="btn btn-primary">Salvar</button></td></table>
					</form>
				</d
						iv>
			</div>
		</div>
	</div>
</body>
</html>