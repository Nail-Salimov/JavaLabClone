package server.servlets;

import context.ReflectionContext;
import server.dto.UserDto;
import server.services.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        Object rawAttribute = servletContext.getAttribute("componentsContext");
        ReflectionContext reflectionContext = (ReflectionContext) rawAttribute;
        this.userService = reflectionContext.getComponent("server.services.UserServiceImpl");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getServletContext().getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String password = req.getParameter("password");

        UserDto userDto = userService.checkClient(name, password);

        if (userDto != null){
            HttpSession session = req.getSession();
            session.setAttribute("Name", name);
            resp.sendRedirect(req.getContextPath() + "/store");
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }

    }
}
