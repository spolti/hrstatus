<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script src="${pageContext.request.contextPath}/hrstatus-js/common/common-functions.js"></script>
<script type="text/javascript">

    var db2update = '';
    $(document).ready(function () {
        //initialize the switch button
        initializeSwitchButton();

        document.getElementById('db_name').style.visibility = "hidden";
        var url = '${pageContext.request.contextPath}/rest/utils/resource/suported-db';
        var option = '';
        $.ajax({
            url: url,
            type: "GET",
            dataType: "json",
            success: function (supportedDb) {
                $.each(supportedDb, function (i) {
                    $('#vendor').append('<option value="' + supportedDb[i] + '">' + supportedDb[i] + '</option>');
                })
                $('#vendor').selectpicker('refresh');
            }
        });

        $('#vendor').on('change', function () {
            setDatabaseConfig();
        });

        $.ajax({
            type: "GET",
            contentType: 'application/json',
            url: '${pageContext.request.contextPath}/rest/resource/database/search/' + getParameterByName('db'),
            dataType: 'json',
            success: function (db) {
                db2update = db;
                $('#hostname').val(db.hostname);
                $('#address').val(db.address);

                $('#port').val(db.port);
                $('#username').val(db.username);

                if (db.vendor == 'MYSQL') {
                    $("#vendor").val('MYSQL');
                } else if (db.vendor == 'ORACLE') {
                    $("#vendor").val('ORACLE');
                } else if (db.vendor == 'POSTGRESQL') {
                    $("#vendor").val('POSTGRESQL');
                } else if (db.vendor == 'DB2') {
                    $("#vendor").val('DB2');
                } else if (db.vendor == 'SQLSERVER') {
                    $("#vendor").val('SQLSERVER');
                    document.getElementById('db_name').style.visibility = "visible";
                } else if (db.vendor == 'MONGODB') {
                    $("#vendor").val('MONGODB');
                }
                $('#vendor').selectpicker('refresh');

                $('#queryDate').val(db.queryDate);
                document.getElementById('queryDate').readOnly = true;

                $('#instance').val(db.instance);

                //radio button verificação ativa/desativada
                //db.verify == true ? $('#toVerifyEnabled').prop("checked", true) : $('#toVerifyDisabled').prop("checked", true);
                $('#verify').bootstrapSwitch('state', db.verify);
            },
            error: function (xhr, textStatus, err) {
                console.log('Failed to retrieve information from server');
            }
        });

        $("button#submit").click(function (e) {
            e.preventDefault();
            if ($("form")[0].checkValidity()) {
                var array = jQuery('#new-db-form').serializeArray();
                var json = {};

                jQuery.each(array, function () {
                    if (this.name == 'verify') {
                        json[this.name] = $("input[name=verify]:checked").val();
                    } else {
                        json[this.name] = this.value || '';
                    }
                });

                json.password = $('#password').val() == '' || null ? db2update.password : $('#password').val();

                if (json.vendor != 'SQLSERVER') {
                    json.db_name = '';
                }

                var mergedJsonObject = $.extend(db2update, json);
                $.ajax({
                    type: "POST",
                    contentType: 'application/json',
                    url: '${pageContext.request.contextPath}/rest/resource/database/update',
                    data: JSON.stringify(mergedJsonObject),
                    dataType: 'json',
                    success: function (response) {
                        console.log('success ' + response.responseMessage);
                        $('#update-modalOs > h1').text(response.responseMessage);
                    },
                    error: function (xhr, textStatus, err) {
                        alert(xhr.responseText)
                        console.log(xhr.responseText);
                        $('#update-modalOs > h1').text("Falha ao atualizar Banco de Dados " + response.failedSubject);
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
                <h4 class="modal-title" id="myModalLabel">Atualização de Banco de Dados</h4>
            </div>
            <div id="modal-body" class="modal-body">
                <div class="form-group">
                    <div id="update-modalOs" class="modal-body">
                        <h1 align="center"></h1>
                        <p align="center"></p>
                    </div>
                </div>
                <div class="modal-footer">
                    <a href="${pageContext.request.contextPath}/admin/resource/database.jsp">
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
                <li><a href="${pageContext.request.contextPath}/admin/resource/database.jsp">
                    Gerenciar Banco de Dados</a></li>
                <li>Editar Banco de Dados</li>
            </ol>
            <h1>Editar Banco de Dados</h1>
            <form id="new-db-form" class="form-horizontal">
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
                    <label class="col-md-2 control-label" for="vendor">Vendor</label>
                    <div class="col-md-10">
                        <select name="vendor" class="selectpicker" id="vendor" required>
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
                    <label class="col-md-2 control-label" for="password">Senha</label>
                    <div class="col-md-6">
                        <input name="password" type="password" id="password" class="form-control">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="queryDate">Query</label>
                    <div class="col-md-6">
                        <input name="queryDate" type="text" id="queryDate" class="form-control"
                               data-errormessage="O diretório digitado não é válido"
                               data-errormessage-value-missing="Campo Obrigatório" pattern="^[select|SELECT].+">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="instance">Instância</label>
                    <div class="col-md-6">
                        <input name="instance" type="text" id="instance" class="form-control"
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="db_name">Nome do Banco de Dados</label>
                    <div class="col-md-6">
                        <input name="db_name" type="text" id="db_name" class="form-control">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">Ativar verificação deste Banco de Dados?</label>
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
                    <div id="collapseTwo" class="panel-collapse collapse">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li><a
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
                    <div id="collapseFive" class="panel-collapse collapse in">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li class="active"><a href="${pageContext.request.contextPath}/admin/resource/database.jsp">Gerenciar
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