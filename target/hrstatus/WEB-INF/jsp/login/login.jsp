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
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>HrStatus login</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="author" content="Spolti">

<link href="${pageContext.request.contextPath}/css/bootstrap.css"
	rel="stylesheet">
<style type="text/css">
body {
	padding-top: 40px;
	padding-bottom: 40px;
	background-color: #f5f5f5;
}

.form-signin {
	max-width: 300px;
	padding: 19px 29px 29px;
	margin: 0 auto 20px;
	background-color: #fff;
	border: 1px solid #e5e5e5;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px;
	-webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
	-moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
	box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
}

.form-signin .form-signin-heading,.form-signin .checkbox {
	margin-bottom: 10px;
}

.form-signin input[type="text"],.form-signin input[type="password"] {
	font-size: 16px;
	height: auto;
	margin-bottom: 15px;
	padding: 7px 9px;
}
</style>
<link
	href="${pageContext.request.contextPath}/css/bootstrap-responsive.css"
	rel="stylesheet">

</head>

<body>
	<script src="http://code.jquery.com/jquery-latest.js" />
	</script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

	<c:if test="${not empty info}">
		<div class="alert alert-info">
			<button type="button" class="close" data-dismiss="alert">×</button>
			${info}
		</div>
	</c:if>
	<div class="container">

		<form class="form-signin" name="f"
			action="<c:url value='j_spring_security_check'/>" method="POST">

			<c:if test="${not empty param.login_error}">
				<font color="red"> Your login attempt was not successful, try
					again.<br> <br> Reason: <c:out
						value="${SPRING_SECURITY_LAST_EXCEPTION.message}"></c:out>.
				</font>
			</c:if>
			<h2 class="form-signin-heading" align="center">Login</h2>
			<input type="text" name='j_username'
				value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'
				class="input-block-level" placeholder="Username"> <input
				type="password" name='j_password' class="input-block-level"
				placeholder="Password">

			<table>
				<tbody>
					<tr>
						<td><label class="checkbox"><input
								name="_spring_security_remember_me" value="remember-me"
								type="checkbox"> Lembre-me </label></td>

						<td><a href="#SubmitUser" data-toggle="modal" role="button">
								Recuperar Senha </a></td>
					</tr>
					<tr>
						<td><button class="btn btn-large btn-primary" type="submit">Sign
								in</button></td>
					</tr>
				</tbody>
			</table>
		</form>

	</div>
	<!-- /container -->
	<!-- Modal -->
	<form method="post" action="<c:url value='/requestNewPass'/>">
		<div id="SubmitUser" class="modal hide fade" style="display: none;">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h3 id="myModalLabel" align="center">Recuperar senha</h3>
			</div>
			<div class="modal-body">
				<p align="center">
					Usuário: <input type="text" name="username" />
				</p>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
				<button type="submit" class="btn btn-primary">Enviar</button>
			</div>
		</div>
	</form>
	
	<div id="footer">
		<div class="container">
			<p align="center" class="muted credit">
				Versão: ${version }
			</p>
		</div>
	</div>

</body>
</html>
