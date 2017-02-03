<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-md-10 col-sm-push-3 col-md-push-2">
            <ol class="breadcrumb">
                <li><a href="/hs/home/home.jsp">Home</a></li>
                <li>Usuários Cadastrados</li>
                <li><a href="${pageContext.request.contextPath}/admin/user/user_form.jsp">
                    Novo Usuário</a></li>
            </ol>

            <ul class="nav nav-tabs">
                <li class="active"><a href="#">Tab One</a></li>
                <li><a href="#">Tab Two</a></li>
                <li><a href="#">Tab Three</a></li>
                <li><a href="#">Tab Four</a></li>
                <li><a href="#">Tab Five</a></li>
            </ul>

        </div><!-- /col -->

        <%@ include file="/home/right-side-menu.jsp" %>
    </div>
</div>
<!-- /row -->
</div>
<!-- /container -->
</body>
</html>