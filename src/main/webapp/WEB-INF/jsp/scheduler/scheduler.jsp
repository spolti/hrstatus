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
				<div class="alert alert-info">Não é possível deletar o agendamento default, somente ativá-lo ou desativá-lo</div>
					<table class="table table-striped" id="resultTable">
						<thead>
							<tr>
								<th>ID</th>
								<th>Nome Agendamento</th>
								<th>Ativo?</th>
								<th>Agendamento Default?</th>
								<th>Executar todos os dias?</th>
								<!--th>Ações&nbsp;<a href="<c:url value="/newScheduler"/>"
									title="Novo Scheduler"><i class="icon-plus-sign"> </i></a></th-->
							</tr>
						</thead>
						<tbody>
							<c:forEach var="scheduler" items="${scheduler}">
								<tr>
									<td>${scheduler.schedulerId}</td>
									<td>${scheduler.schedulerName}</td>
									<td>${scheduler.enabled}</td>
									<td>${scheduler.defaultScheduler}</td>
									<td>${scheduler.everyday}</td>
									<td><a
										href="<c:url value='/findForUpdateScheduler/${scheduler.schedulerId}' />"
										title="Editar Scheduler"><i class="icon-edit"> </i></a> &nbsp;
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- Begin ModalDelete -->
	<form method="post" action="<c:url value='/deleteSchedulerByID'/>"
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
</body>
</html>