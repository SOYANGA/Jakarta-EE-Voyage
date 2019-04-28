<%--
  Created by IntelliJ IDEA.
  User: jiujiu糖
  Date: 2019/4/28
  Time: 16:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>错误信息页面</title>
</head>
<body>
<c:if test="${error_message!=null}">
    <h1>页面发生异常</h1>
    <h2>异常信息如下:</h2>
    <div>
        <p style="color: #ff3e2a">${error_message}"</p>
    </div>
</c:if>

</body>
</html>
