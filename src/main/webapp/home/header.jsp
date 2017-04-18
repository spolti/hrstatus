<!--
Copyright (C) 2012 Filippe Costa Spolti

This file is part of Hrstatus.

Hrstatus is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
-->

<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<!--[if IE 9]><html lang="pt-br" class="ie9"><![endif]-->
<!--[if gt IE 9]><!-->
<html lang="pt-br">
<!--<![endif]-->
<head>
    <title>HrStatus</title>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/patternfly/img/favicon.ico">
    <!-- iPad retina icon -->
    <link rel="apple-touch-icon-precomposed" sizes="152x152"
          href="${pageContext.request.contextPath}/patternfly/img/apple-touch-icon-precomposed-152.png">
    <!-- iPad retina icon (iOS < 7) -->
    <link rel="apple-touch-icon-precomposed" sizes="144x144"
          href="${pageContext.request.contextPath}/patternfly/img/apple-touch-icon-precomposed-144.png">
    <!-- iPad non-retina icon -->
    <link rel="apple-touch-icon-precomposed" sizes="76x76"
          href="${pageContext.request.contextPath}/patternfly/img/apple-touch-icon-precomposed-76.png">
    <!-- iPad non-retina icon (iOS < 7) -->
    <link rel="apple-touch-icon-precomposed" sizes="72x72"
          href="${pageContext.request.contextPath}/patternfly/img/apple-touch-icon-precomposed-72.png">
    <!-- iPhone 6 Plus icon -->
    <link rel="apple-touch-icon-precomposed" sizes="120x120"
          href="${pageContext.request.contextPath}/patternfly/img/apple-touch-icon-precomposed-180.png">
    <!-- iPhone retina icon (iOS < 7) -->
    <link rel="apple-touch-icon-precomposed" sizes="114x114"
          href="${pageContext.request.contextPath}/patternfly/img/apple-touch-icon-precomposed-114.png">
    <!-- iPhone non-retina icon (iOS < 7) -->
    <link rel="apple-touch-icon-precomposed" sizes="57x57"
          href="${pageContext.request.contextPath}/patternfly/img/apple-touch-icon-precomposed-57.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/patternfly/css/patternfly.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/patternfly/css/patternfly-additions.min.css">
    <script src="${pageContext.request.contextPath}/patternfly/jquery/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/patternfly/jquery/jquery.data-table.js"></script>
    <script src="${pageContext.request.contextPath}/patternfly/bootstrap/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/patternfly/bootstrap/bootstrap-select.min.js"></script>
    <script src="${pageContext.request.contextPath}/patternfly/bootstrap/bootstrap-switch.min.js"></script>
    <script src="${pageContext.request.contextPath}/patternfly/js/patternfly.min.js"></script>
    <script src="${pageContext.request.contextPath}/patternfly/js/civem.js"></script>
    <script src="${pageContext.request.contextPath}/hrstatus-js/common/common-functions.js"></script>
</head>

<body onload="Hora()">
<script type="text/javascript">
    function Hora() {
        horario = new Date();
        hora = horario.getHours();
        minuto = horario.getMinutes();
        segundo = horario.getSeconds();
        if (hora < 10) {
            hora = "0" + hora;
        }
        if (minuto < 10) {
            minuto = "0" + minuto;
        }
        if (segundo < 10) {
            segundo = "0" + segundo;
        }
        document.getElementById("time").innerHTML = hora + ":" + minuto;
        +":" + segundo; // hora no documento
    }
    window.setInterval("Hora()", 1000);
</script>
<nav class="navbar navbar-default navbar-pf" role="navigation">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse-1">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="/hs/home/home.jsp">
            <b>HrStatus Server</b>
        </a>
    </div>
    <div class="collapse navbar-collapse navbar-collapse-1">
        <ul class="nav navbar-nav navbar-utility">
            <li>
                <a href="#">Horário Local
                    <div align="center" id="time">time</div>
                </a>
            </li>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    <span class="pficon pficon-user"></span>
                    ${pageContext.request.userPrincipal.name} <b class="caret"></b>
                </a>
                <ul class="dropdown-menu">
                    <li>
                        <a href="${pageContext.request.contextPath}/user/edit.jsp">Editar
                            informações pessoais</a>
                    </li>
                    <li>
                        <a href="#">Something else here</a>
                    </li>
                    <li class="divider"></li>
                    <li class="dropdown-submenu">
                        <a tabindex="-1" href="#">More options</a>
                        <ul class="dropdown-menu">
                            <li>
                                <a href="#">Link</a>
                            </li>
                            <li>
                                <a href="#">Another link</a>
                            </li>
                            <li>
                                <a href="#">Something else here</a>
                            </li>
                            <li class="divider"></li>
                            <li class="dropdown-header">Nav header</li>
                            <li>
                                <a href="#">Separated link</a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <a href="#">One more separated link</a>
                            </li>
                        </ul>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <a href="${pageContext.request.contextPath}/logout">Logout</a>
                    </li>
                </ul>
            </li>
        </ul>
    </div>
</nav>