package server.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import context.ReflectionContext;
import server.dto.StateDto;
import server.services.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/checkName")
public class CheckNameServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        Object rawAttribute = servletContext.getAttribute("componentsContext");
        ReflectionContext reflectionContext = (ReflectionContext) rawAttribute;
        this.userService = reflectionContext.getComponent("server.services.UserServiceImpl");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        StateDto stateDto = userService.checkName(name);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(stateDto);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().write(json);
    }
}
