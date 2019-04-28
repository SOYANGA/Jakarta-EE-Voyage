<%--
  Created by IntelliJ IDEA.
  User: jiujiu糖
  Date: 2019/4/28
  Time: 12:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>主页</title>
</head>
<body>
<form method="post" action="/user/upload" enctype="multipart/form-data">
    <label for="head">设置头像</label>
    <input id="head" name="head" type="file" value="上传头像"/>
    <input type="submit" value="提交"/>
</form>
<c:if test="${head_data!=null}">
    <img src="${head_data}">
</c:if>
</body>
</html>
