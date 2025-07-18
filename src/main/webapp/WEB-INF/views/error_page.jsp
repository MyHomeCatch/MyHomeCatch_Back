<%--
  Created by IntelliJ IDEA.
  User: ahranah
  Date: 6/12/25
  Time: 3:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" import="java.util.*" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <title>Title</title>
</head>
<body>
<h4> <c:out value="${exception.getMessage()}"> </c:out></h4>
<ul>
  <c:forEach items="${exception.getStackTrace()}" var="stack">
    <li><c:out value="${stack}"> </c:out></li>
  </c:forEach>
</ul>
</body>
</html>
