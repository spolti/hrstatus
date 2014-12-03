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
								<th>IP</th>
								<th>SO</th>
								<th>Usuario</th>
								<th>Diretŕio de Logs</th>
								<th>Comando NTP</th>
								<th>Verificação Ativa</th>
								<th>Ações&nbsp;<a href="<c:url value="/newServer"/>"
									title="Novo Servidor"><i class="icon-plus-sign"> </i></a></th>
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
									<td>${server.logDir}</td>
									<td>${server.suCommand}</td>
									<td>${server.verify}</td>
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


