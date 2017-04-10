<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script src="${pageContext.request.contextPath}/hrstatus-js/common/common-functions.js"></script>
<script type="text/javascript">
    window.onload = function () {
        document.getElementById("password").onchange = validatePassword;
        document.getElementById("verifyPassword").onchange = validatePassword;
    }

    $(document).ready(function () {

        //initialize the switch button
        initializeSwitchButton();

        $("button#submit").click(function (e) {
            e.preventDefault();
            if ($("form")[0].checkValidity()) {
                var array = jQuery('#new-user-form').serializeArray();
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
                json.enabled = $('#enabled').bootstrapSwitch('state');
                console.log(json);
                $.ajax({
                    type: "POST",
                    contentType: 'application/json',
                    url: '${pageContext.request.contextPath}/rest/user/admin/new',
                    data: JSON.stringify(json),
                    dataType: 'json',
                    success: function (response) {
                        console.log('success ' + response.responseMessage);
                        $('#create-modalUser > h1').text("Usuário " + response.createdUser + " criado com sucesso.");
                    },
                    error: function (xhr, textStatus, err) {
                        var response = JSON.parse(xhr.responseText);
                        console.log(response);
                        $('#create-modalUser > h1').text("Falha ao criar usuário " + response.failedSubject);
                        $('#create-modalUser > p').text("Mensagem de erro: " + response.responseErrorMessage);
                    }
                });
                $('#create-user-modal').modal('show');
            } else {
                submitform();
            }
        });
    })
</script>
<div class="modal fade" id="create-user-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">Criação de Usuário</h4>
            </div>
            <div id="modal-body" class="modal-body">
                <div class="form-group">
                    <div id="create-modalUser" class="modal-body">
                        <h1 align="center"></h1>
                        <p align="center"></p>
                    </div>
                </div>
                <p1 style="font-size:20px" align="left">O que deseja fazer?</p1>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal">Criar outro Usuário</button>
                    <a href="${pageContext.request.contextPath}/admin/user/users.jsp">
                        <button type="button" class="btn btn-primary">Ir para página de usuários</button>
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
                <li><a href="${pageContext.request.contextPath}/admin/user/users.jsp">
                    Gerenciar Usuários</a></li>
                <li>Novo Usuário</li>
            </ol>
            <h1>Cadastrar Usuário</h1>
            <form method="POST" id="new-user-form" class="form-horizontal" action="#">
                <input id="submit_handle" type="submit" style="display: none"/>
                <input type="hidden" id="firstLogin" name="firstLogin" value="true">
                <div class="form-group">
                    <label class="col-md-2 control-label" for="name">Nome</label>
                    <div class="col-md-6">
                        <input name="nome" type="text" id="name" class="form-control" required
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="username">Nome de Usuário</label>
                    <div class="col-md-6">
                        <input name="username" type="text" id="username" class="form-control" required
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="password">Senha</label>
                    <div class="col-md-6">
                        <input name="password" type="password" id="password" class="form-control" required
                               data-errormessage-value-missing="Campo Obrigatório"
                               data-errormessage="Senha não atinge os requisitos necessários: mínimo 8 caracteres sendo no mínimo 1 minúsculo, 1 maiúsculo e um caracter especial."
                               pattern="(?=^.{6,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="verifyPassword">Repita Senha</label>
                    <div class="col-md-6">
                        <input name="verifyPassword" type="password" id="verifyPassword" class="form-control" required
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="mail">E-mail</label>
                    <div class="col-md-6">
                        <input name="mail" type="email" id="mail" class="form-control" required
                               data-errormessage-type-mismatch="Email inválido."
                               data-errormessage-value-missing="Campo Obrigatório">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">Ativo</label>
                    <div class="col-md-6">
                        <div class="radio">
                            <input name="enabled" class="bootstrap-switch" id="enabled" type="checkbox">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label" for="roles">Roles</label>
                    <div class="col-md-10">
                        <select name="roles" class="selectpicker" multiple data-selected-text-format="count>3"
                                id="roles" title="Nenhuma Role selecionada"
                                required>
                            <option value="ROLE_ADMIN">Administrador</option>
                            <option value="ROLE_USER">Usuário</option>
                            <option value="ROLE_REST">Permissão para Requisições Rest</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-10 col-md-offset-2">
                        <button id="submit" type="submit" class="btn btn-primary">Salvar</button>
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