<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
</head>
<body>
    <div align="center">
        <h1>Welcome Admin</h1>
        <a href="logout">Logout</a>
        <h2>Task Management</h2>
        <a href="task-form.jsp">Add New Task</a>
        <table border="1" cellpadding="5">
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Description</th>
                <th>Status</th>
                <th>Assigned To</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="task" items="${listTasks}">
                <tr>
                    <td><c:out value="${task.id}" /></td>
                    <td><c:out value="${task.title}" /></td>
                    <td><c:out value="${task.description}" /></td>
                    <td><c:out value="${task.status}" /></td>
                    <td><c:out value="${task.assignedTo}" /></td>
                    <td>
                        <a href="edit-task?id=<c:out value='${task.id}' />">Edit</a>
                        <a href="delete-task?id=<c:out value='${task.id}' />">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
        
        <h2>User Management</h2>
        <a href="user-form.jsp">Add New User</a>
        <table border="1" cellpadding="5">
            <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Email</th>
                <th>Role</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="user" items="${listUsers}">
                <tr>
                    <td><c:out value="${user.id}" /></td>
                    <td><c:out value="${user.username}" /></td>
                    <td><c:out value="${user.email}" /></td>
                    <td><c:out value="${user.role}" /></td>
                    <td>
                        <a href="edit-user?id=<c:out value='${user.id}' />">Edit</a>
                        <a href="delete-user?id=<c:out value='${user.id}' />">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</body>
</html>