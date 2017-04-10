<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script>
    var os2del;

    //hiding alerts
    $(document).ready(function () {
        $('#deleteOSSuccess').hide();
        $('#deleteOSFailure').hide();
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
            var oTable = $('#OsTable').dataTable();
            $.ajax({
                url: '${pageContext.request.contextPath}/rest/resource/operating-system/delete/' + os2del,
                type: "DELETE",
                success: function () {
                    $("tr:contains('" + os2del + "')").each(function () {
                        oTable.fnDeleteRow(this);
                    });
                    $('#deleteOSSuccess > strong').text(os2del);
                    $('#deleteOSSuccess').show();
                },
                error: function () {
                    $('#deleteOSFailure > strong').text(os2del);
                    $('#deleteOSFailure').show();
                }
            });
            $('#delete-os-modal').modal('hide');
        });

        //populate the users datatable from json
        $(document).ready(function () {
            var oTable = $('#OsTable').dataTable();
            $.ajax({
                type: "GET",
                contentType: 'application/json',
                url: '${pageContext.request.contextPath}/rest/resource/operating-system/list',
                dataType: 'json',
                success: function (os) {
                    var editUrl = '${pageContext.request.contextPath}/admin/resource/edit-operating-system.jsp';
                    oTable.fnClearTable();
                    $.each(os, function (id, value) {
                        oTable.fnAddData([
                            value.id,
                            value.hostname,
                            value.address,
                            value.port,
                            value.username,
                            value.type,
                            value.status,
                            value.osTime,
                            value.hrstatusTime,
                            value.difference,
                            value.lastCheck,
                            value.logDir,
                            value.suCommand,
                            value.toVerify,
                            '<a href=' + editUrl + '?os=' + value.id + ' titlle="Editar OS"><i class="pficon-edit"> </i></a>' +
                            '&nbsp;' +
                            '<a href="javascript:setParameterUser(\'' + value.id + '\',\'' + value.hostname + '\');" title="Remover OS"><i class="pficon-delete"> </i></a>'
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
    function setParameterUser(osId, hostname) {
        $('#delete-modalOS> h1').text("Id do OS: " + osId);
        $('#delete-modalOS > p').text("Hostname: " + hostname);
        $('#delete-os-modal').modal('show');
        os2del = osId;
    }
</script>
<div id="deleteOSSuccess"
     class="toast-pf toast-pf-max-width toast-pf-top-right alert alert-success alert-dismissable">
    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
        <span class="pficon pficon-close"></span>
    </button>
    <span class="pficon pficon-ok"></span>
    O Sistema Operacional <strong></strong> foi removido com sucesso.
</div>
<div id="deleteOSFailure"
     class="toast-pf toast-pf-max-width toast-pf-top-right alert alert-warning alert-dismissable">
    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
        <span class="pficon pficon-close"></span>
    </button>
    <span class="pficon pficon-ok"></span>
    Falha ao deletar Sistema Operacional ID <strong></strong>.
</div>
<div class="modal fade" id="delete-os-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">Deletar Sistema Operacional</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <div id="delete-modalOS" class="modal-body">
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
                <li>Sistemas Operacionais Cadastrados</li>
                <li><a href="${pageContext.request.contextPath}/admin/resource/new-operating-system.jsp">
                    Novo Sistema Operacional</a></li>
            </ol>
            <table id="OsTable" class="datatable table table-striped table-bordered">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Hostname</th>
                    <th>IP</th>
                    <th>Porta</th>
                    <th>Usuário</th>
                    <th>Sistema Operacional</th>
                    <th>Status</th>
                    <th>Horário OS</th>
                    <th>Horário Server</th>
                    <th>Diferença</th>
                    <th>Última Checagem</th>
                    <th>Diretório de Log</th>
                    <th>Comando su</th>
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