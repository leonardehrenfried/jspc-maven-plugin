<!-- example from https://docs.oracle.com/javaee/5/tutorial/doc/bnama.html -->
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
    
<html>

  <%
    String hello = "01";
	
	<body>
 		<form method="get">
			<input type="text" name="username" size="25">
			<p></p>
			<input type="submit" value="Submit">
			<input type="reset" value="Reset">
		</form>
	
		Hello <%=hello %>
		
		<h:response1 name="${param.username}"/>
	</body>  

</html>

