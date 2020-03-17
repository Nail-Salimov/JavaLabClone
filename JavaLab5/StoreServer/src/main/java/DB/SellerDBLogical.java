package DB;

import org.mindrot.jbcrypt.BCrypt;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SellerDBLogical implements SellerLogical {

    private static Connection connection;
    private static SellerDBLogical sellerLogical;

    private final String SEARCH_COUNT = "SELECT count FROM products WHERE name = ?";
    private final String PRODUCT_IS_EXIST = "SELECT 1 FROM products WHERE name = ?";
    private final String UPDATE_PRODUCT = "UPDATE products SET count = ? WHERE name = ?";
    private final String ADD_PRODUCT = "INSERT INTO products (name, count) VALUES (?, ?)";

    private final String DELETE_PRODUCT = "DELETE FROM products WHERE name = ?";

    private SellerDBLogical(String propertyPath) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(propertyPath));
            String name = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            String url = properties.getProperty("db.url");

            connection = DriverManager.getConnection(url, name, password);
        } catch (IOException | SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static SellerDBLogical getSellerDBLogical(String propertyPath) {
        return (sellerLogical == null) ? new SellerDBLogical(propertyPath) : sellerLogical;
    }


    public Integer getCount(String name) {

        try {
            PreparedStatement statement = connection.prepareStatement(SEARCH_COUNT);
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();
            Integer count = null;
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            statement.close();
            return count;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean productIsExist(String name) {
        try {
            PreparedStatement statement = connection.prepareStatement(PRODUCT_IS_EXIST);
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                statement.close();
                return true;
            } else {
                statement.close();
                return false;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void addProduct(String name, int count) {
        Integer number = getCount(name);
        if (number == null) {
            addNewProduct(name, count);
        } else if (number >= 0) {
            addOldProduct(name, count, number);
        } else {
            throw new IllegalStateException("Отрицательное количество товара");
        }
    }

    private void addOldProduct(String name, int count, int countNow) {
        try {
            PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT);
            statement.setInt(1, count + countNow);
            statement.setString(2, name);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException();
            }
            statement.close();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private void addNewProduct(String name, int count) {
        try {
            PreparedStatement statement = connection.prepareStatement(ADD_PRODUCT);
            statement.setInt(2, count);
            statement.setString(1, name);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException();
            }
            statement.close();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }


    private void deleteProductFromTable(String product) {
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT);
            statement.setString(1, product);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException();
            }
            statement.close();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean deleteProduct(String name) {
        boolean isExist = productIsExist(name);

        if (isExist) {
            deleteProductFromTable(name);
            return true;
        } else {
            return false;
        }
    }
}
