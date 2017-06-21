<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-md-10 col-sm-push-3 col-md-push-2">
            <ol class="breadcrumb">
                <li><a href="/hs/home/home.jsp">Home</a></li>
                <li>Verificações - Banco de Dados</li>
            </ol>
            <ul class="nav nav-tabs">
                <li class="active"><a href="/hs/verification/db/verification.jsp" name="verification">Verificações</a></li>
                <li><a href="/hs/verification/db/databasenok.jsp" name="database_nok">Banco de Dados Desatualizados</a></li>
                <li><a href="/hs/verification/db/databaseok.jsp" name="database_ok">Banco de Dados OK</a></li>
            </ul>
            OK
        </div><!-- /col -->

        <%@ include file="/home/left-side-menu.jsp" %>
    </div>
</div>
<!-- /row -->
</div>
<!-- /container -->
</body>
</html>