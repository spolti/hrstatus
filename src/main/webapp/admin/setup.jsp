<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script language="JavaScript">

    var actualConfiguration = ''

    $(document).ready(function () {
        $('#mailJndi').selectpicker('refresh');
        //hiding alerts
        $('#emailTestSuccess').hide();
        $('#emailTestFailure').hide();

        //initialize the switch button
        initializeSwitchButton();

        $.ajax({
            url: '${pageContext.request.contextPath}/rest/setup/available-mail-sessions',
            type: "GET",
            dataType: "json",
            success: function (availableMailSessions) {
                $.each(availableMailSessions, function (i) {
                    $('#mailJndi').append('<option value="' + availableMailSessions[i] + '">' + availableMailSessions[i] + '</option>');
                })
                $('#mailJndi').selectpicker('refresh');
            }
        });

        $.ajax({
            url: '${pageContext.request.contextPath}/rest/setup/load',
            type: "GET",
            dataType: "json",
            success: function (configuration) {
                actualConfiguration = configuration;
                $('#mailJndi').val(configuration.mailJndi);
                $('#mailJndi').selectpicker('refresh');
                $('#difference').val(configuration.difference);
                $('#mailFrom').val(configuration.mailFrom);
                $('#sendNotification').bootstrapSwitch('state', configuration.sendNotification);
                $('#subject').val(configuration.subject);

                jQuery.each(configuration.destinatarios, function (index, value) {
                    $('#destinatarios').append('<option value="' + value + '">' + value + '</option>');
                });
                $('#destinatarios').selectpicker('refresh');

                $('#ntpServer').val(configuration.ntpServer);
                $('#updateNtpIsActive').bootstrapSwitch('state', configuration.updateNtpIsActive);
                $('#welcomeMessage').val(configuration.welcomeMessage);
            },
            error: function (response) {
                console.warn("Failed to retrieve information from server");
            }
        });
        $('#setupSubmitForm').click(function (e) {
            e.preventDefault();
            if ($('#configuration')[0].checkValidity()) {
                var array = jQuery('#configuration').serializeArray();
                var json = {};

                jQuery.each(array, function () {
                    json[this.name] = this.value || '';
                });
                var options = $('#destinatarios option');
                var rcptsArray = $.map(options, function (option) {
                    return option.value;
                });

                json.destinatarios = rcptsArray;
                json.sendNotification = $('#sendNotification').bootstrapSwitch('state');
                json.updateNtpIsActive = $('#updateNtpIsActive').bootstrapSwitch('state');
                var mergedJsonObject = $.extend(actualConfiguration, json);
                $.ajax({
                    type: "POST",
                    contentType: 'application/json',
                    url: '${pageContext.request.contextPath}/rest/setup/update',
                    data: JSON.stringify(mergedJsonObject),
                    dataType: 'json',
                    success: function (response) {
                        $('#config-modal-body > h1').text("Configuração atualizada com sucesso.");
                    },
                    error: function (xhr, textStatus, err) {
                        var response = JSON.parse(xhr.responseText);
                        console.log("response " + response.responseErrorMessage);
                        $('#config-modal-body > h1').text("Falha ao atualizar configuração");
                        $('#config-modal-body > p').text(response.responseErrorMessage);
                    }
                });
                $('#config-modal').modal('show');
            } else {
                submitform();
            }
        });
    });

    function send() {
        $.ajax({
            url: '${pageContext.request.contextPath}/rest/utils/mail/test?jndi=' + $('#testMailJndi').val() + '&dest=' + $('#dest').val(),
            type: "POST",
            dataType: "json",
            success: function (response) {
                console.log(response.responseMessage);
                $('#sendTestEmail').modal('hide');
                $('#emailTestSuccess').show();
            },
            error: function (response) {
                console.warn(response.responseMessage);
                $('#sendTestEmail').modal('hide');
                $('#emailTestFailure').show();
            }
        });
    }
</script>
<div class="modal fade" id="config-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <h4 class="modal-title" id="myModalLabel">Atualização de Configuração</h4>
            </div>
            <div id="modal-body" class="modal-body">
                <div class="form-group">
                    <div id="config-modal-body" class="modal-body">
                        <h1 align="center"></h1>
                        <p align="center"></p>
                    </div>
                </div>
                <div class="modal-footer">
                    <a href="${pageContext.request.contextPath}/admin/setup.jsp">
                        <button type="button" class="btn btn-primary">Prosseguir</button>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Modal send mail test -->
<div class="modal fade" id="sendTestEmail" tabindex="-1" role="dialog" aria-labelledby="sendTestEmailLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="sendTestEmailLabel">Enviar E-mail de Teste</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" action="javascript:send();">
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="testMailJndi">Mail Jndi</label>
                        <div class="col-sm-9">
                            <input type="text" id="testMailJndi" name="testMailJndi" class="form-control" readonly>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="dest">Destinatário</label>
                        <div class="col-sm-9">
                            <input type="email" id="dest" name="dest" class="form-control"
                                   placeholder="example@hrstatus.com" data-errormessage-type-mismatch="Email inválido.">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="modal-footer">
                            <button class="btn btn-primary">Enviar</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Fechar</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<!-- End of Modal send mail test -->
<div id="emailTestSuccess"
     class="toast-pf toast-pf-max-width toast-pf-top-right alert alert-success alert-dismissable">
    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
        <span class="pficon pficon-close"></span>
    </button>
    <span class="pficon pficon-ok"></span>
    Email de teste enviado com sucesso.
</div>
<div id="emailTestFailure"
     class="toast-pf toast-pf-max-width toast-pf-top-right alert alert-warning alert-dismissable">
    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
        <span class="pficon pficon-close"></span>
    </button>
    <span class="pficon pficon-ok"></span>
    Falha ao enviar email.
