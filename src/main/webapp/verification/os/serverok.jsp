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
                <li><a href="/hs/verification/os/servernok.jsp" name="server_nok">Servidores Desatualizados</a></li>
                <li class="active"><a href="/hs/verification/os/serverok.jsp" name="server_ok">Servidores OK</a></li>
            </ul>

            <table id="serversOkTable" class="datatable table table-striped table-bordered">
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
<!-- /row -->
</div>
<!-- /container -->
</body>
</html>