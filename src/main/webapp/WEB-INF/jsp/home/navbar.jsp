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
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/filter.js"></script>

<script
	src="${pageContext.request.contextPath}/js/RGraph.common.core.js"></script>
<script
	src="${pageContext.request.contextPath}/js/RGraph.common.tooltips.js"></script>
<script
	src="${pageContext.request.contextPath}/js/RGraph.common.dynamic.js"></script>
<script
	src="${pageContext.request.contextPath}/js/RGraph.common.effects.js"></script>
<script src="${pageContext.request.contextPath}/js/RGraph.pie.js"></script>
<script src="${pageContext.request.contextPath}/js/RGraph.bar.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		useFilter('search', 'resultTable');
	});
</script>

<script language="JavaScript">
	function setParameter(id, hostname) {
		$('#delete-modal > p').text(id + " - " + hostname);
		$('#id_server').val(id);
		$('#ModalDeleteServer').modal('show');
	}
</script>

<script language="JavaScript">
	function setParameterDataBase(id, hostname) {
		$('#delete-modal > p').text(id + " - " + hostname);
		$('#id_dataBase').val(id);
		$('#ModalDeleteDataBase').modal('show');
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

		}
		window.setInterval("Tempo()", 1000);
	</script>

	<script type="text/javascript">
		$(document).ready(function() {
			$('.find_1 input:checkbox').change(function() {
				var ids = $(".find_1 input:checkbox:checked").map(function() {
					return this.value;
				}).get().join(',');
				var url = "/hrstatus/updateTimeSelectedClients/";

				$("#dynamicURL a").attr('href', url + ids);
			}).change();
		});
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
							<i class="icon-list"> </i> Menu <span class="caret"></span>
						</a>
						<ul class="dropdown-menu">
							<a href="<c:url value="/configServer"/>" data-toggle="modal"><i
								class="icon-wrench"> </i> Sistema</a>
							<a href="<c:url value="/listLocks"/>" data-toggle="modal"><i
								class="icon-lock"> </i> Lock de Recursos</a>
							<a href="<c:url value="/configClients"/>" data-toggle="modal"><i
								class="icon-hdd"> </i> Servidores</a>
							<a href="<c:url value="/configDataBases"/>" data-toggle="modal"><i
								class="icon-book"> </i> Banco de Dados </a>
							<a href="<c:url value="/configUser"/>" data-toggle="modal"><i
								class="icon-user"> </i> Usuários</a>
							<li class="dropdown-submenu"><a tabindex="-1" href="#"><i
									class="icon-folder-open"> </i> Relatórios</a>
								<ul class="dropdown-menu">
									<li class="dropdown-submenu"><a tabindex="-1" href="#">Servidores</a>
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
													<li><a
														href="<c:url value="/reports/reportSOWindows"/>"
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
													<li><a
														href="<c:url value="/reports/reportServersOK"/>"
														title="Download Relatório Servidores OK no formato PDF">
															<i class="icon-download-alt"></i> Servidores OK
													</a></li>
													<li><a
														href="<c:url value="/reports/reportServersNOK"/>"
														title="Download Relatório Servidores não OK no formato PDF">
															<i class="icon-download-alt"></i> Servidores não OK
													</a></li>
												</ul></li>
										</ul></li>
									<li class="dropdown-submenu"><a tabindex="-1" href="#">Banco
											de Dados</a>
										<ul class="dropdown-menu">
											<li><a href="<c:url value="/reports/reportDataBaseFull"/>"
												title="Download Relatório Servidores não OK no formato PDF">
													<i class="icon-download-alt"></i> Todos os Banco de Dados
											</a></li> 
										</ul></li>
								</ul></li>
							<a href="<c:url value="/selectServer"/>" data-toggle="modal"><i
								class="icon-file"> </i> Verificar logs</a>

							<li class="dropdown-submenu"><a tabindex="-1" href="#"><i
									class="icon-tasks"> </i> Gráficos </a>
								<ul class="dropdown-menu">
									<li><a
										href="<c:url value="/charts/servers/consolidated"/>"
										title="Plotar os gráficos de status das checagens de Servidores">
											<i class="icon-arrow-right"></i> Servidores
									</a></li>
									<li><a
										href="<c:url value="/charts/database/consolidated"/>"
										title="Plotar os gráficos de status das checagens de Banco de Dados">
											<i class="icon-arrow-right"></i> Banco de Dados
									</a></li>

								</ul></li>

							<a href="#ModalAbout" data-toggle="modal"><i
								class="icon-info-sign"></i> Sobre </a>


						</ul>
				</li>
			</ul>

			<ul class="nav pull-right">
				<li class="divider-vertical" />
				<input type="text" class="navbar-search" id="search"
					placeholder="Procurar" />

				<li>
					<div class="btn-group">
						<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
							<i class="icon-user"> </i> ${loggedUser} <span class="caret"></span>
						</a>

						<ul class="dropdown-menu">
							<li><a
								href="<c:url value="/atualizarCadastro/${loggedUser}"/>">Atualizar
									Cadastro</a></li>
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
	<div id="ModalAbout" class="modal hide fade" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">×</button>
			<h3 id="myModalLabel">Sobre</h3>
		</div>
		<div class="modal-body">
			<p>
				<a href="http://www.hrstatus.com.br/hrstatus/home.html"
					target="_blank">Hr Status</a><br>Versão: 3.1<br> <br>
				<br> <a href="http://www.hrstatus.com.br/hrstatus/home.html"
					target="_blank"> <img
					src="${pageContext.request.contextPath}/img/hrimg.JPG"></img></a><br>
				<br> <br> Todos os Direitos Reservados.<br> Para
				Suporte: <a
					href="mailto:spolti@hrstatus.com.br?Subject=Suporte%20Hrstatus">Contato</a>
				<br> Para reportar bugs: <a
					href="https://github.com/spolti/hrstatus/issues/new"
					target="_blank"> Registrar Issue </a>
			</p>
		</div>
		<div class="modal-footer">
			<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
		</div>
	</div>