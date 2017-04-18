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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<!--[if IE 9]>
<html class="ie9 login-pf"><![endif]-->
<!--[if gt IE 9]><!-->
<html class="login-pf">
<!--<![endif]-->
<head>
    <title>Login - HrStatus</title>
    <meta charset="UTF-8">
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
    <link href="${pageContext.request.contextPath}/patternfly/css/patternfly.min.css" rel="stylesheet"
          media="screen, print">
    <link href="${pageContext.request.contextPath}/patternfly/css/patternfly-additions.min.css" rel="stylesheet"
          media="screen, print">
    <script src="${pageContext.request.contextPath}/patternfly/jquery/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/patternfly/bootstrap/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/patternfly/js/patternfly.min.js"></script>

    <script>
        $(document).ready(function () {
            $.ajax({
                type: "GET",
                contentType: 'application/json',
                url: '${pageContext.request.contextPath}/rest/public/welcome-message',

                success: function (response) {
                    console.log('success ' + response);
                    $('#welcomeMessage').text(response)
                },
                error: function (xhr, textStatus, err) {
                    $('#welcomeMessage').text(textStatus.valueOf())
                }
            });
        })
    </script>
</head>
<c:if test="${not empty param['failed']}">
    <div class="toast-pf toast-pf-max-width toast-pf-top-right alert alert-warning alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
            <span class="pficon pficon-close"></span>
        </button>
        <span class="pficon pficon-warning-triangle-o"></span>
        Usuário ou senha incorretos
    </div>
</c:if>
<body>
<span id="badge">
      <img src="${pageContext.request.contextPath}/patternfly/img/logo.svg" />
    </span>
<div class="container">
    <div class="row">
        <div class="col-sm-12">
            <div id="brand">
                <b>HrStatus Server</b>
            </div><!--/#brand-->
        </div><!--/.col-*-->
            <div class="col-sm-7 col-md-6 col-lg-5 login">
                <form class="form-horizontal" role="form" method=post action="${pageContext.request.contextPath}/login">
                    <div class="form-group">
                        <label for="inputUsername" class="col-sm-2 col-md-2 control-label">Usuário</label>
                        <div class="col-sm-10 col-md-10">
                            <input name="j_username" type="text" class="form-control" id="inputUsername" placeholder="" tabindex="1">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputPassword" class="col-sm-2 col-md-2 control-label">Senha</label>
                        <div class="col-sm-10 col-md-10">
                            <input name= "j_password" type="password" class="form-control" id="inputPassword" placeholder="" tabindex="2">
                        </div>
                    </div>
                    <br>
                    <div class="form-group">
                        <div class="col-xs-8 col-sm-offset-2 col-sm-6 col-md-offset-2 col-md-6">
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" tabindex="3"> Lembrar usuário
                                </label>
                            </div>
                        <span class="help-block"> Esqueceu <a href="#" tabindex="5">usuário</a> ou <a href="#"
                                                                                                      tabindex="6">senha</a>?</span>
                        </div>
                        <div class="col-xs-4 col-sm-4 col-md-4 submit">
                            <button type="submit" class="btn btn-primary btn-lg" tabindex="4">Entrar</button>
                        </div>
                    </div>
                </form>
            </div><!--/.col-*-->
        <div class="col-sm-5 col-md-6 col-lg-7 details">
            <p><strong>HrStatus</strong> <br>
                <div id="welcomeMessage"></div></p>
        </div><!--/.col-*-->
    </div><!--/.row-->
</div><!--/.container-->
</body>
</html>