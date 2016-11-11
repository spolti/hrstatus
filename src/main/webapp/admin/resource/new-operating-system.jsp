<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script type="text/javascript">

    window.onload = function () {
        var protocol = window.location.protocol;
        var host = window.location.host;
        var url = protocol + '//' + host + '${pageContext.request.contextPath}/rest/utils/resource/suported-os';
        var option = '';
        $.ajax({
            url: url,
            type: "GET",
            dataType: "json",
            success: function (resp) {
                $.each(resp, function (i) {
                    $('#type').append('<option value="' + resp[i] + '">' + resp[i] + '</option>');
                })
                $('#type').selectpicker('refresh');
            }
        });
    }

    function setPort() {
        if (document.getElementById('type').value == 'WINDOWS') {
            document.getElementById('port').setAttribute('value', '23');
            document.getElementById('logDir').style.visibility = "hidden";
            document.getElementById('suCommand').style.visibility = "hidden";
        } else {
            document.getElementById('port').setAttribute('value', '22');
            document.getElementById('logDir').style.visibility = "visible";
            document.getElementById('suCommand').style.visibility = "visible";
        }
    }

    $(document).ready(function(){
        $("button#submit").click(function(){

            var array = jQuery('#new-os-form').serializeArray();
            var json = {};

            jQuery.each(array, function() {
                json[this.name] = this.value || '';
            });
            console.log(json);
            $.ajax({
                type: "POST",
                contentType: 'application/json',
                url: '${pageContext.request.contextPath}/rest/resource/operating-system/new',
                data: JSON.stringify(json),
                dataType: 'json'

            });
        });
    });
</script>
<c:if test="${error == 'true'}">
    <div class="toast-pf toast-pf-max-width toast-pf-top-right alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-error-circle-o"></span>
        Falha ao criar usuário, verificar logs, Mensagem de erro: ${message}
    </div>
</c:if>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-md-10 col-sm-push-3 col-md-push-2">
            <ol class="breadcrumb">
                <li><a href="/hs/home/home.jsp">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/rest/resource/operating-system/load">
                    Gerenciar Sistemas Operacionais</a></li>
                <li>Novo Sistema Operacional</li>
            </ol>
            <h1>Cadastrar Sistema Operacional</h1>
            <form id="new-os-form"class="form-horizontal"
                 >
                <input type="hidden">
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
                               data-errormessage-type-mismatch="O diretório digitado não é válido"
                               data-errormessage-value-missing="Campo Obrigatório"
                               pattern="^\/.+">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="suCommand">Comando Sudo</label>
                    <div class="col-md-6">
                        <input name="suCommand" type="text" id="suCommand" class="form-control"
                               data-errormessage-type-mismatch="O comando digitado não é válido"
                               data-errormessage-value-missing="Campo Obrigatório"
                               pattern="^sudo.+">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">Ativar verificação este Sistema Operacional</label>
                    <div class="col-md-6">
                        <div class="radio">
                            <label>
                                <input name="verify" type="radio" name="optionsRadios" id="optionsRadios1" value="true">
                                Sim
                            </label>
                        </div>
                        <div class="radio">
                            <label>
                                <input name="verify" type="radio" name="optionsRadios" id="optionsRadios2" value="false"
                                       checked>
                                Não
                            </label>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-10 col-md-offset-2">
                        <button type="submit" class="btn btn-primary" id="submit">Save</button>
                        <button type="reset" class="btn btn-default">Cancel</button>
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
                                        href="${pageContext.request.contextPath}/rest/resource/operating-system/load">Gerenciar
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
                                <li><a href="${pageContext.request.contextPath}/rest/resource/database/load">Gerenciar
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
                                <li><a href="${pageContext.request.contextPath}/rest/setup/load">Editar Configuração</a>
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