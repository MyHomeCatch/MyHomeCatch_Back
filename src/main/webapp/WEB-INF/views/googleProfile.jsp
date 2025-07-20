<%--
  Created by IntelliJ IDEA.
  User: sohyun
  Date: 2025. 7. 18.
  Time: 13:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head><title>Google User Info</title></head>
<body>
<h1>구글 로그인 성공</h1>
<p>이메일: ${user.email}</p>
<p>이름: ${user.name}</p>
<p>프로필 사진: <img src="${user.picture}" /></p>
</body>
</html>