</div>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-md-10 col-sm-push-3 col-md-push-2">
            <ol class="breadcrumb">
                <li><a href="/hs/home/home.jsp">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/rest/setup/load">
                    Configuração</a></li>
                <li>Editar Configuração</li>
            </ol>
            <h1>Configurações</h1>
            <form id="configuration" class="form-horizontal">
                <input id="submit_handle" type="submit" style="display: none"/>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="mailJndi">Mail Jndi</label>
                    <div class="col-md-6">
                        <select name="mailJndi" class="selectpicker" id="mailJndi">
                        </select> &nbsp; - &nbsp;
                        <a href="javascript:sendEmailTest();" class="btn btn-info">Enviar Email de Teste</a>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="difference">Diferença de Tempo (segundos)</label>
                    <div class="col-md-6">
                        <input name="difference" type="number" id="difference"
                               class="form-control" required
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="mailFrom">Remetente dos E-mails</label>
                    <div class="col-md-6">
                        <input name="mailFrom" type="text" id="mailFrom"
                               class="form-control" required
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="sendNotification">Ativar Notificação Via e-mail</label>
                    <div class="col-md-6">
                        <input name="sendNotification" class="bootstrap-switch" id="sendNotification" type="checkbox">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="subject">Assunto do email</label>
                    <div class="col-md-6">
                        <input name="subject" type="text" id="subject"
                               class="form-control" required
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="addDestinatario">Adicionar Destinatário</label>
                    <div class="col-md-5">
                        <input name="addDestinatario" type="email" id="addDestinatario"
                               class="form-control"
                               data-errormessage="O endereço de email invlálido">
                    </div>
                    - &nbsp; <a href="javascript:addDest();" class="btn btn-info">Adicionar</a>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="destinatarios">Destinatários</label>
                    <div class="col-md-6">
                        <select name="destinatarios" id="destinatarios" class="selectpicker">
                        </select> &nbsp; - &nbsp;
                        <a href="javascript:removeMail();" class="btn btn-info">Remover</a>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-md-2 control-label" for="ntpServer">Servidor NTP</label>
                    <div class="col-md-6">
                        <input name="ntpServer" type="text" id="ntpServer"
                               class="form-control" required
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="updateNtpIsActive">Ativar Atualização via NTP</label>
                    <div class="col-md-6">
                        <input name="updateNtpIsActive" class="bootstrap-switch" id="updateNtpIsActive" type="checkbox">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="welcomeMessage">Mensagem de boas vindas</label>
                    <div class="col-md-6">
                        <input name="welcomeMessage" type="text" id="welcomeMessage"
                               class="form-control" required
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-10 col-md-offset-2">
                        <button class="btn btn-primary" id="setupSubmitForm">Atualizar Configurações</button>
                    </div>
                </div>
            </form>
        </div><!-- /col -->
        <div class="col-sm-3 col-md-2 col-sm-pull-9 col-md-pull-10 sidebar-pf sidebar-pf-left">
            <div class="panel-group" id="accordion">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne" class="collapsed">
                                Usuários
                            </a>
                        </h4>
                    </div>
                    <div id="collapseOne" class="panel-collapse collapse">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li><a href="${pageContext.request.contextPath}/admin/user/users.jsp">
                                    Gerenciar Usuários</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" class="collapsed">
                                Servidores
                            </a>
                        </h4>
                    </div>
                    <div id="collapseTwo" class="panel-collapse collapse">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li><a href="${pageContext.request.contextPath}/admin/resource/operating-system.jsp">Gerenciar
                                    Servidores</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseFive" class="collapsed">
                                Banco de Dados
                            </a>
                        </h4>
                    </div>
                    <div id="collapseFive" class="panel-collapse collapse">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li><a href="${pageContext.request.contextPath}/admin/resource/database.jsp">Gerenciar
                                    Banco de Dados</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseThree" class="collapsed">
                                Configuração
                            </a>
                        </h4>
                    </div>
                    <div id="collapseThree" class="panel-collapse collapse in">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li class="active">
                                    <a href="${pageContext.request.contextPath}/admin/setup.jsp">Editar Configuração</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseFour" class="collapsed">
                                Logs
                            </a>
                        </h4>
                    </div>
                    <div id="collapseFour" class="panel-collapse collapse">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li><a href="#">Extrair Logs</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseSix" class="collapsed">
                                Relatórios
                            </a>
                        </h4>
                    </div>
                    <div id="collapseSix" class="panel-collapse collapse">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li><a href="#">Sed est</a></li>
                                <li><a href="#">Curabitur</a></li>
                                <li><a href="#">Eu dignissim</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseSeven" class="collapsed">
                                Gráficos
                            </a>
                        </h4>
                    </div>
                    <div id="collapseSeven" class="panel-collapse collapse">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li><a href="#">Sed est</a></li>
                                <li><a href="#">Curabitur</a></li>
                                <li><a href="#">Eu dignissim</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseEigth" class="collapsed">
                                Agendamentos
                            </a>
                        </h4>
                    </div>
                    <div id="collapseEigth" class="panel-collapse collapse">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li><a href="#">Sed est</a></li>
                                <li><a href="#">Curabitur</a></li>
                                <li><a href="#">Eu dignissim</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseNine" class="collapsed">
                                Sobre o Hrstatus
                            </a>
                        </h4>
                    </div>
                    <div id="collapseNine" class="panel-collapse collapse">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li><a href="${pageContext.request.contextPath}/home/about">Sobre</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /row -->
</div>
<!-- /container -->
</body>
</html>