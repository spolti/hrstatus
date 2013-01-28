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
<title>Configuração Clientes</title>

</head>
<body>

	<div class="container">
		<div class="content">
			<div class="row">
				<div class="span12">
					<table class="table table-striped">
						<thead>
							<tr>
								<td>ID</td>
								<td>Servidor</td>
								<td>IP</td>
								<td>SO</td>
								<td>Usuario</td>
								<td>Password</td>
								<td>Ações&nbsp;<a href="<c:url value="/newServer"/>"
									title="Novo Servidor"><i class="icon-plus-sign"> </i></a></td>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="server" items="${server}">
								<tr>
									<td>${server.id}</td>
									<td>${server.hostname}</td>
									<td>${server.ip}</td>
									<td>${server.SO}</td>
									<td>${server.user}</td>
									<td>${server.pass}</td>
									<td><a
										href="<c:url value='/findForUpdateServer/${server.id}' />"
										title="Editar Servidor"><i class="icon-edit"> </i></a> &nbsp;
										<a
										href="javascript:setParameter('${server.id}' ,'${server.hostname}');" title="remover Servidor"><i
											class="icon-remove"> </i></a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- Begin ModalDelete -->
	<form method="post" action="<c:url value='/deleteServerByID'/>"
		onload="javascript:getParameter()">
		<div class="modal" id="ModalDeleteServer" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true"
			style="display: none">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h3 id="myModalLabel">Deseja realmente deletar este servidor?</h3>
			</div>
			<div id="delete-modal" class="modal-body">
				<input type="hidden" id="id_server" name="id_server">
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


