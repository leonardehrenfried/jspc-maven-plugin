<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<html>

<%
  Map<String, Boolean> settings = new HashMap<>();
  settings.put("strict", true);
%>

<body>
  <c:if test="<%= settings.get("strict")%>">
    Strict
  </c:if>
  Quote Escaping
</body>

</html>
