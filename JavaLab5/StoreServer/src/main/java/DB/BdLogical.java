package DB;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mindrot.jbcrypt.BCrypt;
import org.omg.PortableInterceptor.INACTIVE;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class BdLogical {
    private static Connection connection;
    private static BdLogical bdLogical;

    private final String SEARCH_CLIENT = "SELECT * FROM clients WHERE id = ? ;";

    private final String SEARCH_COUNT = "SELECT count FROM products WHERE name = ?";
    private final String PRODUCT_IS_EXIST = "SELECT 1 FROM products WHERE name = ?";
    private final String UPDATE_PRODUCT = "UPDATE products SET count = ? WHERE name = ?";
    private final String ADD_PRODUCT = "INSERT INTO products (name, count) VALUES (?, ?)";
    private final String FIND_ALL_PRODUCTS = "SELECT * FROM products";

    private final String DELETE_PRODUCT = "DELETE FROM products WHERE name = ?";

    private BdLogical(String propertyPath) {
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

    public static BdLogical getBdLogical(String propertyPath) {
        return (bdLogical == null) ? new BdLogical(propertyPath) : bdLogical;
    }

    public boolean checkClient(int id, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement(SEARCH_CLIENT);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String hash = resultSet.getString("password");
                String resultSetRole = resultSet.getString("role");
                statement.close();
                return (BCrypt.checkpw(password, hash));
            } else {
                statement.close();
                return false;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getRole(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement(SEARCH_CLIENT);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String resultSetRole = resultSet.getString("role");
                statement.close();
                return resultSetRole;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
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

    public boolean productIsExist(String name) {
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

    public boolean buyProduct(String name, int count) {
        try {
            Integer countNow = getCount(name);
            System.out.println(countNow + " "+ count);
            if (countNow == null) {
                return false;

            } else if (countNow < count) {
                return false;
            } else {
                PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT);
                statement.setInt(1, countNow - count);
                statement.setString(2, name);

                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException();
                }
                if(countNow == count){
                    deleteProduct(name);
                }
                statement.close();
                return true;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Map<String, Integer> listProduct() {
        Map<String, Integer> map = new HashMap<>();
        try {
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_PRODUCTS);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int count = resultSet.getInt("count");
                map.put(name, count);
            }
            return map;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public void deleteProduct(String product){
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT);
            statement.setString(1, product);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException();
            }
            statement.close();
        } catch (SQLException e){
            throw  new IllegalStateException(e);
        }
    }
}
