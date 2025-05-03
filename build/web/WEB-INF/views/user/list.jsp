<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Admin Dashboard | Task Manager</title>
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <style>
      .action-btns .btn {
        padding: 0.25rem 0.5rem;
        font-size: 0.875rem;
        margin-right: 5px;
      }
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
      .highlight-menu-item {
        background-color: #0d6efd !important;
        color: white !important;
      }
    </style>
  </head>
  <body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/"
          >Task Manager</a
        >
        <div class="navbar-nav ms-auto">
          <span class="navbar-text me-3"
            >Welcome, ${user.username} (Admin)</span
          >
          <a class="nav-link" href="${pageContext.request.contextPath}/logout"
            >Logout</a
          >
        </div>
      </div>
    </nav>

    <div class="container mt-4">
      <div class="row">
        <div class="col-md-3">
          <div class="card mb-4">
            <div class="card-header bg-primary text-white">Quick Actions</div>
            <div class="list-group list-group-flush">
              <a
                href="${pageContext.request.contextPath}/task/new"
                class="list-group-item list-group-item-action"
                >Create New Task</a
              >
              <a
                href="${pageContext.request.contextPath}/user/new"
                class="list-group-item list-group-item-action highlight-menu-item"
              >
                <i class="fas fa-plus-circle"></i> Add New User
              </a>
              <a
                href="${pageContext.request.contextPath}/user/list"
                class="list-group-item list-group-item-action"
                >Manage Users</a
              >
            </div>
          </div>
        </div>

        <div class="col-md-9">
          <div class="card">
            <div class="card-header">
              <h5 class="mb-0">All Users</h5>
              <div>
                <span class="badge bg-primary me-2"
                  >${listUser.size()} users</span
                >
                <a
                  href="${pageContext.request.contextPath}/user/new"
                  class="btn btn-success btn-sm"
                >
                  <i class="fas fa-plus"></i> Add New User
                </a>
              </div>
            </div>
            <div class="card-body">
              <c:choose>
                <c:when test="${empty listUser}">
                  <div class="alert alert-info">No users found</div>
                </c:when>
                <c:otherwise>
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
                              <span
                                class="badge ${user.role == 'admin' ? 'bg-danger' : 'bg-info'}"
                              >
                                ${user.role}
                              </span>
                            </td>
                            <td class="action-btns">
                              <a
                                href="${pageContext.request.contextPath}/user/edit?id=${user.id}"
                                class="btn btn-sm btn-outline-primary"
                              >
                                <i class="fas fa-edit"></i> Edit
                              </a>
                              <a
                                href="${pageContext.request.contextPath}/user/delete?id=${user.id}"
                                class="btn btn-sm btn-outline-danger"
                                onclick="return confirm('Are you sure you want to delete this user?')"
                              >
                                <i class="fas fa-trash-alt"></i> Delete
                              </a>
                            </td>
                          </tr>
                        </c:forEach>
                      </tbody>
                    </table>
                  </div>
                </c:otherwise>
              </c:choose>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Font Awesome for icons -->
    <script
      src="https://kit.fontawesome.com/a076d05399.js"
      crossorigin="anonymous"
    ></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>
