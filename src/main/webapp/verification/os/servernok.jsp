<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script>
    $(document).ready(function () {
        // Initialize Datatables
        $('.datatable').dataTable({
            "fnDrawCallback": function (oSettings) {
                // if .sidebar-pf exists, call sidebar() after the data table is drawn
                if ($('.sidebar-pf').length > 0) {
                    $(document).sidebar();
                }
            }
        });

        //populate the resources not ok datatable from json
        $(document).ready(function () {
            var oTable = $('#serversNOkTable').dataTable();
            $.ajax({
                type: "GET",
                contentType: 'application/json',
                url: '${pageContext.request.contextPath}/rest/common/resources-status/os/NOT_OK',
                dataType: 'json',
                success: function (os) {
                    var editUrl = '${pageContext.request.contextPath}/admin/resource/edit-operating-system.jsp';
                    oTable.fnClearTable();
                    $.each(os, function (id, value) {
                        oTable.fnAddData([
                            value.id,
                            value.hostname,
                            value.type,
                            value.address,
                            value.port,
                            value.username,
                            value.status,
                            value.osTime,
                            value.hrstatusTime,
                            value.difference,
                            value.lastCheck,
                            value.toVerify,
                            '<a href=' + editUrl + '?os=' + value.id + ' titlle="Editar OS"><i class="pficon-edit"> </i></a>' +
                            '&nbsp;' +
                            '<a href="javascript:setParameterUser(\'' + value.id + '\',\'' + value.hostname + '\');" alt="Iniciar Verificação Neste Recurso"><i class="pficon-build"> </i></a>'
                        ])
                    });
                },
                error: function (xhr, textStatus, err) {
                    info = 'failed';
                }
            });
        });

    });
</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-md-10 col-sm-push-3 col-md-push-2">
            <ol class="breadcrumb">
                <li><a href="/hs/home/home.jsp">Home</a></li>
                <li>Verificações - Sistemas Operacionais</li>
            </ol>
            <ul class="nav nav-tabs">
                <li><a href="/hs/verification/os/verification.jsp" name="verification">Verificações</a></li>
                <li class="active"><a href="/hs/verification/os/servernok.jsp" name="server_nok">Servidores Desatualizados</a></li>
                <li><a href="/hs/verification/os/serverok.jsp" name="server_ok">Servidores OK</a></li>
            </ul>

            <table id="serversNOkTable" class="datatable table table-striped table-bordered">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Hostname</th>
                    <th>Sistema Operacional</th>
                    <th>IP</th>
                    <th>Porta</th>
                    <th>Usuário</th>
                    <th>Status</th>
                    <th>Horário OS</th>
                    <th>Horário Server</th>
                    <th>Diferença</th>
                    <th>Última Checagem</th>
                    <th>Verificação Ativa?</th>
                    <th>Ações</th>
                </tr>
                </thead>
            </table>
        </div><!-- /col -->

        <%@ include file="/home/left-side-menu.jsp" %>
    </div>
</div>
<!-- /row -->
</div>
<!-- /container -->
</body>
</html>