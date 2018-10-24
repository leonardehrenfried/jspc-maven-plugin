<!-- example from https://docs.oracle.com/javaee/5/tutorial/doc/bnama.html -->
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>

<html>

  <%
    String hello = "03";
  %>
	
	<body>
		Hello <%=hello %>
    
    	<%@ include file="include3.jspf"%>
	</body>  

</html>

