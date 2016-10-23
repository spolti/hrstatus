<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script type="text/javascript">
    window.onload = function () {
        document.getElementById("password").onchange = validatePassword;
        document.getElementById("verifyPassword").onchange = validatePassword;
    }
    function validatePassword() {
        var pass2 = document.getElementById("verifyPassword").value;
        var pass1 = document.getElementById("password").value;
        if (pass1 != pass2)
            document.getElementById("verifyPassword").setCustomValidity("As senhas digitadas não são iguais");
        else
            document.getElementById("verifyPassword").setCustomValidity('');
    }

    $(document).ready(function () {
        $("button#submit").click(function (e) {
            e.preventDefault();
            if($("form")[0].checkValidity()) {
                var array = jQuery('#update-user-form').serializeArray();
                var json = {};

                var rolesArray = $('#roles option:selected');
                var rolesString = '';
                $(rolesArray).each(function (index, role) {
                    if (index == rolesArray.length - 1) {
                        rolesString += $(this).val()
                    } else {
                        rolesString += $(this).val() + ','
                    }
                });

                jQuery.each(array, function () {
                    if (this.name == 'enable') {
                        json[this.name] = $(".enabled:checked").val();
                    } else if (this.name == 'roles') {
                        json[this.name] = rolesString;
                    } else {
                        json[this.name] = this.value || '';
                    }
                });
                var protocol = window.location.protocol;
                var host = window.location.host;
                console.log(json);
                $.ajax({
                    type: "POST",
                    contentType: 'application/json',
                    url: '${pageContext.request.contextPath}/rest/user/admin/update',
                    data: JSON.stringify(json),
                    dataType: 'json',
                    success: function (response) {
                        console.log('success ' + response.responseMessage);
                        $('#update-modalUser > h1').text(response.responseMessage);
                    },
                    error: function (xhr,textStatus,err) {
                        //var response = JSON.parse(xhr.responseText);
                        alert(xhr.responseText)
                        console.log(xhr.responseText);
                        $('#update-modalUser > h1').text("Falha ao atualizar usuário " + response.failedUser);
                        $('#update-modalUser > p').text("Mensagem de erro: " + response.responseErrorMessage);
                    }
                });
                $('#update-user-modal').modal('show');
            }
        });
    })

</script>
<div class="modal fade" id="update-user-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <h4 class="modal-title" id="myModalLabel">Atualização de Usuário</h4>
            </div>
            <div id="modal-body" class="modal-body">
                <div class="form-group">
                    <div id="update-modalUser" class="modal-body">
                        <h1 align="center"></h1>
                        <p align="center"></p>
                    </div>
                </div>
                <div class="modal-footer">
                    <a href="${pageContext.request.contextPath}/rest/user/admin/list/form"> <button type="button" class="btn btn-primary">Prosseguir</button> </a>
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
                <li><a href="${pageContext.request.contextPath}/rest/user/admin/list/form">
                    Gerenciar Usuários</a></li>
                <li>Editar Usuário</li>
            </ol>
            <h1>Editar Usuário</h1>
            <form method="POST" id="update-user-form" class="form-horizontal" action="#">
                <div class="form-group">
                    <label class="col-md-2 control-label" for="name">Nome</label>
                    <div class="col-md-6">
                        <input name="nome" type="text" id="name"
                               value="${user.nome}"
                               class="form-control" required readonly
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="username">Nome de Usuário</label>
                    <div class="col-md-6">
                        <input name="username" type="text" id="username"
                               value="${user.username}"
                               class="form-control" required readonly
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="password">Senha</label>
                    <div class="col-md-6">
                        <input name="password" type="password" id="password"
                               value="${user.password}"
                               class="form-control" required
                               data-errormessage-value-missing="Campo Obrigatório"
                               data-errormessage="Senha não atinge os requisitos necessários: mínimo 8 caracteres sendo no mínimo 1 minúsculo, 1 maiúsculo e um caracter especial."
                               pattern="(?=^.{6,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="verifyPassword">Repita Senha</label>
                    <div class="col-md-6">
                        <input name="verifyPassword" type="password" id="verifyPassword"
                               value="${user.password}"
                               class="form-control" required
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="mail">E-mail</label>
                    <div class="col-md-6">
                        <input name="mail" type="email" id="mail"
                               value="${user.mail}"
                               class="form-control" required
                               data-errormessage-type-mismatch="Email inválido."
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">Ativo</label>
                    <div class="col-md-6">
                        <div class="radio">
                            <label>
                                <input name="enabled" type="radio" name="optionsRadios" id="optionsRadios1" value="true"
                                <c:if test="${user.enabled}"> checked</c:if>>
                                Sim
                            </label>
                        </div>
                        <div class="radio">
                            <label>
                                <input name="enabled" type="radio" name="optionsRadios" id="optionsRadios2"
                                       value="false" <c:if test="${not user.enabled}"> checked</c:if>>
                                Não
                            </label>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="roles">Roles</label>
                    <div class="col-md-10">
                        <select name="roles" class="selectpicker" multiple data-selected-text-format="count>3"
                                id="roles"
                                required>
                            <c:forEach items="${user.roles}" var="role" varStatus="stat">
                                <c:set var="myVar" value="${stat.first ? '' : myVar} ${role}" />
                            </c:forEach>
                            <option <c:if test="${myVar.contains('ROLE_ADMIN')}" > selected </c:if> value="ROLE_ADMIN">Administrador</option>
                            <option <c:if test="${myVar.contains('ROLE_USER')}" > selected </c:if>value="ROLE_USER">Usuário</option>
                            <option <c:if test="${myVar.contains('ROLE_REST')}" > selected </c:if>value="ROLE_REST">Permissão para Requisições Rest</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-10 col-md-offset-2">
                        <button id="submit" type="submit" class="btn btn-primary">Atualizar</button>
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
                    <div id="collapseOne" class="panel-collapse collapse in">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li class="active"><a
                                        href="${pageContext.request.contextPath}/rest/user/admin/list/form">
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
                                <li><a href="${pageContext.request.contextPath}/rest/resource/operating-system/load">Gerenciar Servidores</a></li>
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
                                <li><a href="${pageContext.request.contextPath}/rest/resource/database/load">Gerenciar Banco de Dados</a></li>
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
                                <li><a href="${pageContext.request.contextPath}/rest/setup/load">Editar Configuração</a></li>
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