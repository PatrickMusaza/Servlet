<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Login | Task Manager</title>
        <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
            rel="stylesheet"
            />
        <base href="${pageContext.request.contextPath}/" />
    </head>
    <body class="bg-light">
        <div class="container">
            <div class="row justify-content-center mt-5">
                <div class="col-md-6 col-lg-4">
                    <div class="card shadow">
                        <div class="card-body p-4">
                            <div class="text-center mb-4">
                                <h2 class="card-title">Task Manager</h2>
                                <p class="text-muted">Please sign in to continue</p>
                            </div>

                            <form action="login" method="post">
                                <div class="mb-3">
                                    <label for="username" class="form-label">Username</label>
                                    <input
                                        type="text"
                                        class="form-control"
                                        id="username"
                                        name="username"
                                        required
                                        />
                                </div>
                                <div class="mb-3">
                                    <label for="password" class="form-label">Password</label>
                                    <input
                                        type="password"
                                        class="form-control"
                                        id="password"
                                        name="password"
                                        required
                                        />
                                </div>
                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-primary">Sign in</button>
                                </div>
                            </form>

                            <c:if test="${not empty error}">
                                <div class="alert alert-danger mt-3">${error}</div>
                            </c:if>
                            <div class="text-center mt-3">
                                <p>Don't have an account? <a href="${pageContext.request.contextPath}/signup">Sign Up</a></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
