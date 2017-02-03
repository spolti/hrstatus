<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9 col-md-10 col-sm-push-3 col-md-push-2">
            <ol class="breadcrumb">
                <li><a href="/hs/home/home.jsp">Home</a></li>
                <li>Sobre o HrStatus</li>
            </ol>
            <h1>Veja abaixo algumas informações importantes sobre o servidor que o HrStatus está instalado.</h1>
            <section>
                <h2>Informação do Servidor</h2>
                <div class="row">
                    <div class="col-sm-8 col-md-8 border-right">
                        <dl class="dl-horizontal">
                            <a href="http://www.hrstatus.com.br/hrstatus/home.html"
                               target="_blank"> <img
                                    src="${pageContext.request.contextPath}/patternfly/img/hrimg.jpg"></a>
                        </dl>
                    </div><!-- /col -->
                    <div class="col-sm-5 col-md-5 border-right">
                        <dl>
                            <dt>Versão</dt>
                            <dd>${version}</dd>
                            <dt>Data de Instalação:</dt>
                            <dd>${installationDate}</dd>
                            <dt>Para Suporte</dt>
                            <dd><a href="mailto:spolti@hrstatus.com.br?Subject=Suporte%20Hrstatus">Contato</a></dd>
                            <dt>Para reportar bugs</dt>
                            <dd><a href="https://github.com/hrstatus/hrstatus/issues/new" target="_blank"> Registrar
                                Issue </a></dd>
                            <dt>Documentação</dt>
                            <dd><a href="${pageContext.request.contextPath}/doc/DocumentacaoHrStatus.pdf"
                                   target="_blank"> Vizualizar </a></dd>
                        </dl>
                    </div>
                    <div class="col-sm-4 col-md-4">
                        <dl>
                            <dt>Java</dt>
                            <dd>${java}</dd>
                            <dt>Versão</dt>
                            <dd>${javaVersion}</dd>
                            <dt>Vendor</dt>
                            <dd>${javaVendor}</dd>
                            <dt>Versão do Sistema Operacional</dt>
                            <dd>${osVersion}</dd>
                            <dt>Uptime</dt>
                            <dd>${uptime}</dd>
                        </dl>
                    </div><!-- /col -->
                </div><!-- /row -->
            </section>
        </div><!-- /col -->

        <%@ include file="/home/right-side-menu.jsp" %>
    </div>
</div>
<!-- /row -->
</div>
<!-- /container -->
</body>
</html>