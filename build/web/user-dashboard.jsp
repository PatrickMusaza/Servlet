<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>User Dashboard | Task Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="#">Task Manager</a>
            <div class="navbar-nav ms-auto">
                <span class="navbar-text me-3">Welcome, ${user.username}</span>
                <a class="nav-link" href="logout">Logout</a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row">
            <div class="col-md-8 mx-auto">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">Your Tasks</h5>
                        <span class="badge bg-primary">${userTasks.size()} tasks</span>
                    </div>
                    <div class="card-body">
                        <c:if test="${empty userTasks}">
                            <div class="alert alert-info">No tasks assigned to you yet.</div>
                        </c:if>
                        
                        <c:forEach var="task" items="${userTasks}">
                            <div class="card mb-3">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between">
                                        <h5>${task.title}</h5>
                                        <span class="badge ${task.status == 'completed' ? 'bg-success' : task.status == 'in_progress' ? 'bg-warning' : 'bg-secondary'}">
                                            ${task.status}
                                        </span>
                                    </div>
                                    <p class="card-text">${task.description}</p>
                                    <div class="d-flex justify-content-end">
                                        <a href="task/edit?id=${task.id}" class="btn btn-sm btn-outline-primary me-2">Update Status</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>