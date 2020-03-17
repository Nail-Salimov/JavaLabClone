package server.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import context.ReflectionContext;
import server.dto.AllProductsPayload;
import server.services.ProductService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/store")
public class StoreServlet extends HttpServlet {

    private ProductService productService;
    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        mapper = new ObjectMapper();
        ServletContext servletContext = config.getServletContext();
        Object rawAttribute = servletContext.getAttribute("componentsContext");
        ReflectionContext reflectionContext = (ReflectionContext) rawAttribute;
        this.productService = reflectionContext.getComponent("server.services.ProductServiceImpl");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/store.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String product = req.getParameter("product");
        String count = req.getParameter("count");


        if (isDigit(count) && product != null){
            productService.sellProduct(product, Integer.parseInt(count));
        }

        String json = mapper.writeValueAsString(productService.listProducts());

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().write(json);
    }

    private boolean isDigit(String s){
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
