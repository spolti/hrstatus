<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../home/navbar.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Configurar Servidor</title>

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
					<c:if test="${not empty info}">
						<div class="alert">
							<button type="button" class="close" data-dismiss="alert">×</button>
							${info}
						</div>
					</c:if>
					
					<table class="table table-striped" id="resultTable">
						<thead>
							<tr>
								<th>Parâmetro</th>
								<th>Valor</th>
								<th>Ação</th>
							</tr>
						</thead>
						<tbody>

							<tr class="info">
								<form method="GET" action="<c:url value='/sendMailtest'/>">
									<td>Enviar E-Mail de Teste</td>
									<td>Enviar para: <input type="text" id="rcpt" name="rcpt"></td>
									<td><button type="submit" class="btn btn-primary"
											value="Atualizar">Enviar</button></td>
								</form>
							</tr>
							<tr class="info">
								<td>Diferença de Tempo (segundos)</td>
								<td>${difference}</td>
								<td><a
									href="javascript:setParameterForUpdate('${difference}','Editar Parametro Diferença de Tempo (segundos)','difference');"
									title="Atualizar campo"> <i class="icon-edit"> </i></a></td>
							</tr>
							<tr class="info">
								<td>Remetente do E-mail</td>
								<td>${mailFrom}</td>
								<td><a
									href="javascript:setParameterForUpdate('${mailFrom}','Editar Parametro Remetente do E-mail','mailFrom');"
									title="Atualizar campo"> <i class="icon-edit"> </i></a></td>
							</tr>

							<tr class="info">
								<td>Ativar Notificação Via e-mail</td>
								<td>${sendNotification}</td>
								<td><a
									href="javascript:setParameterForUpdate('${sendNotification}','Editar Parametro Ativação de Notificação','sendNotification');"
									title="Atualizar campo"> <i class="icon-edit"> </i></a></td>
							</tr>

							<tr class="info">
								<td>Assunto</td>
								<td>${subject}</td>
								<td><a
									href="javascript:setParameterForUpdate('${subject}','Editar Parametro Assunto','subject');"
									title="Atualizar campo"> <i class="icon-edit"> </i></a></td>
							</tr>

							<tr class="info">
								<td>Destinatários</td>
								<td>${dests}</td>
								<td><a
									href="javascript:setParameterForUpdate('${dests}','Editar Parametro Destinatários','dests');"
									title="Atualizar campo"> <i class="icon-edit"> </i></a></td>
							</tr>

							<tr class="info">
								<td>Mail Session</td>
								<td>${jndiMail}</td>
								<td><a
									href="javascript:setMailSessionParameterForUpdate('${jndiMail}','Editar Parametro Mail Session','jndiMail');"
									title="Atualizar campo"> <i class="icon-edit"> </i></a></td>
							</tr>

							<tr class="info">
								<td>Servidor NTP</td>
								<td>${ntpServer}</td>
								<td><a
									href="javascript:setParameterForUpdate('${ntpServer}','Editar Parametro Servidor NTP','ntpServer');"
									title="Atualizar campo"> <i class="icon-edit"> </i></a></td>
							</tr>

							<tr class="info">
								<td>Ativar Atualização via NTP:</td>
								<td>${updateNtpIsActive}</td>
								<td><a
									href="javascript:setParameterForUpdate('${updateNtpIsActive}','Editar Parametro Ativar Atualização via NTP','updateNtpIsActive');"
									title="Atualizar campo"> <i class="icon-edit"> </i></a></td>
							</tr>
							<tr class="info">
								<td>
									<form action="<c:url value="/uploud/logo/imagem"/>"
										method="POST" enctype="multipart/form-data">
										<fieldset>
											<legend>
												<h4>
													Enviar Logo Tela de Login
													</h4>
											</legend>
											<input type="file" name="imagem" />

											<button type="submit">Enviar</button>
										</fieldset>
									</form>
								</td>
								<td><img src="<c:url value="/show/logo/imagem/settings"/>" />
								</td>
								<td><a href="<c:url value="/delete/logo/imagem"/>"
									title="Remover Imagem"> <i class="icon-remove-circle">
									</i>
								</a></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>


	<form method="POST" action="<c:url value='/updateConfig'/>"
		onload="javascript:setParameterForUpdate()">
		<div class="modal" id="ConfigurationTab" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true"
			style="display: none">
			<div id="cab" class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h3 id="myModalLabel"></h3>
			</div>
			<div id="edit-modal" class="modal-body">
				<input type="hidden" id="campo" name="campo">
				<p align="center"></p>
				<div align="center">
					Novo valor: <input type="text" name="new_value">
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
				<button type="submit" class="btn btn-primary" value="Atualizar">Atualizar</button>
			</div>
		</div>
	</form>
	
	<form method="POST" action="<c:url value='/updateConfig'/>"
		onload="javascript:setMailSessionParameterForUpdate()">
		<div class="modal" id="MailSessionConfigurationTab" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true"
			style="display: none">
			<div id="cab" class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h3 id="myModalLabel"></h3>
			</div>
			<div id="edit-modal" class="modal-body">
				<input type="hidden" id="campo" name="campo" value="jndiMail">
				<p align="center"></p>
				<div align="center">
					Novo valor: <select name="new_value" id="mailSessions">
										<c:forEach items="${mailSessions}" var="mailSessions">
											<option value="<c:out value="${mailSessions}" />">${mailSessions}</option>
										</c:forEach>
								</select>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
				<button type="submit" class="btn btn-primary" value="Atualizar">Atualizar</button>
			</div>
		</div>
	</form>
	
</body>
</html>