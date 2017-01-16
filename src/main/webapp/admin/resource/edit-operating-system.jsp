<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script src="${pageContext.request.contextPath}/hrstatus-js/common/common-functions.js"></script>
<script type="text/javascript">

    var os2update = '';

    $(document).ready(function () {
        //initialize the switch button
        initializeSwitchButton();

        var url = '${pageContext.request.contextPath}/rest/utils/resource/suported-os';
        var option = '';
        $.ajax({
            url: url,
            type: "GET",
            dataType: "json",
            success: function (suportedOs) {
                $.each(suportedOs, function (i) {
                    $('#type').append('<option value="' + suportedOs[i] + '">' + suportedOs[i] + '</option>');
                })
            }
        });

        $.ajax({
            type: "GET",
            contentType: 'application/json',
            url: '${pageContext.request.contextPath}/rest/resource/operating-system/search/' + getParameterByName('os'),
            dataType: 'json',
            success: function (os) {
                os2update = os;
                $('#hostname').val(os.hostname);
                $('#address').val(os.address);

                $('#port').val(os.port);
                $('#username').val(os.username);

                if (os.type == 'WINDOWS') {
                    $("#type").val('WINDOWS');
                    document.getElementById('logDir').style.visibility = "hidden";
                    document.getElementById('suCommand').style.visibility = "hidden";
                } else {
                    $("#type").val('UNIX').change();
                    $('#logDir').val(os.logDir);
                    $('#suCommand').val(os.suCommand);
                }
                $('#type').selectpicker('refresh');

                $('#verify').bootstrapSwitch('state', os.toVerify);

            },
            error: function (xhr, textStatus, err) {
                console.log('Failed to retrieve information from server');
            }
        });

        $("button#submit").click(function (e) {
            e.preventDefault();
            if ($("form")[0].checkValidity()) {
                var array = jQuery('#new-os-form').serializeArray();
                var json = {};

                jQuery.each(array, function () {
                    if (this.name == 'verify') {
                        json[this.name] = $("input[name=verify]:checked").val();
                    } else {
                        json[this.name] = this.value || '';
                    }
                });

                json.password = $('#password-1').val() == '' || null ? os2update.password : $('#password-1').val();

                if (json.type == 'WINDOWS') {
                    json.logDir = '';
                    json.suCommand = '';
                }
                json.verify = $('#verify').bootstrapSwitch('state');
                var mergedJsonObject = $.extend(os2update, json);
                $.ajax({
                    type: "POST",
                    contentType: 'application/json',
                    url: '${pageContext.request.contextPath}/rest/resource/operating-system/update',
                    data: JSON.stringify(mergedJsonObject),
                    dataType: 'json',
                    success: function (response) {
                        console.log('success ' + response.responseMessage);
                        $('#update-modalOs > h1').text(response.responseMessage);
                    },
                    error: function (xhr, textStatus, err) {
                        alert(xhr.responseText)
                        console.log(xhr.responseText);
                        $('#update-modalOs > h1').text("Falha ao atualizar Sistema Operacional " + response.failedSubject);
                        $('#update-modalOs > p').text("Mensagem de erro: " + response.responseErrorMessage);
                    }
                });
                $('#update-os-modal').modal('show');
            } else {
                submitform();
            }
        });
    });
</script>
<div class="modal fade" id="update-os-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">Atualização de Sistema Operacional</h4>
            </div>
            <div id="modal-body" class="modal-body">
                <div class="form-group">
                    <div id="update-modalOs" class="modal-body">
                        <h1 align="center"></h1>
                        <p align="center"></p>
                    </div>
                </div>
                <div class="modal-footer">
                    <a href="${pageContext.request.contextPath}/admin/resource/operating-system.jsp">
                        <button type="button" class="btn btn-primary">Prosseguir</button>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-md-10 col-sm-push-3 col-md-push-2">
            <ol class="breadcrumb">
                <li><a href="/hs/home/home.jsp">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/resource/operating-system.jsp">
                    Gerenciar Sistemas Operacionais</a></li>
                <li>Editar Sistema Operacional</li>
            </ol>
            <h1>Editar Sistema Operacional</h1>
            <form id="new-os-form" class="form-horizontal">
                <input id="submit_handle" type="submit" style="display: none"/>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="hostname">Hostname</label>
                    <div class="col-md-6">
                        <input name="hostname" type="text" id="hostname" class="form-control" required
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="address">Endereço de IP</label>
                    <div class="col-md-6">
                        <input name="address" type="text" id="address" class="form-control" required
                               data-errormessage-value-missing="Campo Obrigatório"
                               data-errormessage="O endereço de Ip digitado não é válido"
                               pattern="^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="type">Tipo</label>
                    <div class="col-md-10">
                        <select name="type" class="selectpicker" id="type"
                                onchange="setPort();"
                                required>
                            <option>Escolha uma opção</option>
                            <!-- Auto populated through ajax request -->
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-md-2 control-label" for="port">Porta</label>
                    <div class="col-md-6">
                        <input name="port" type="number" id="port" class="form-control" required
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="username">Usuário</label>
                    <div class="col-md-6">
                        <input name="username" type="text" id="username" class="form-control" required
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="password-1">Senha</label>
                    <div class="col-md-6">
                        <input name="password-1" type="password" id="password-1" class="form-control">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="logDir">Diretório de logs</label>
                    <div class="col-md-6">
                        <input name="logDir" type="text" id="logDir" class="form-control"
                               data-errormessage="O diretório digitado não é válido"
                               data-errormessage-value-missing="Campo Obrigatório"
                               pattern="^\/.+">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="suCommand">Comando Sudo</label>
                    <div class="col-md-6">
                        <input name="suCommand" type="text" id="suCommand" class="form-control"
                               data-errormessage="O comando digitado não é válido, o comando deve iniciar com sudo"
                               data-errormessage-value-missing="Campo Obrigatório"
                               pattern="^sudo.+">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">Ativar verificação deste Sistema Operacional?</label>
                    <div class="col-md-6">
                        <div class="radio">
                            <input name="verify" class="bootstrap-switch" id="verify" type="checkbox">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-10 col-md-offset-2">
                        <button type="submit" class="btn btn-primary" id="submit">Salvar</button>
                        <button type="reset" class="btn btn-default">Cancelar</button>
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
                    <div id="collapseTwo" class="panel-collapse collapse in">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li class="active"><a
                                        href="${pageContext.request.contextPath}/admin/resource/operating-system.jsp">Gerenciar
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
                    <div id="collapseThree" class="panel-collapse collapse">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li><a href="${pageContext.request.contextPath}/admin/setup.jsp">Editar Configuração</a>
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