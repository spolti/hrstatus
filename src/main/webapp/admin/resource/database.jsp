<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script>
    var db2del;
    var db2delHostname;

    // Initialize Datatables
    $(document).ready(function () {
        //hiding alerts
        $('#deleteDBSuccess').hide();
        $('#deleteDBFailure').hide();

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
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
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
                <li><a href="${pageContext.request.contextPath}/admin/resource/new-database.jsp">Novo Banco de Dados</a>
                </li>
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
        <%@ include file="/home/left-side-menu.jsp" %>
    </div>
</div>
<!-- /container -->
</body>
</html>