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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${title}</title>

<link href="${pageContext.request.contextPath}/css/bootstrap.css"
	rel="stylesheet">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link
	href="${pageContext.request.contextPath}/css/bootstrap-responsive.css"
	rel="stylesheet">

<script language="JavaScript">
	function setParameter(id, hostname) {
		$('#delete-modal > p').text(id + " - " + hostname);
		$('#id_server').val(id);
		$('#ModalDeleteServer').modal('show');
	}
</script>

<script language="JavaScript">
	function setParameterUser(username, nome) {
		$('#delete-modalUser > p').text(username + " - " + nome);
		$('#username').val(username);
		$('#ModalDeleteUser').modal('show');
	}
</script>

<script language="JavaScript">
	function setParameterForUpdate(valor1, parametro, campo) {
		$('#cab > h3').text(parametro);
		$('#edit-modal > p').text("Valor atual: " + valor1);
		$('#edit-modal > p').val(
				"Novo valor: <input type='text' name='new_value' />");
		$('#campo').val(campo);
		$('#ConfigurationTab').modal('show');
	}
</script>

</head>
<body onload="Tempo()">
	<script src="http://code.jquery.com/jquery-latest.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		function Tempo() {
			horario = new Date();
			hora = horario.getHours();
			minuto = horario.getMinutes();
			segundo = horario.getSeconds();
			if (hora < 10) {
				hora = "0" + hora;
			}
			if (minuto < 10) {
				minuto = "0" + minuto;
			}
			if (segundo < 10) {
				segundo = "0" + segundo;
			}
			document.getElementById("time").innerHTML = hora + ":" + minuto;
			+":" + segundo; // hora no documento
			// barra de título
			// barra de status
		}
		window.setInterval("Tempo()", 1000);
	</script>
	<!-- Begin NavBar -->
	<div class="navbar">
		<div class="navbar-inner">
			<a class="brand" href="<c:url value="/home"/>"> <i
				class="icon-home"> </i> HR Status
			</a>

			<ul class="nav">

				<li class="divider-vertical" />

				<li>
					<div class="btn-group">
						<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
							<i class="icon-wrench"> </i> Configurações <span class="caret"></span>
						</a>
						<ul class="dropdown-menu">
							<a href="<c:url value="/configServer"/>" data-toggle="modal">Sistema</a>
							<a href="<c:url value="/configClients"/>" data-toggle="modal">Servidores</a>
							<a href="<c:url value="/configUser"/>" data-toggle="modal">Usuários</a>
							<li class="dropdown-submenu"><a tabindex="-1" href="#">Relatórios</a>
								<ul class="dropdown-menu">
									<li><a href="<c:url value="/reports/reportFull"/>"
										title="Download Relatório de todos os Servidores Cadastrados no formato PDF">
											<i class="icon-download-alt"></i> Todos Servidores
											Cadastrados
									</a></li>
									<li class="dropdown-submenu"><a tabindex="-1" href="#">Subdivididos
											por SO</a>
										<ul class="dropdown-menu">
											<li><a href="<c:url value="/reports/reportSOLinux"/>"
												title="Download Relatório Servidores Linux no formato PDF">
													<i class="icon-download-alt"></i> Linux
											</a></li>
											<li><a href="<c:url value="/reports/reportSOWindows"/>"
												title="Download Relatório Servidores Windows no formato PDF"><i
													class="icon-download-alt"></i> Windows</a></li>
											<li><a href="<c:url value="/reports/reportSOUnix"/>"
												title="Download Relatório Servidores Unix no formato PDF">
													<i class="icon-download-alt"></i> Unix
											</a></li>
											<li><a href="<c:url value="/reports/reportSOOthers"/>"
												title="Download Relatório Servidores Otros no formato PDF">
													<i class="icon-download-alt"></i> Outros
											</a></li>
										</ul></li>
									<li class="dropdown-submenu"><a tabindex="-1" href="#">Por
											Status</a>
										<ul class="dropdown-menu">
											<li><a href="<c:url value="/reports/reportServersOK"/>"
												title="Download Relatório Servidores OK no formato PDF">
													<i class="icon-download-alt"></i> Servidores OK
											</a></li>
											<li><a href="<c:url value="/reports/reportServersNOK"/>"
												title="Download Relatório Servidores não OK no formato PDF">
													<i class="icon-download-alt"></i> Servidores não OK
											</a></li>
										</ul></li>
								</ul></li>
						</ul>
				</li>

			</ul>
			<ul class="nav pull-right">
				<li class="divider-vertical" />
				<li>
					<div class="btn-group">
						<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
							<i class="icon-user"> </i> ${loggedUser} <span class="caret"></span>
						</a>

						<ul class="dropdown-menu">
							<li><a href="<c:url value="/atualizarCadastro/${loggedUser}"/>">Atualizar Cadastro</a></li>
							<li><a href="<c:url value="/j_spring_security_logout"/>">Logout</a></li>
						</ul>
					</div>
				</li>
				<li class="divider-vertical" />
				<li>
				<li>Seu Horário<b><div align="center" id="time">time</div></b>
			</ul>
		</div>
	</div>