<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../home/navbar.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Configuração Clientes</title>
	<script src="http://code.jquery.com/jquery-latest.js" > </script>
	<script>
		function callTail(fileName, divName){
			nm = fileName.trim().split(" ");
			dv = "#"+divName;
			valor = $(dv).val();
			if(fileName === null || fileName === '' ||
					valor === null || valor === ''){
				alert("Número de linhas é obrigatório.")
				return; 
			}
			$.get('${pageContext.request.contextPath}/tailFile/${hostname}/'+nm[1]+'/'+valor, "", function(retorno) {    
				 var tailContent = $("#tailContent", retorno);
				 $('#pageContent').empty().append(tailContent);
			})
			.fail(function() { alert("Erro ao executar comando."); });
		}

		function findInFile(fileName, divName){
			nm = fileName.trim().split(" ");
			dv = "#"+divName;
			valor = $(dv).val();
			if(fileName == null || fileName === '' ||
					valor == null || valor === ''){
				alert("Palavra da busca é obrigatória.")
				return; 
			}
			$.get('${pageContext.request.contextPath}/findInFile/${hostname}/'+nm[1]+'/'+valor, "", function(retorno) {    
				 var tailContent = $("#findContent", retorno);
				 $('#pageContent').empty().append(tailContent);
			})
			.fail(function() { alert("Erro ao executar comando."); });
		}

		function numbersonly(myfield, e) {
			var key;
			var keychar;
			if (window.event)
				key = window.event.keyCode;
			else if (e)
				key = e.which;
			else
				return true;
			keychar = String.fromCharCode(key);
			// control keys
			if ((key == null) || (key == 0) || (key == 8) || (key == 9)
					|| (key == 13) || (key == 27))
				return true;
			// numbers
			else if ((("0123456789").indexOf(keychar) > -1))
				return true;

			return false;
		}
			
		
	</script>

</head>
<body>

	<div class="container">
		<div class="content" id="pageContent">
			<div class="row">
				<div class="span12">
					<table class="table table-striped" id="resultTable">
						<h3>Index of ${logDir}</h3>
						<HR WIDTH="50%" />
						<thead>
							<tr>
								<th>Arquivos encontrados: ${qtdn }</th>
								<th>Visualizar últimas N linhas</th>
								<th>Buscar no arquivo</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach begin="1" var="file" varStatus="status" items="${listOfFiles}">
								<tr>
									<td><a href="<c:url value='/downloadFile/${hostname}/${file}' />" value="${file}">${file}</a></td>
									<td>
										<div style="display: inline">
											Linhas: 
											<input type="text" name="numLinhas" style="margin-bottom: 0px;" id="file${status.index}" onkeypress="return numbersonly(this,event)"/>
											<input type="image" src="../img/tail.png" name="image" width="20" height="20" onclick="callTail('${file}', 'file${status.index}')"/>
										</div>
									</td>
									<td>
										<div style="display: inline">
											Buscar: 
											<input type="text" name="palavraBusca" style="margin-bottom: 0px;" id="findfile${status.index}"/>
											<input type="image" src="../img/search.png" name="image" width="20" height="20" onclick="findInFile('${file}', 'findfile${status.index}')"/>
										</div>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>