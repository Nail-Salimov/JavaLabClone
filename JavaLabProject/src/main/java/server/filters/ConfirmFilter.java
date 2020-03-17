package server.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@WebFilter("/confirm/*")
public class ConfirmFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("id") == null || session.getAttribute("email") == null || session.getAttribute("name") == null){
            servletRequest.getServletContext().getRequestDispatcher("/reg").forward(request, response);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
