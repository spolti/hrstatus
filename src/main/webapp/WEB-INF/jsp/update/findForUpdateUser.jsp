<%@page import="br.com.hrstatus.security.Crypto"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../home/navbar.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Atualizar Usuário</title>
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
					<div class="alert alert-info">Só preencha o Password se
						desejar trocar o mesmo.</div>
					<c:if test="${not empty errors}">
						<div class="alert">
							<button type="button" class="close" data-dismiss="alert">×</button>
							<c:forEach var="error" items="${errors}">
	   		 					${error.category} - ${error.message}<br />
							</c:forEach>
						</div>
					</c:if>

					<form method="POST" action="<c:url value='/updateUser'/>">
						<table align=center>
							<input type="hidden" name="user.firstLogin"
								value="${user.firstLogin}" />
							<input type="hidden" name="user.lastLogin"
								value="${user.lastLogin}" />
							<tr>
								<td align=right>Username :</td>
								<td><input name="user.username" value="${user.username}"
									readonly="readonly" /></td>
							</tr>
							<tr>
								<td align=right>Nome:</td>
								<td><input type="text" name="user.nome"
									value="${user.nome}" /></td>
							</tr>
							<tr>
								<td align=right>Password:</td>
								<td><input type="password" name="user.password" /></td>
							</tr>
							<tr>
								<td align="right">Repita password:</td>
								<td><input type="password" name="user.confirmPass" /></td>
							</tr>
							<tr>
								<td align=right>Ativo:</td>
								<td><input type="radio" name="user.enabled" value="1"
									<c:if test="${user.enabled}"> checked="checked" </c:if> />Sim
									<input type="radio" name="user.enabled" value="0"
									<c:if test="${not user.enabled}"> checked="checked" </c:if> />Não</td>
							</tr>
							<tr>
								<td align=right>E-mail:</td>
								<td><input type="text" name="user.mail"
									value="${user.mail}" /></td>
							</tr>
							<tr>
								<td align=right>Perfil:</td>
								<td><select name="user.authority" ${isDisabled}>
										<option value="ROLE_ADMIN"
											<c:if test="${user.authority == 'ROLE_ADMIN'}"> selected="selected" </c:if>>Admin</option>
										<option value="ROLE_USER"
											<c:if test="${user.authority == 'ROLE_USER'}"> selected="selected" </c:if>>User</option>
										<option value="ROLE_REST"
											<c:if test="${user.authority == 'ROLE_REST'}"> selected="selected" </c:if>>Rest Request</option>	
											
								</select></td>
							</tr>

							<tr>
								<td align=right>Selecione os Servidores que o usuário terá
									permissão de extrair logs:</td>
								<td><select name="idServer[]" size="${count}" multiple>
										<c:forEach var="servidor" items="${server}">
											<option
												<c:if test="${not empty isDisabled}"> style="display: none" </c:if>
												value="${servidor.id}" ${servidor.selected}>${servidor.hostname}</option>
										</c:forEach>
										<option style="display: none" value="notNull" selected></option>
								</select></td>
							</tr>

							<tr>
								<td align=right>Selecionar todos os servidores:</td>
								<td><input
									<c:if test="${not empty isDisabled}"> disabled </c:if>
									type="checkbox" name="checkall"></td>
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