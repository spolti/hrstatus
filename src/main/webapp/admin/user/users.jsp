<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script>
    var user2del;

    //hiding alerts
    $(document).ready(function () {
        $('#deleteUserSuccess').hide();
        $('#deleteUserFailure').hide();
    });

    // Initialize Datatables
    $(document).ready(function () {
        $('.datatable').dataTable({
            "fnDrawCallback": function (oSettings) {
                // if .sidebar-pf exists, call sidebar() after the data table is drawn
                if ($('.sidebar-pf').length > 0) {
                    $(document).sidebar();
                }
            }
        });

        $('#btDelete').click(function () {
            var oTable = $('#usersTable').dataTable();
            $.ajax({
                url: '${pageContext.request.contextPath}/rest/user/admin/delete/' + user2del,
                type: "DELETE",
                success: function () {
                    $("tr:contains('" + user2del + "')").each(function () {
                        oTable.fnDeleteRow(this);
                    });
                    $('#deleteUserSuccess > strong').text(user2del);
                    $('#deleteUserSuccess').show();
                },
                error: function () {
                    $('#deleteUserFailure > strong').text(user2del);
                    $('#deleteUserFailure').show();
                }
            });
            $('#delete-user-modal').modal('hide');
        });
    });

    //populate the users datatable from json
    $(document).ready(function () {
        var oTable = $('#usersTable').dataTable();
        $.ajax({
            type: "GET",
            contentType: 'application/json',
            url: '${pageContext.request.contextPath}/rest/user/list',
            dataType: 'json',
            success: function (user) {
                var editUrl = '${pageContext.request.contextPath}/admin/user/edit_user.jsp';
                console.log('success ' + user.valueOf());
                oTable.fnClearTable();
                $.each(user, function (id, value) {
                    oTable.fnAddData([
                        value.nome,
                        value.username,
                        value.mail,
                        value.lastLogin,
                        value.lastLoginAddressLocation,
                        value.enabled,
                        value.failedLogins,
                        value.firstLogin,
                        value.roles,
                        '<a href=' + editUrl + '?username=' + value.username + ' titlle="Editar Usuário"><i class="pficon-edit"> </i></a>' +
                        '&nbsp;' +
                        '<a href="javascript:setParameterUser(\'' + value.username + '\',\'' + value.nome + '\');" title="Remover Usuário"><i class="pficon-delete"> </i></a>'
                    ])
                });
            },
            error: function (xhr, textStatus, err) {
                info = 'failed';
            }
        });
    })

    function setParameterUser(username, nome) {
        $('#delete-modalUser > h1').text("Usuário: " + nome);
        $('#delete-modalUser > p').text("Username: " + username);
        $('#delete-user-modal').modal('show');
        user2del = username;
    }
</script>
<div id="deleteUserSuccess"
     class="toast-pf toast-pf-max-width toast-pf-top-right alert alert-success alert-dismissable">
    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
        <span class="pficon pficon-close"></span>
    </button>
    <span class="pficon pficon-ok"></span>
    Usuário <strong></strong> foi removido com sucesso.
</div>
<div id="deleteUserFailure"
     class="toast-pf toast-pf-max-width toast-pf-top-right alert alert-warning alert-dismissable">
    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
        <span class="pficon pficon-close"></span>
    </button>
    <span class="pficon pficon-ok"></span>
    Falha ao deletar usuário <strong></strong>.
</div>
<div class="modal fade" id="delete-user-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <h4 class="modal-title" id="myModalLabel">Deletar Usuário</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <div id="delete-modalUser" class="modal-body">
                        <h1 align="center"></h1>
                        <p align="center"></p>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button id="btDelete" type="button" class="btn btn-primary">Deletar</button>
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
                <li>Usuários Cadastrados</li>
                <li><a href="${pageContext.request.contextPath}/admin/user/user_form.jsp">
                    Novo Usuário</a></li>
            </ol>
            <table id="usersTable" class="datatable table table-striped table-bordered">
                <thead>
                <tr>
                    <th>Nome</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Último Login</th>
                    <th>Endereço IP</th>
                    <th>Ativo</th>
                    <th>Falhas de Login</th>
                    <th>Primeiro Login?</th>
                    <th>Roles</th>
                    <th>Ações</th>
                </tr>
                </thead>
            </table>
        </div><!-- /col -->
        <%@ include file="/home/left-side-menu.jsp" %>
    </div>
</div>
</div>
<!-- /row -->
</div>
<!-- /container -->
</body>
</html>