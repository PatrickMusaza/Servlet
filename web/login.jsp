<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <div align="center">
        <h1>Login</h1>
        <form action="login" method="post">
            <table style="with: 100%">
                <tr>
                    <td>Username</td>
                    <td><input type="text" name="username" /></td>
                </tr>
                <tr>
                    <td>Password</td>
                    <td><input type="password" name="password" /></td>
                </tr>
            </table>
            <input type="submit" value="Login" />
        </form>
        <c:if test="${not empty error}">
            <div style="color: red;">${error}</div>
        </c:if>
    </div>
</body>
</html>