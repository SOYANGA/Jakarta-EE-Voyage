<%--
  Created by IntelliJ IDEA.   Veiw视图处理
  User: jiujiu糖
  Date: 2019/4/25
  Time: 15:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Spring MVC</title>
</head>
<body>
<%--<h1>当前时间${greetting_message}</h1>--%>
<%--<a href="index.html">HelloWeb</a>--%>

<form action="/user/login" method="post">
    <label for="username">用户名：</label> <input id="username" type='text' name="username" value=""
                                              placeholder="请输入用户名">
    <br>
    <label for="password">密&nbsp&nbsp&nbsp码：</label> <input id="password" type='text' name="password" value=""
                                                            placeholder="请输入密码">
    <br>

    <input type="submit" value="登陆">
</form>

</body>
</html>
