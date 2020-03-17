package server.servlets;

import org.springframework.context.ApplicationContext;
import server.dto.client.UserDto;
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
import java.util.Optional;

@WebServlet("/reg")
public class RegistrationServlet extends HttpServlet {

    private UserService service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        ApplicationContext springContext = (ApplicationContext) context.getAttribute("springContext");
        service = springContext.getBean(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/templates/reg.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        if (service.save(name, password, email)) {
            HttpSession httpSession = req.getSession();
            httpSession.setAttribute("email", email);
            httpSession.setAttribute("name", name);
            UserDto userDto = service.findByNameAndEmailNotConfirmed(name, email).get();
            httpSession.setAttribute("id", userDto.getId());

            resp.sendRedirect(req.getContextPath() + "/confirm/confirmation");
        } else {
           doGet(req, resp);
        }
    }
}
