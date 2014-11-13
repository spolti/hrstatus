<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../home/navbar.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${title}</title>

</head>
<body>
	<div class="container">
		<div class="content" id="tailContent">
			<h2>Diretório: ${logDir} || Arquivo: ${fileName}</h2>
			<h3>Extraídas ${qtdn} linhas do arquivo.</h3>
			<div id="logLines">
				<c:forEach begin="1" var="linha" items="${linhasArquivo}">
					<p>${linha}</p>
				</c:forEach>
			</div>
			<a href="<c:url value='/listLogFiles/${hostname}' />" value="Voltar">Voltar</a>
		</div>
	</div>
</body>
</html>