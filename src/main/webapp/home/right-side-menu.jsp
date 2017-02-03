<%@ page contentType="text/html; charset=UTF-8" %>
<script>

    var USERS_LOCATION = "/hs/admin/user/users.jsp";
    var EDIT_USER_LOCATION = "/hs/admin/user/edit_user.jsp";
    var NEW_USER_LOCATION = "/hs/admin/user/user_form.jsp";
    var UPDATE_USER_LOCATION = "/hs/user/edit.jsp";
    var SERVIDORES_LOCATION = "/hs/admin/resource/operating-system.jsp";
    var EDIT_OS_LOCATION = "/hs/admin/resource/edit-operating-system.jsp";
    var NEW_OS_LOCATION = "/hs/admin/resource/new-operating-system.jsp";
    var BANCO_DADOS_LOCATION = "/hs/admin/resource/database.jsp";
    var EDIT_DATABASE_LOCATION = "/hs/admin/resource/edit-database.jsp";
    var NEW_DATABASE_LOCATION = "/hs/admin/resource/new-database.jsp";
    var CONFIGURACOES_LOCATION = "/hs/admin/setup.jsp";
    var VERIFICACOES_LOCATION = "/hs/verification/";
    var VERIFICACAO_DB_LOCATION = "/hs/verification/database.jsp";
    var VERIFICACAO_OS_LOCATION = "/hs/verification/os.jsp";
    var LOGS_LOCATION = "/hs/#";
    var RELATORIOS_LOCATION = "/hs/#";
    var GRAFICOS_LOCATION = "/hs/#";
    var AGENTAMENTOS_LOCATION = "/hs/#";
    var ABOUT_LOCATION = "/hs/home/about";


    $(document).ready(function () {
        var pathname = window.location.pathname;

        if (pathname == USERS_LOCATION || pathname == EDIT_USER_LOCATION || pathname == NEW_USER_LOCATION || pathname == UPDATE_USER_LOCATION) {
            $('#collapseOne').addClass("in");
            $('#liUsers').addClass("active");
        }
        if (pathname == SERVIDORES_LOCATION || pathname == EDIT_OS_LOCATION || pathname == NEW_OS_LOCATION) {
            $('#collapseTwo').addClass("in");
            $('#liServidores').addClass("active");
        }
        if (pathname == BANCO_DADOS_LOCATION || pathname == EDIT_DATABASE_LOCATION || pathname == NEW_DATABASE_LOCATION) {
            $('#collapseThree').addClass("in");
            $('#liDb').addClass("active");
        }
        if (pathname == CONFIGURACOES_LOCATION) {
            $('#collapseFour').addClass("in");
            $('#liConfs').addClass("active");
        }
        if (pathname.indexOf(VERIFICACOES_LOCATION) != -1) {
            $('#collapseFive').addClass("in");
        }
        if (pathname == VERIFICACAO_DB_LOCATION) {
            $('#collapseFive').addClass("in");
            $('#liVerDB').addClass("active");
        }
        if (pathname == VERIFICACAO_OS_LOCATION) {
            $('#collapseFive').addClass("in");
            $('#liVerOS').addClass("active");
        }
        if (pathname == LOGS_LOCATION) {
            $('#collapseSix').addClass("in");
            $('#liLogs').addClass("active");
        }
        if (pathname == RELATORIOS_LOCATION) {
            $('#collapseSeven').addClass("in");
            $('#liAbout').addClass("active");
        }
        if (pathname == GRAFICOS_LOCATION) {
            $('#collapseEight').addClass("in");
            $('#liAbout').addClass("active");
        }
        if (pathname == AGENTAMENTOS_LOCATION) {
            $('#collapseNine').addClass("in");
            $('#liAbout').addClass("active");
        }
        if (pathname == ABOUT_LOCATION) {
            $('#collapseTen').addClass("in");
            $('#liAbout').addClass("active");
        }
    });
</script>


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
                        <li id="liUsers"><a href="${pageContext.request.contextPath}/admin/user/users.jsp">
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
                        <li id="liServidores"><a href="${pageContext.request.contextPath}/admin/resource/operating-system.jsp">Gerenciar
                            Servidores</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4 class="panel-title">
                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseThree" class="collapsed">
                        Banco de Dados
                    </a>
                </h4>
            </div>
            <div id="collapseThree" class="panel-collapse collapse">
                <div class="panel-body">
                    <ul class="nav nav-pills nav-stacked">
                        <li id="liDb"><a href="${pageContext.request.contextPath}/admin/resource/database.jsp">Gerenciar Banco de
                            Dados</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4 class="panel-title">
                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseFour" class="collapsed">
                        Configuração
                    </a>
                </h4>
            </div>
            <div id="collapseFour" class="panel-collapse collapse">
                <div class="panel-body">
                    <ul class="nav nav-pills nav-stacked">
                        <li id="liConfs"><a href="${pageContext.request.contextPath}/admin/setup.jsp">Editar Configuração</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4 class="panel-title">
                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseFive" class="collapsed">
                        Verificações
                    </a>
                </h4>
            </div>
            <div id="collapseFive" class="panel-collapse collapse">
                <div class="panel-body">
                    <ul class="nav nav-pills nav-stacked">
                        <li id="liVerOS"><a href="${pageContext.request.contextPath}/verification/os.jsp">Servidores</a></li>
                        <li id="liVerDB"><a href="${pageContext.request.contextPath}/verification/database.jsp">Banco de Dados</a></li>
                        <li><a href="#">Estou pensando no que add</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4 class="panel-title">
                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseSix" class="collapsed">
                        Logs
                    </a>
                </h4>
            </div>
            <div id="collapseSix" class="panel-collapse collapse">
                <div class="panel-body">
                    <ul class="nav nav-pills nav-stacked">
                        <li id="liLogs"><a href="#">Extrair Logs</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4 class="panel-title">
                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseSeven" class="collapsed">
                        Relatórios
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
                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseEight" class="collapsed">
                        Gráficos
                    </a>
                </h4>
            </div>
            <div id="collapseEight" class="panel-collapse collapse">
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
                        Agendamentos
                    </a>
                </h4>
            </div>
            <div id="collapseNine" class="panel-collapse collapse">
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
                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseTen" class="collapsed">
                        Sobre o Hrstatus
                    </a>
                </h4>
            </div>
            <div id="collapseTen" class="panel-collapse collapse">
                <div class="panel-body">
                    <ul class="nav nav-pills nav-stacked">
                        <li id="liAbout"><a href="${pageContext.request.contextPath}/home/about">Sobre</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>