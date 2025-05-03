package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import dao.TaskDAO;
import dao.UserDAO;
import model.Task;
import model.User;

@WebServlet(urlPatterns = {
        "/user/*",
        "/task/*",
        "/admin/*",
        "/dashboard"
})
public class MainServlet extends HttpServlet {

    private UserDAO userDao;
    private TaskDAO taskDao;

    @Override
    public void init() throws ServletException {
        userDao = new UserDAO();
        taskDao = new TaskDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        HttpSession session = request.getSession(false);
        User currentUser = session != null ? (User) session.getAttribute("user") : null;

        try {
            if (path.equals("/dashboard") || path.equals("/admin/dashboard") || path.equals("/user/dashboard")) {
                if (currentUser == null) {
                    response.sendRedirect(request.getContextPath() + "/login");
                    return;
                }
                showDashboard(request, response, currentUser);
                return;
            }

            if (path.startsWith("/user/")) {
                handleUserRoutes(path, request, response, currentUser);
            } else if (path.startsWith("/task/")) {
                handleTaskRoutes(path, request, response, currentUser);
            } else if (path.startsWith("/admin/")) {
                handleAdminRoutes(path, request, response, currentUser);
            } else {
                if (currentUser != null) {
                    response.sendRedirect(getDashboardPath(currentUser));
                } else {
                    response.sendRedirect(request.getContextPath() + "/login");
                }
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response,
            User currentUser) throws SQLException, ServletException, IOException {
        if ("admin".equals(currentUser.getRole())) {
            List<User> users = userDao.selectAllUsers();
            List<Task> tasks = taskDao.selectAllTasks();
            int userCount = userDao.selectUserCount();
            int taskCompleted = taskDao.selectTaskCompletedCount();
            int taskActive = taskDao.selectActiveCount();
            request.setAttribute("listUser", users);
            request.setAttribute("task", tasks);
            request.setAttribute("userCount", userCount);
            request.setAttribute("taskCompleted", taskCompleted);
            request.setAttribute("taskActive", taskActive);
            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
        } else {
            List<Task> tasks = taskDao.selectUserTasks(currentUser.getId());
            request.setAttribute("userTasks", tasks);
            request.getRequestDispatcher("/WEB-INF/views/user/dashboard.jsp").forward(request, response);
        }
    }

    private void handleUserRoutes(String path, HttpServletRequest request,
            HttpServletResponse response, User currentUser)
            throws ServletException, IOException, SQLException {
        switch (path) {
            case "/user/new":
                showUserForm(request, response, null, "insert");
                break;
            case "/user/edit":
                showEditUserForm(request, response);
                break;
            case "/user/list":
                listUsers(request, response);
                break;
            case "/user/insert":
                insertUser(request, response);
                break;
            case "/user/update":
                updateUser(request, response);
                break;
            case "/user/delete":
                deleteUser(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleTaskRoutes(String path, HttpServletRequest request,
            HttpServletResponse response, User currentUser)
            throws ServletException, IOException, SQLException {
        String dashboardPath = getDashboardPath(currentUser);

        switch (path) {
            case "/task/new":
                showTaskForm(request, response, null, "insert");
                break;
            case "/task/edit":
                showEditTaskForm(request, response, currentUser);
                break;
            case "/task/list":
                listTask(request, response, dashboardPath);
                break;
            case "/task/insert":
                insertTask(request, response, currentUser, dashboardPath);
                break;
            case "/task/update":
                updateTask(request, response, currentUser, dashboardPath);
                break;
            case "/task/delete":
                deleteTask(request, response, currentUser, dashboardPath);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleAdminRoutes(String path, HttpServletRequest request,
            HttpServletResponse response, User currentUser)
            throws ServletException, IOException, SQLException {
        if (!"admin".equals(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private String getDashboardPath(User user) {
        return "admin".equals(user.getRole()) ? "/admin/dashboard" : "/user/dashboard";
    }

    private void showUserForm(HttpServletRequest request, HttpServletResponse response,
            User user, String action) throws ServletException, IOException {
        List<User> users = userDao.selectAllUsers();
        request.setAttribute("user", user);
        request.setAttribute("action", action);
        request.getRequestDispatcher("/WEB-INF/views/user/form.jsp").forward(request, response);
    }

    private void showEditUserForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = userDao.selectUser(id);
        showUserForm(request, response, existingUser, "update");
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<User> users = userDao.selectAllUsers();
        request.setAttribute("users", users);

        // Check for messages
        String message = (String) request.getSession().getAttribute("message");
        if (message != null) {
            request.setAttribute("message", message);
            request.getSession().removeAttribute("message");
        }

        request.getRequestDispatcher("/WEB-INF/views/admin/user.jsp").forward(request, response);
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        // Extract user data from request
        User newUser = extractUserFromRequest(request);

        // Validate required fields
        if (newUser.getUsername() == null || newUser.getUsername().isEmpty() ||
                newUser.getPassword() == null || newUser.getPassword().isEmpty() ||
                newUser.getEmail() == null || newUser.getEmail().isEmpty()) {

            // Store error message and redirect back to form
            request.getSession().setAttribute("error", "All fields are required");
            response.sendRedirect(request.getContextPath() + "/user/new");
            return;
        }

        // Check if username already exists
        if (userDao.userExists(newUser.getUsername())) {
            request.getSession().setAttribute("error", "Username already exists");
            response.sendRedirect(request.getContextPath() + "/user/new");
            return;
        }

        // Insert the user
        boolean success = userDao.insertUser(newUser);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/user/list");
        } else {
            request.getSession().setAttribute("error", "Failed to create user");
            response.sendRedirect(request.getContextPath() + "/user/new");
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        User user = extractUserFromRequest(request);
        user.setId(Integer.parseInt(request.getParameter("id")));
        userDao.updateUser(user);
        response.sendRedirect("list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDao.deleteUser(id);
        response.sendRedirect("list");
    }

    private User extractUserFromRequest(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        return new User(username, password, email, role);
    }

    private void showTaskForm(HttpServletRequest request, HttpServletResponse response,
            Task task, String action) throws ServletException, IOException, SQLException {
        List<User> users = userDao.selectAllUsers();
        request.setAttribute("users", users);
        request.setAttribute("task", task);
        request.setAttribute("action", action);
        request.getRequestDispatcher("/WEB-INF/views/task/form.jsp").forward(request, response);
    }

    private void showEditTaskForm(HttpServletRequest request, HttpServletResponse response,
            User currentUser) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Task existingTask = taskDao.selectTask(id);

        if (!"admin".equals(currentUser.getRole()) && existingTask.getAssignedTo() != currentUser.getId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        showTaskForm(request, response, existingTask, "update");
    }

    private void listTask(HttpServletRequest request, HttpServletResponse response,
            String redirectPath) throws SQLException, IOException, ServletException {
        List<Task> tasks = taskDao.selectAllTasks();
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("/WEB-INF/views/admin/task.jsp").forward(request, response);
    }

    private void insertTask(HttpServletRequest request, HttpServletResponse response,
            User currentUser, String redirectPath) throws SQLException, IOException {
        Task newTask = extractTaskFromRequest(request);
        newTask.setCreatedBy(currentUser.getId());
        taskDao.insertTask(newTask);
        response.sendRedirect(request.getContextPath() + redirectPath);
    }

    private void updateTask(HttpServletRequest request, HttpServletResponse response,
            User currentUser, String redirectPath) throws SQLException, IOException {
        Task task = extractTaskFromRequest(request);
        task.setId(Integer.parseInt(request.getParameter("id")));

        Task existingTask = taskDao.selectTask(task.getId());
        if (!"admin".equals(currentUser.getRole()) && existingTask.getAssignedTo() != currentUser.getId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        taskDao.updateTask(task);
        response.sendRedirect(request.getContextPath() + redirectPath);
    }

    private void deleteTask(HttpServletRequest request, HttpServletResponse response,
            User currentUser, String redirectPath) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Task existingTask = taskDao.selectTask(id);

        if (!"admin".equals(currentUser.getRole()) && existingTask.getAssignedTo() != currentUser.getId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        taskDao.deleteTask(id);
        response.sendRedirect(request.getContextPath() + redirectPath);
    }

    private Task extractTaskFromRequest(HttpServletRequest request) {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String status = request.getParameter("status");
        int assignedTo = Integer.parseInt(request.getParameter("assignedTo"));
        return new Task(title, description, status, assignedTo, 0);
    }
}