<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script>
    var user2del;
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
            var protocol = window.location.protocol;
            var host = window.location.host;
            var url = protocol + '//' + host + '${pageContext.request.contextPath}/rest/user/admin/delete/' + user2del;
            $.ajax({
                url: url,
                type: "DELETE",
                success: function () {
                    location.href = protocol + '//' + host + '${pageContext.request.contextPath}/rest/user/admin/list/form?status=successDelete&userDeleted=' + user2del;
                },
                error: function () {
                    location.href = protocol + '//' + host + '${pageContext.request.contextPath}/rest/user/admin/list/form?status=failed&userDeleted=' + user2del;
                }
            });
            $('#delete-user-modal').modal('hide');
        });
    });
</script>
<script language="JavaScript">
    function setParameterUser(username, nome) {
        $('#delete-modalUser > h1').text("Usuário: " + nome);
        $('#delete-modalUser > p').text("Username: " + username);
        $('#delete-user-modal').modal('show');
        user2del = username;
    }
</script>
<c:if test="${info == 'successDelete'}">
    <div class="toast-pf toast-pf-max-width toast-pf-top-right alert alert-success alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-ok"></span>
        Usuário <strong>${userDeleted}</strong> foi removido com sucesso.
    </div>
</c:if>
<c:if test="${info == 'success'}">
    <div class="toast-pf toast-pf-max-width toast-pf-top-right alert alert-success alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-ok"></span>
        Usuário <strong>${user}</strong> foi criado com sucesso.
    </div>
</c:if>
<c:if test="${update == 'success'}">
    <div class="toast-pf toast-pf-max-width toast-pf-top-right alert alert-success alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-ok"></span>
        Usuário <strong>${user}</strong> alterado com sucesso.
    </div>
</c:if>
<c:if test="${info == 'failed'}">
    <div class="toast-pf toast-pf-max-width toast-pf-top-right alert alert-warning alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-ok"></span>
        Falha ao deletar usuário <strong>${userDeleted}</strong>.
    </div>
</c:if>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-md-10 col-sm-push-3 col-md-push-2">
            <ol class="breadcrumb">
                <li><a href="/hs/home/home.jsp">Home</a></li>
                <li>Banco de Dados Cadastrados</li>
                <li><a href="${pageContext.request.contextPath}/admin/resource/new-database.jsp">
                    Novo Banco de Dados</a></li>
            </ol>
            <table class="datatable table table-striped table-bordered">
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
                <tbody>
                <c:forEach var="user" items="${userList}">
                    <tr>
                        <td>${user.nome}</td>
                        <td>${user.username}</td>
                        <td>${user.mail}</td>
                        <td>${user.lastLogin}</td>
                        <td>${user.lastLoginAddressLocation}</td>
                        <td>${user.enabled}</td>
                        <td>${user.failedLogins}</td>
                        <td>${user.firstLogin}</td>
                        <td>${user.roles}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/rest/user/admin/edit/${user.username}"
                               titlle="Editar Usuário"><i class="pficon-edit"> </i></a>
                            &nbsp;
                            <a href="javascript:setParameterUser('${user.username}' ,'${user.nome}');"
                               title="Remover Usuário"><i class="pficon-delete"> </i></a>
                        </td>

                    </tr>
                </c:forEach>
                </tbody>
            </table>
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
                                <li><a
                                        href="${pageContext.request.contextPath}/admin/user/users.jsp">
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
                    <div id="collapseFive" class="panel-collapse collapse in">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li class="active"><a href="${pageContext.request.contextPath}/rest/resource/database/load">Gerenciar Banco de Dados</a></li>
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
</body>
</html>