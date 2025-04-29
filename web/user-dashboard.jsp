<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Dashboard</title>
</head>
<body>
    <div align="center">
        <h1>Welcome <c:out value="${user.username}" /></h1>
        <a href="logout">Logout</a>
        <h2>Your Tasks</h2>
        <table border="1" cellpadding="5">
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Description</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="task" items="${userTasks}">
                <tr>
                    <td><c:out value="${task.id}" /></td>
                    <td><c:out value="${task.title}" /></td>
                    <td><c:out value="${task.description}" /></td>
                    <td><c:out value="${task.status}" /></td>
                    <td>
                        <a href="edit-task?id=<c:out value='${task.id}' />">Update Status</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</body>
</html>