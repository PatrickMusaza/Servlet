package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.UserDAO;
import model.User;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    private UserDAO userDao;

    @Override
    public void init() throws ServletException {
        userDao = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is already logged in
        if (request.getSession(false) != null && request.getSession().getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/auth/signup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Basic validation
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            doGet(request, response);
            return;
        }

        try {
            // Check if username already exists
            if (userDao.userExists(username)) {
                request.setAttribute("error", "Username already exists");
                doGet(request, response);
                return;
            }

            // Create new user (default role is "user")
            User newUser = new User(username, password, email, "user");
            userDao.insertUser(newUser);

            // Automatically log in the new user
            request.getSession().setAttribute("user", newUser);
            response.sendRedirect(request.getContextPath() + "/user/dashboard");

        } catch (Exception e) {
            request.setAttribute("error", "Registration failed. Please try again.");
            doGet(request, response);
        }
    }
}