<!-- 
    Copyright (C) 2012  Filippe Costa Spolti

	This file is part of Hrstatus.

    Hrstatus is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<html>
<head>
<meta charset="utf-8">
<title>Página não encontrada</title>

<!-- CSS -->
<link href="${pageContext.request.contextPath}/css/bootstrap.css"
	rel="stylesheet">
<style type="text/css">

/* Sticky footer styles
      -------------------------------------------------- */
html,body {
	height: 100%;
	/* The html and body elements cannot have any padding or margin. */
}

/* Wrapper for page content to push down footer */
#wrap {
	min-height: 100%;
	height: auto !important;
	height: 100%;
	/* Negative indent footer by it's height */
	margin: 0 auto -60px;
}

/* Set the fixed height of the footer here */
#push,#footer {
	height: 60px;
}

#footer {
	background-color: #f5f5f5;
}

/* Lastly, apply responsive CSS fixes as necessary */
@media ( max-width : 767px) {
	#footer {
		margin-left: -20px;
		margin-right: -20px;
		padding-left: 20px;
		padding-right: 20px;
	}
}

/* Custom page CSS
      -------------------------------------------------- */
/* Not required for template or sticky footer method. */
.container {
	width: auto;
	max-width: 680px;
}

.container2 {
	margin-top: 30px;
	width: 650px;
}

.container .credit {
	margin: 20px 0;
}
</style>
<link
	href="${pageContext.request.contextPath}/css/bootstrap-responsive.css"
	rel="stylesheet">

<script src="${pageContext.request.contextPath}/js/jquery.js"
	type="text/javascript"></script>

<script>
	$(document).ready(function() {
		var progress = setInterval(function() {
			var $bar = $('.bar');
			if ($bar.width() == 650) {
				clearInterval(progress);
				$('.progress').removeClass('active');
				var url = window.location;
				var urlStr = url.toString();
				var urlArray = urlStr.split("/");
				$(window.location).attr('href', urlArray[0]+'/hs/home');
			} else {
				$bar.width($bar.width() + 65);
			}
			$bar.text($bar.width() / 6.5 + "%");
		}, 800);
		
		
		
	});
	
</script>
</head>

<body>

	<div id="wrap">

		<!-- Begin page content -->
		<div class="container">
			<div class="page-header">
				<h1>404 - Página não encontrada</h1>
			</div>
			<p class="lead">O recurso solicitado não foi encontrado, você
				será automaticamente redirecionado para a home da aplicação dentro
				de instantes.</p>

			<p class="lead">Aguarde</p>

			<div class="container2">
				<div class="progress progress-striped active">
					<div class="bar" style="width: 0%;"></div>
				</div>
			</div>
			<p class="lead">
				ou clique <a href="<c:url value="/home"/>"> aqui</a> para acessar
				imediatamente.
			</p>

		</div>

		<div id="push"></div>
	</div>

	<div id="footer">
		<div class="container">
			<p class="muted credit">
				<a href="http://www.hrstatus.com.br/hrstatus/home.html">Hrstatus</a> - 2012. Todos os Direitos Reservados
			</p>
		</div>
	</div>

</body>
</html>