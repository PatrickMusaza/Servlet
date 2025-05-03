<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>
        <c:choose>
            <c:when test="${not empty task.id}">Edit Task</c:when>
            <c:otherwise>Create New Task</c:otherwise>
        </c:choose> | Task Manager
    </title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <div class="card">
            <div class="card-header bg-primary text-white">
                <h4>
                    <c:choose>
                        <c:when test="${not empty task.id}">Edit Task #${task.id}</c:when>
                        <c:otherwise>Create New Task</c:otherwise>
                    </c:choose>
                </h4>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/task/${not empty task.id ? 'update' : 'insert'}" method="post">
                    <c:if test="${not empty task.id}">
                        <input type="hidden" name="id" value="${task.id}">
                    </c:if>
                    <c:if test="${not empty redirect}">
                        <input type="hidden" name="redirect" value="${redirect}">
                    </c:if>
                    
                    <div class="mb-3">
                        <label for="title" class="form-label">Title</label>
                        <input type="text" class="form-control" id="title" name="title" 
                               value="${task.title}" required>
                    </div>
                    
                    <div class="mb-3">
                        <label for="description" class="form-label">Description</label>
                        <textarea class="form-control" id="description" name="description" 
                                  rows="3">${task.description}</textarea>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="status" class="form-label">Status</label>
                            <select class="form-select" id="status" name="status" required>
                                <option value="pending" ${task.status == 'pending' ? 'selected' : ''}>Pending</option>
                                <option value="in_progress" ${task.status == 'in_progress' ? 'selected' : ''}>In Progress</option>
                                <option value="completed" ${task.status == 'completed' ? 'selected' : ''}>Completed</option>
                            </select>
                        </div>
                        
                        <div class="col-md-6 mb-3">
                            <label for="assignedTo" class="form-label">Assigned To</label>
                            <select class="form-select" id="assignedTo" name="assignedTo" required>
                                <c:forEach var="user" items="${users}">
                                    <option value="${user.id}" 
                                        ${task.assignedTo == user.id ? 'selected' : ''}>
                                        ${user.username}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    
                    <div class="d-flex justify-content-between">
                        <a href="${pageContext.request.contextPath}/${not empty redirect ? redirect : 'task/list'}" 
                           class="btn btn-secondary">Cancel</a>
                        <button type="submit" class="btn btn-primary">
                            <c:choose>
                                <c:when test="${not empty task.id}">Update Task</c:when>
                                <c:otherwise>Create Task</c:otherwise>
                            </c:choose>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>