package filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import model.User;

@WebFilter("/*")
public class SecurityFilter implements Filter {

    private static final String[] PUBLIC_PATHS = {
        "/login",
        "/signup",};

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI().substring(
                request.getContextPath().length()
        );

        if (path.equals("/admin/dashboard")) {
            System.out.println("Allowing admin dashboard access");
        }

        // Check if path is public
        for (String publicPath : PUBLIC_PATHS) {
            if (path.startsWith(publicPath)) {
                chain.doFilter(request, response);
                return;
            }
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // For logged-in users
        User user = (User) session.getAttribute("user");

        // Special case for dashboard
        if (path.equals("/dashboard") || path.equals("/user/dashboard") || path.equals("/admin/dashboard")) {
            chain.doFilter(request, response);
            return;
        }

        // Admin paths check
        if (path.startsWith("/admin/") && !"admin".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
