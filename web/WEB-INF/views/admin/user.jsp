<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>User Management | Task Manager</title>
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
      .action-btn {
        width: 30px;
        height: 30px;
        padding: 0;
        display: inline-flex;
        align-items: center;
        justify-content: center;
      }
      .role-admin {
        background-color: #f8d7da;
      }
      .role-user {
        background-color: #e7f1ff;
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
                  class="nav-link"
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
                  class="nav-link active"
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
            <h1 class="h2"><i class="fas fa-users me-2"></i>User Management</h1>
            <div class="btn-toolbar mb-2 mb-md-0">
              <a
                href="${pageContext.request.contextPath}/user/new"
                class="btn btn-primary"
              >
                <i class="fas fa-plus me-1"></i> New User
              </a>
            </div>
          </div>

          <div class="card">
            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-hover">
                  <thead class="table-light">
                    <tr>
                      <th>ID</th>
                      <th>Username</th>
                      <th>Email</th>
                      <th>Role</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach var="user" items="${users}">
                      <tr
                        class="${user.role == 'admin' ? 'role-admin' : 'role-user'}"
                      >
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                        <td>
                          <span
                            class="badge ${user.role == 'admin' ? 'bg-info' : 'bg-warning'}"
                          >
                            ${user.role}
                          </span>
                        </td>
                        <td>
                          <a
                            href="${pageContext.request.contextPath}/user/edit?id=${user.id}"
                            class="btn btn-sm btn-outline-primary action-btn"
                            title="Edit"
                          >
                            <i class="fas fa-edit"></i>
                          </a>
                          <a
                            href="${pageContext.request.contextPath}/user/delete?id=${user.id}"
                            class="btn btn-sm btn-outline-danger action-btn"
                            title="Delete"
                            onclick="return confirm('Are you sure you want to delete this user?')"
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
