<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script src="${pageContext.request.contextPath}/hrstatus-js/common/common-functions.js"></script>
<script>
    var user2update = '';
    $(document).ready(function () {
        <!-- TODO migrate this to a rest endpoint -->
        var DEFAULT_ROLES = ['ROLE_ADMIN', 'ROLE_USER', 'ROLE_REST'];
        var DEFAULT_ROLES_DESCRIPTION = ['Administrador', 'Usuário', 'Permissão para Requisições Rest'];

        var selected = '';
        $.ajax({
            type: "GET",
            contentType: 'application/json',
            url: '${pageContext.request.contextPath}/rest/user/edit-nonadmin/${pageContext.request.userPrincipal.name}',
            dataType: 'json',
            success: function (user) {
                user2update = user;
                $('#name').val(user.nome);
                $('#username').val(user.username);
                $('#email').val(user.mail);
                user.enabled == true ? $('#enabled').prop("checked", true) : $('#disabled').prop("checked", true);
                <!-- populating the selectpicker roles -->
                jQuery.each(DEFAULT_ROLES, function (default_role_id) {
                    selected = ''
                    jQuery.each(user.roles, function (role_id) {
                        if (user.roles[role_id] == DEFAULT_ROLES[default_role_id]) {
                            selected = 'selected';
                        }
                    });
                    $("#bootstrapSelect").append('<option value="' + DEFAULT_ROLES[default_role_id] + '" ' + selected + '>' + DEFAULT_ROLES_DESCRIPTION[default_role_id] + '</option>');
                });
                $('#bootstrapSelect').selectpicker('refresh');
                $('#bootstrapSelect').attr('disabled', true)
            },
            error: function (xhr, textStatus, err) {
                console.log('Failed to retrieve information from server');
            }
        });

        $("button#update").click(function (e) {

            e.preventDefault();
            if ($("form")[0].checkValidity()) {

                user2update.password = $('#password').val() == '' || null ? user2update.password : $('#password').val();
                user2update.mail = $('#email').val() == '' || null ? user2update.email : $('#email').val();
                var roles = '';
                jQuery.each(user2update.roles, function (index) {
                    if (index == user2update.roles.length - 1) {
                        roles += user2update.roles[index];
                    } else {
                        roles += user2update.roles[index] + ',';
                    }
                });
                user2update.roles = roles;
                console.log(user2update.roles);
                $.ajax({
                    type: "POST",
                    contentType: 'application/json',
                    url: '${pageContext.request.contextPath}/rest/user/update-nonadmin',
                    data: JSON.stringify(user2update),
                    dataType: 'json',
                    success: function (response) {
                        console.log('success ' + response.responseMessage);
                        $('#update-modalNonAdminUser > h1').text(response.responseMessage);
                    },
                    error: function (xhr, textStatus, err) {
                        //var response = JSON.parse(xhr.responseText);
                        alert(xhr.responseText)
                        console.log(xhr.responseText);
                        $('#update-modalNonAdminUser > h1').text("Falha ao atualizar usuário " + response.failedSubject);
                        $('#update-modalNonAdminUser > p').text("Mensagem de erro: " + response.responseErrorMessage);
                    }
                });
                $('#update-non-admin-user-modal').modal('show');
            } else {
                submitform();
            }
        });
    })
</script>
<div class="modal fade" id="update-non-admin-user-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
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
                    <div id="update-modalNonAdminUser" class="modal-body">
                        <h1 align="center"></h1>
                        <p align="center"></p>
                    </div>
                </div>
                <div class="modal-footer">
                    <a href="${pageContext.request.contextPath}">
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
                <li><a href="${pageContext.request.contextPath}/rest/user/admin/list/form">
                    Gerenciar Usuários</a></li>
                <li>Editar Usuário</li>
            </ol>
            <h1>Editar Usuário</h1>
            <form method="POST" class="form-horizontal" id="update-myself" action="#">
                <input id="submit_handle" type="submit" style="display: none"/>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="name">Nome</label>
                    <div class="col-md-6">
                        <input name="nome" type="text" id="name"
                               class="form-control" required readonly
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="username">Nome de Usuário</label>
                    <div class="col-md-6">
                        <input name="username" type="text" id="username"
                               class="form-control" required readonly
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="password">Senha</label>
                    <div class="col-md-6">
                        <input name="password" type="password" id="password"
                               class="form-control"
                               data-errormessage="Senha não atinge os requisitos necessários: mínimo 8 caracteres sendo no mínimo 1 minúsculo, 1 maiúsculo e um caracter especial."
                               pattern="(?=^.{6,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="verifyPassword">Repita Senha</label>
                    <div class="col-md-6">
                        <input name="verifyPassword" type="password" id="verifyPassword"
                               class="form-control">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="email">E-mail</label>
                    <div class="col-md-6">
                        <input name="email" type="email" id="email"
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
                                <input name="enabled" type="radio" name="optionsRadios" id="enabled" value="true">
                                Sim
                            </label>
                        </div>
                        <div class="radio">
                            <label>
                                <input name="enabled" type="radio" name="optionsRadios" id="disabled" value="false">
                                Não
                            </label>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="bootstrapSelect">Roles</label>
                    <div class="col-md-10">
                        <select name="roles" class="selectpicker" multiple data-selected-text-format="count>3"
                                id="bootstrapSelect">

                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-10 col-md-offset-2">
                        <button id="update" type="submit" class="btn btn-primary">Atualizar</button>
                        <button type="reset" class="btn btn-default">Cancelar</button>
                    </div>
                </div>
            </form>
        </div><!-- /col -->
        <%@ include file="/home/left-side-menu.jsp" %>
    </div>
</div>
<!-- /container -->
</body>
</html>