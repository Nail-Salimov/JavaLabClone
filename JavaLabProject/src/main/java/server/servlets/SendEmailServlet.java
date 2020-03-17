package server.servlets;

import org.springframework.context.ApplicationContext;
import server.services.MailConfirmationService;
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
import javax.websocket.Session;
import java.io.IOException;

@WebServlet("/confirm/confirmation")
public class SendEmailServlet extends HttpServlet {
    private MailConfirmationService mailService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        ApplicationContext springContext = (ApplicationContext) context.getAttribute("springContext");
        userService = springContext.getBean(UserService.class);
        mailService = springContext.getBean(MailConfirmationService.class);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSession = req.getSession(false);
        String name = (String) httpSession.getAttribute("name");
        String mail = (String) httpSession.getAttribute("email");
        Long id = (Long) httpSession.getAttribute("id");

        String token = userService.getToken(id, name, mail);
        userService.addToken(name, token);

        //String url = "http://localhost:8080/confirm/check?t=" + token;

        mailService.send("Подтверждение", "pologies12314s@mail.ru", mail, name, mail, token, name);
        req.getRequestDispatcher("/templates/hello.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
