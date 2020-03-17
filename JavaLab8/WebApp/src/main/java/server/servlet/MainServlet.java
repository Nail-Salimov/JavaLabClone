package server.servlet;

import di.context.ReflectionContext;
import di.controller.Controller;
import di.controller.ControllerKit;
import di.controller.ReflectionCollector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class MainServlet extends HttpServlet {

    private ControllerKit kit;

    @Override
    public void init(ServletConfig config) throws ServletException {

        ServletContext servletContext = config.getServletContext();
        Object rawAttribute = servletContext.getAttribute("componentsContext");
        ReflectionContext reflectionContext = (ReflectionContext) rawAttribute;

        ReflectionCollector reflectionCollector = new ReflectionCollector("server");
        kit = reflectionCollector.collect();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        redirect(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        redirect(req, resp);
    }

    private void redirect(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURL() + ((req.getQueryString() != null) ? "?" + req.getQueryString() : "");
        String[] urlSplit = req.getRequestURL().toString().split("/");

        Controller controller = kit.getController("/" + urlSplit[urlSplit.length - 1]);

        if (controller != null) {
            controller.handle(req, resp);
        } else {
            resp.sendError(404);
        }
    }
}
