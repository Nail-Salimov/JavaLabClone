package server.servlets;

import org.springframework.context.ApplicationContext;
import server.dto.client.UserDto;
import server.services.MailConfirmationServiceImpl;
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

@WebServlet("/confirm/check")
public class ConfirmationServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        ApplicationContext springContext = (ApplicationContext) context.getAttribute("springContext");
        userService = springContext.getBean(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = (String) req.getParameter("t");
        String userName = (String) req.getParameter("u");

        Optional<UserDto> optionalUserDto = userService.findUserByNameNotConfirmed(userName);

        if (optionalUserDto.isPresent()) {
            if(optionalUserDto.get().getToken().equals(token)) {
                userService.confirmed(optionalUserDto.get().getMail());
                resp.sendRedirect(req.getContextPath() + "/home");
                //req.getRequestDispatcher("/templates/home.ftl").forward(req, resp);
            } else {
                req.getRequestDispatcher("/templates/reg.ftl").forward(req, resp);
            }
        } else {
            req.getRequestDispatcher("/templates/reg.ftl").forward(req, resp);
        }
    }

}
