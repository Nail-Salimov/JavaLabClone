package server.controller;


import di.controller.Controller;
import di.controller.Mapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Mapping("/test")
public class SimpleController implements Controller {

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();

        if (method.equals("GET")) {
            doGet(req, resp);
        } else if (method.equals("POST")) {
            doPost(req, resp);
        } else {
            throw new IllegalStateException("Не коррект тип запрса");
        }
    }

    private void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/test.jsp").forward(req, resp);
    }

    private void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain");

        resp.getWriter().write("Hi, " + name);
    }
}
