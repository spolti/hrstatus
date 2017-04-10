<%@ include file="/home/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<script type="text/javascript">
    $(document).ready(function () {
        $.ajax({
            type: "GET",
            contentType: 'application/json',
            url: '${pageContext.request.contextPath}/rest/utils/resource/server-info',
            dataType: 'json',
            success: function (response) {
                $('#version').text(response.version);
                $('#installationDate').text(response.installationDate);
                $('#java').text(response.java);
                $('#javaVersion').text(response.javaVersion);
                $('#javaVendor').text(response.javaVendor);
                $('#osVersion').text(response.osVersion);
                $('#uptime').text(response.uptime);
            },
            error: function (xhr, textStatus, err) {
                var response = JSON.parse(xhr.responseText);
                console.log(response);
            }
        });

    });
</script>
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
                            <dd id="version"></dd>
                            <dt>Data de Instalação:</dt>
                            <dd id="installationDate"></dd>
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
                            <dd id="java"></dd>
                            <dt>Versão</dt>
                            <dd id="javaVersion"></dd>
                            <dt>Vendor</dt>
                            <dd id="javaVendor"></dd>
                            <dt>Versão do Sistema Operacional</dt>
                            <dd id="osVersion"></dd>
                            <dt>Uptime</dt>
                            <dd id="uptime"></dd>
                        </dl>
                    </div><!-- /col -->
                </div><!-- /row -->
            </section>
        </div><!-- /col -->

        <%@ include file="/home/left-side-menu.jsp" %>
    </div>
</div>
<!-- /row -->
</div>
<!-- /container -->
</body>
</html>