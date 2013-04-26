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
<%@ include file="../home/navbar.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Configurar Usuários</title>
</head>
<body>

	<div class="container">
		<div class="content">
			<div class="row">
				<div class="span12">
					<table class="table table-striped" id="resultTable">
						<thead id="resultTable">
							<tr>
								<th>Nome</th>
								<th>Username</th>
								<th>Enabled</th>
								<th>E-mail</th>
								<th>Perfil</th>
								<th>Ações&nbsp;<a href="<c:url value="/newUser"/>"
									title="Novo Usuário"><i class="icon-plus-sign"> </i></a></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="user" items="${user}">
								<tr>
									<td>${user.nome}</td>
									<td>${user.username}</td>
									<td>${user.enabled}</td>
									<td>${user.mail}</td>
									<td>${user.authority}</td>
									<td><a
										href="<c:url value='/findForUpdateUser/${user.username}/0' />"
										title="Editar Usuário"><i class="icon-edit"> </i></a> &nbsp;
										<a
										href="javascript:setParameterUser('${user.username}' ,'${user.nome}');" title="remover Usuário"><i
											class="icon-remove"> </i></a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- Begin ModalDeleteUser -->
	<form method="post" action="<c:url value='/deleteUserByID'/>"
		onload="javascript:getParameter()">
		<div class="modal" id="ModalDeleteUser" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true"
			style="display: none">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h3 id="myModalLabel">Deseja realmente deletar este usuário?</h3>
			</div>
			<div id="delete-modalUser" class="modal-body">
				<input type="hidden" id="username" name="username">
				<p align="center"></p>

			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
				<button type=submit class="btn btn-primary" name="_method"
					value="DELETE">Delete</button>
			</div>
		</div>
	</form>
	<!-- End forms delete bottons table -->
</body>
</html>