<%--
  Created by IntelliJ IDEA.
  User: jiujiu糖
  Date: 2019/4/25
  Time: 16:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>home</title>
</head>
<body>

<h1>欢迎${username}</h1>
<c:if test="${cookie.remind.value == null}">
    <h1> 您的登陆密码是${password}</h1><br>
</c:if>
<h2><a href="/user/logout">退出</a></h2>

</body>
</html>
