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
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
    />
    <style>
      .sidebar {
        min-height: 100vh;
        background: #343a40;
      }
      .sidebar .nav-link {
        color: rgba(255, 255, 255, 0.5);
        border-radius: 0.25rem;
        margin: 0.25rem 0;
      }
      .sidebar .nav-link:hover,
      .sidebar .nav-link.active {
        color: white;
        background: rgba(255, 255, 255, 0.1);
      }
      .sidebar .nav-link i {
        width: 20px;
        margin-right: 10px;
        text-align: center;
      }
      .card-stat {
        transition: transform 0.3s;
      }
      .card-stat:hover {
        transform: translateY(-5px);
      }
    </style>
  </head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <!-- Sidebar -->
        <div class="col-md-3 col-lg-2 d-md-block sidebar bg-dark">
          <div class="position-sticky pt-3">
            <ul class="nav flex-column">
              <li class="nav-item">
                <a
                  class="nav-link active"
                  href="${pageContext.request.contextPath}/admin/dashboard"
                >
                  <i class="fas fa-tachometer-alt"></i> Dashboard
                </a>
              </li>
              <li class="nav-item">
                <a
                  class="nav-link"
                  href="${pageContext.request.contextPath}/task/list"
                >
                  <i class="fas fa-tasks"></i> Manage Tasks
                </a>
              </li>
              <li class="nav-item">
                <a
                  class="nav-link"
                  href="${pageContext.request.contextPath}/user/list"
                >
                  <i class="fas fa-users"></i> Manage Users
                </a>
              </li>
            </ul>
          </div>
        </div>

        <!-- Main Content -->
        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
          <div
            class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom"
          >
            <h1 class="h2">
              <i class="fas fa-tachometer-alt me-2"></i>Dashboard
            </h1>
            <div class="btn-toolbar mb-2 mb-md-0">
              <span class="navbar-text me-3">
                Welcome, ${user.username}
                <span class="badge bg-danger">Admin</span>
              </span>
              <a
                class="btn btn-sm btn-outline-secondary"
                href="${pageContext.request.contextPath}/logout"
              >
                <i class="fas fa-sign-out-alt"></i> Logout
              </a>
            </div>
          </div>

          <!-- Stats Cards -->
          <div class="row mb-4">
            <div class="col-md-4">
              <div class="card card-stat bg-primary text-white">
                <div class="card-body">
                  <div class="d-flex justify-content-between">
                    <div>
                      <h5 class="card-title">Total Users</h5>
                      <h2 class="mb-0">${userCount}</h2>
                    </div>
                    <i class="fas fa-users fa-3x opacity-50"></i>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-md-4">
              <div class="card card-stat bg-success text-white">
                <div class="card-body">
                  <div class="d-flex justify-content-between">
                    <div>
                      <h5 class="card-title">Active Tasks</h5>
                      <h2 class="mb-0">${taskActive}</h2>
                    </div>
                    <i class="fas fa-tasks fa-3x opacity-50"></i>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-md-4">
              <div class="card card-stat bg-info text-white">
                <div class="card-body">
                  <div class="d-flex justify-content-between">
                    <div>
                      <h5 class="card-title">Completed Tasks</h5>
                      <h2 class="mb-0">${taskCompleted}</h2>
                    </div>
                    <i class="fas fa-check-circle fa-3x opacity-50"></i>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Recent Tasks -->
          <div class="card mb-4">
            <div class="card-header">
              <div class="d-flex justify-content-between align-items-center">
                <h5 class="mb-0">
                  <i class="fas fa-history me-2"></i>Recent Tasks
                </h5>
                <a
                  href="${pageContext.request.contextPath}/task/new"
                  class="btn btn-sm btn-primary"
                >
                  <i class="fas fa-plus"></i> New Task
                </a>
              </div>
            </div>
            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-hover">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Title</th>
                      <th>Assigned To</th>
                      <th>Status</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach var="task" items="${task}">
                      <tr>
                        <td>${task.id}</td>
                        <td>${task.title}</td>
                        <td>
                          <c:forEach var="user" items="${listUser}">
                            <c:if test="${user.id == task.assignedTo}">
                              ${user.username}
                            </c:if>
                          </c:forEach>
                        </td>
                        <td>
                          <span
                            class="badge ${task.status == 'completed' ? 'bg-success' : task.status == 'in_progress' ? 'bg-warning' : 'bg-secondary'}"
                          >
                            ${task.status}
                          </span>
                        </td>
                        <td>
                          <a
                            href="${pageContext.request.contextPath}/task/edit?id=${task.id}"
                            class="btn btn-sm btn-outline-primary"
                          >
                            <i class="fas fa-edit"></i>
                          </a>
                          <a
                            href="${pageContext.request.contextPath}/task/delete?id=${task.id}"
                            class="btn btn-sm btn-outline-danger"
                            onclick="return confirm('Are you sure?')"
                          >
                            <i class="fas fa-trash"></i>
                          </a>
                        </td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>
