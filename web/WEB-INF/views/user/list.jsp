<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Admin Dashboard | Task Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .action-btns .btn {
            padding: 0.25rem 0.5rem;
            font-size: 0.875rem;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">Task Manager</a>
            <div class="navbar-nav ms-auto">
                <span class="navbar-text me-3">Welcome, ${user.username} (Admin)</span>
                <a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row">
            <div class="col-md-3">
                <div class="card mb-4">
                    <div class="card-header bg-primary text-white">Quick Actions</div>
                    <div class="list-group list-group-flush">
                        <a href="${pageContext.request.contextPath}/task/new" 
                           class="list-group-item list-group-item-action">Create New Task</a>
                        <a href="${pageContext.request.contextPath}/user/new" 
                           class="list-group-item list-group-item-action">Add New User</a>
                        <a href="${pageContext.request.contextPath}/user/list" 
                           class="list-group-item list-group-item-action">Manage Users</a>
                    </div>
                </div>
            </div>

            <div class="col-md-9">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">All Users</h5>
                        <span class="badge bg-primary">${listUser.size()} users</span>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Username</th>
                                        <th>Email</th>
                                        <th>Role</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="user" items="${listUser}">
                                        <tr>
                                            <td>${user.id}</td>
                                            <td>${user.username}</td>
                                            <td>${user.email}</td>
                                            <td>
                                                <span class="badge ${user.role == 'admin' ? 'bg-danger' : 'bg-info'}">
                                                    ${user.role}
                                                </span>
                                            </td>
                                            <td class="action-btns">
                                                <a href="${pageContext.request.contextPath}/user/edit?id=${user.id}" 
                                                   class="btn btn-sm btn-outline-primary">Edit</a>
                                                <a href="${pageContext.request.contextPath}/user/delete?id=${user.id}" 
                                                   class="btn btn-sm btn-outline-danger" 
                                                   onclick="return confirm('Are you sure you want to delete this user?')">Delete</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>