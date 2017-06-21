<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-md-10 col-sm-push-3 col-md-push-2">
            <ol class="breadcrumb">
                <li><a href="/hs/home/home.jsp">Home</a></li>
                <li>Verificações - Sistemas Operacionais</li>
            </ol>
            <ul class="nav nav-tabs">
                <li class="active"><a href="/hs/verification/os/verification.jsp" name="verification">Verificações</a></li>
                <li><a href="/hs/verification/os/servernok.jsp" name="server_nok">Servidores Desatualizados</a></li>
                <li><a href="/hs/verification/os/serverok.jsp" name="server_ok">Servidores OK</a></li>
            </ul>
            verificação
        </div><!-- /col -->

        <%@ include file="/home/left-side-menu.jsp" %>
    </div>
</div>
<!-- /row -->
</div>
<!-- /container -->
</body>
</html>