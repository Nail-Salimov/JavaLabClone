package server.servlet;

import di.context.ReflectionContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextServletListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        ReflectionContext context = new ReflectionContext("java");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        context.addConnection("/home/nail/Progy/JavaLab/JavaLab7/Servlet_With_Context/target/Servlet_With_Context-0.0.1/WEB-INF/classes/db.properties");
        context.buildProject();

        servletContext.setAttribute("componentsContext", context);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
