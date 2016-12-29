<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script>
    var db2del;
    var db2delHostname;

    //hiding alerts
    $(document).ready(function () {
        $('#deleteDBSuccess').hide();
        $('#deleteDBFailure').hide();
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
            var oTable = $('#dbTable').dataTable();
            $.ajax({
                url: '${pageContext.request.contextPath}/rest/resource/database/delete/' + db2del,
                type: "DELETE",
                success: function () {
                    $("tr:contains('" + db2del + "')").each(function () {
                        oTable.fnDeleteRow(this);
                    });
                    $('#deleteDBSuccess > strong').text(db2delHostname);
                    $('#deleteDBSuccess').show();
                },
                error: function () {
                    $('#deleteDBFailure > strong').text(db2delHostname);
                    $('#deleteDBFailure').show();
                }
            });
            $('#delete-db-modal').modal('hide');
        });

        //populate the users datatable from json
        $(document).ready(function () {
            var oTable = $('#dbTable').dataTable();
            $.ajax({
                type: "GET",
                contentType: 'application/json',
                url: '${pageContext.request.contextPath}/rest/resource/database/list',
                dataType: 'json',
                success: function (db) {
                    var editUrl = '${pageContext.request.contextPath}/admin/resource/edit-database.jsp';
                    oTable.fnClearTable();
                    $.each(db, function (id, value) {
                        oTable.fnAddData([
                            value.id,
                            value.hostname,
                            value.address,
                            value.port,
                            value.username,
                            value.vendor,
                            value.instance,
                            value.queryDate,
                            value.status,
                            value.timestamp,
                            value.serverTime,
                            value.difference,
                            value.lastCheck,
                            value.db_name,
                            value.verify,
                            '<a href=' + editUrl + '?db=' + value.id + ' titlle="Editar DB"><i class="pficon-edit"> </i></a>' +
                            '&nbsp;' +
                            '<a href="javascript:setParameterUser(\'' + value.id + '\',\'' + value.hostname + '\');" title="Remover DB"><i class="pficon-delete"> </i></a>'
                        ])
                    });
                },
                error: function (xhr, textStatus, err) {
                    info = 'failed';
                }
            });
        });
    })
</script>
<script language="JavaScript">
    function setParameterUser(dbId, hostname) {
        $('#delete-modalDB> h1').text("Id do Banco de Dados: " + dbId);
        $('#delete-modalDB > p').text("Hostname: " + hostname);
        $('#delete-db-modal').modal('show');
        db2del = dbId;
        db2delHostname = hostname;
    }
</script>
<div id="deleteDBSuccess"
     class="toast-pf toast-pf-max-width toast-pf-top-right alert alert-success alert-dismissable">
    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
        <span class="pficon pficon-close"></span>
    </button>
    <span class="pficon pficon-ok"></span>
    O Banco de Dados <strong></strong> foi removido com sucesso.
</div>
<div id="deleteDBFailure"
     class="toast-pf toast-pf-max-width toast-pf-top-right alert alert-warning alert-dismissable">
    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
        <span class="pficon pficon-close"></span>
    </button>
    <span class="pficon pficon-ok"></span>
    Falha ao deletar SBanco de Dados ID <strong></strong>.
</div>
<div class="modal fade" id="delete-db-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <h4 class="modal-title" id="myModalLabel">Deletar Banco de Dados</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <div id="delete-modalDB" class="modal-body">
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
                <li>Banco de Dados Cadastrados</li>
                <li><a href="${pageContext.request.contextPath}/admin/resource/new-database.jsp">Novo Banco de Dados</a></li>
            </ol>
            <table id="dbTable" class="datatable table table-striped table-bordered">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Hostname</th>
                    <th>IP</th>
                    <th>Porta</th>
                    <th>Usuário</th>
                    <th>Vendor</th>
                    <th>Instância</th>
                    <th>Query</th>
                    <th>Status</th>
                    <th>Timestamp</th>
                    <th>Horário Server</th>
                    <th>Diferença</th>
                    <th>Última Checagem</th>
                    <th>SQL Server - db name</th>
                    <th>Verificação Ativa?</th>
                    <th>Ações</th>
                </tr>
                </thead>
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
                    <div id="collapseFive" class="panel-collapse collapse in">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li class="active"><a
                                        href="${pageContext.request.contextPath}/admin/resource/database.jsp">Gerenciar
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