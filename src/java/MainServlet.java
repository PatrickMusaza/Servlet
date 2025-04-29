import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/")
public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDao;
    private TaskDAO taskDao;

    public void init() {
        userDao = new UserDAO();
        taskDao = new TaskDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        HttpSession session = request.getSession(false);

        try {
            // Public actions (no session required)
            if (action.equals("/login")) {
                login(request, response);
                return;
            }

            // Check if user is logged in
            if (session == null || session.getAttribute("user") == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            User currentUser = (User) session.getAttribute("user");
            String role = currentUser.getRole();

            // Common actions for all roles
            switch (action) {
                case "/logout":
                    logout(request, response);
                    break;
                case "/dashboard":
                    showDashboard(request, response, currentUser);
                    break;

                // User management (admin only)
                case "/user/new":
                    if (!"admin".equals(role)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                    showUserForm(request, response, null);
                    break;
                case "/user/insert":
                    if (!"admin".equals(role)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                    insertUser(request, response);
                    break;
                case "/user/delete":
                    if (!"admin".equals(role)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                    deleteUser(request, response);
                    break;
                case "/user/edit":
                    if (!"admin".equals(role)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                    showEditUserForm(request, response);
                    break;
                case "/user/update":
                    if (!"admin".equals(role)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                    updateUser(request, response);
                    break;
                case "/user/list":
                    if (!"admin".equals(role)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                    listUsers(request, response);
                    break;

                // Task management
                case "/task/new":
                    showTaskForm(request, response, null);
                    break;
                case "/task/insert":
                    insertTask(request, response, currentUser);
                    break;
                case "/task/delete":
                    deleteTask(request, response, currentUser);
                    break;
                case "/task/edit":
                    showEditTaskForm(request, response, currentUser);
                    break;
                case "/task/update":
                    updateTask(request, response, currentUser);
                    break;
                case "/task/list":
                    listTasks(request, response, currentUser);
                    break;

                default:
                    showDashboard(request, response, currentUser);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = userDao.validate(username, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("role", user.getRole());
                session.setAttribute("userId", user.getId());

                response.sendRedirect("dashboard");
            } else {
                request.setAttribute("error", "Invalid username or password");
                RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
                dispatcher.forward(request, response);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("login.jsp");
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws SQLException, ServletException, IOException {
        if ("admin".equals(currentUser.getRole())) {
            List<User> users = userDao.selectAllUsers();
            List<Task> tasks = taskDao.selectAllTasks();
            request.setAttribute("listUser", users);
            request.setAttribute("listTask", tasks);
            RequestDispatcher dispatcher = request.getRequestDispatcher("admin-dashboard.jsp");
            dispatcher.forward(request, response);
        } else {
            List<Task> tasks = taskDao.selectUserTasks(currentUser.getId());
            request.setAttribute("userTasks", tasks);
            RequestDispatcher dispatcher = request.getRequestDispatcher("user-dashboard.jsp");
            dispatcher.forward(request, response);
        }
    }

    // User management methods
    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<User> listUser = userDao.selectAllUsers();
        request.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
        dispatcher.forward(request, response);
    }

    private void showUserForm(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        request.setAttribute("user", user);
        request.setAttribute("action", "insert");
        RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditUserForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = userDao.selectUser(id);
        request.setAttribute("user", existingUser);
        request.setAttribute("action", "update");
        RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
        dispatcher.forward(request, response);
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String role = request.getParameter("role");

        User newUser = new User(username, password, email, role);
        userDao.insertUser(newUser);
        response.sendRedirect("user/list");
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String role = request.getParameter("role");

        User user = new User(id, username, password, email, role);
        userDao.updateUser(user);
        response.sendRedirect("user/list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDao.deleteUser(id);
        response.sendRedirect("user/list");
    }

    // Task management methods
    private void listTasks(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws SQLException, IOException, ServletException {
        List<Task> tasks;
        if ("admin".equals(currentUser.getRole())) {
            tasks = taskDao.selectAllTasks();
        } else {
            tasks = taskDao.selectUserTasks(currentUser.getId());
        }
        request.setAttribute("listTask", tasks);
        RequestDispatcher dispatcher = request.getRequestDispatcher("task-list.jsp");
        dispatcher.forward(request, response);
    }

    private void showTaskForm(HttpServletRequest request, HttpServletResponse response, Task task)
            throws ServletException, IOException {
        List<User> users = userDao.selectAllUsers();
        request.setAttribute("users", users);
        request.setAttribute("task", task);
        request.setAttribute("action", "insert");
        RequestDispatcher dispatcher = request.getRequestDispatcher("task-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditTaskForm(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Task existingTask = taskDao.selectTask(id);

        // Only allow editing if admin or task is assigned to current user
        if (!"admin".equals(currentUser.getRole()) && existingTask.getAssignedTo() != currentUser.getId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        List<User> users = userDao.selectAllUsers();
        request.setAttribute("users", users);
        request.setAttribute("task", existingTask);
        request.setAttribute("action", "update");
        RequestDispatcher dispatcher = request.getRequestDispatcher("task-form.jsp");
        dispatcher.forward(request, response);
    }

    private void insertTask(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws SQLException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String status = request.getParameter("status");
        int assignedTo = Integer.parseInt(request.getParameter("assignedTo"));

        Task newTask = new Task(title, description, status, assignedTo, currentUser.getId());
        taskDao.insertTask(newTask);
        response.sendRedirect("task/list");
    }

    private void updateTask(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String status = request.getParameter("status");
        int assignedTo = Integer.parseInt(request.getParameter("assignedTo"));

        Task task = new Task(id, title, description, status, assignedTo, currentUser.getId());

        // Verify ownership before update
        Task existingTask = taskDao.selectTask(id);
        if (!"admin".equals(currentUser.getRole()) && existingTask.getAssignedTo() != currentUser.getId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        taskDao.updateTask(task);
        response.sendRedirect("task/list");
    }

    private void deleteTask(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        // Verify ownership before delete
        Task existingTask = taskDao.selectTask(id);
        if (!"admin".equals(currentUser.getRole()) && existingTask.getAssignedTo() != currentUser.getId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        taskDao.deleteTask(id);
        response.sendRedirect("task/list");
    }
}