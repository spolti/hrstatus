<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script src="${pageContext.request.contextPath}/hrstatus-js/common/common-functions.js"></script>
<script type="text/javascript">

    window.onload = function () {
        var url = '${pageContext.request.contextPath}/rest/utils/resource/suported-os';
        var option = '';
        $.ajax({
            url: url,
            type: "GET",
            dataType: "json",
            success: function (supportedOs) {
                $.each(supportedOs, function (i) {
                    $('#type').append('<option value="' + supportedOs[i] + '">' + supportedOs[i] + '</option>');
                })
                $('#type').selectpicker('refresh');
            }
        });
    }

    $(document).ready(function () {
        //initialize the switch button
        initializeSwitchButton();

        $("button#submit").click(function (e) {
            e.preventDefault();
            if ($("form")[0].checkValidity()) {
                var array = jQuery('#new-os-form').serializeArray();
                var json = {};

                jQuery.each(array, function () {
                    json[this.name] = this.value || '';
                });
                json.verify = $('#verify').bootstrapSwitch('state');
                $.ajax({
                    type: "POST",
                    contentType: 'application/json',
                    url: '${pageContext.request.contextPath}/rest/resource/operating-system/new',
                    data: JSON.stringify(json),
                    dataType: 'json',
                    success: function (response) {
                        console.log('success ' + response.responseMessage);
                        $('#new-os-modal-body > h1').text(response.responseMessage);
                    },
                    error: function (xhr, textStatus, err) {
                        var response = JSON.parse(xhr.responseText);
                        console.log("response " + response.failedSubject);
                        $('#new-os-modal-body > h1').text("Falha ao criar o servidor " + response.failedSubject);
                        $('#new-os-modal-body > p').text("Mensagem de erro: " + response.responseErrorMessage);
                    }
                });
                $('#new-os-modal').modal('show');
            } else {
                submitform();
            }
        });
    });
</script>
<div class="modal fade" id="new-os-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">Registro de Sistema Operacional</h4>
            </div>
            <div id="modal-body" class="modal-body">
                <div class="form-group">
                    <div id="new-os-modal-body" class="modal-body">
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
                <li>Novo Sistema Operacional</li>
            </ol>
            <h1>Cadastrar Sistema Operacional</h1>
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
                    <label class="col-md-2 control-label" for="password">Senha</label>
                    <div class="col-md-6">
                        <input name="password" type="password" id="password" class="form-control" required
                               data-errormessage-value-missing="Campo Obrigatório">
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
        <%@ include file="/home/right-side-menu.jsp" %>
    </div>
</div>
<!-- /container -->
</body>
</html>