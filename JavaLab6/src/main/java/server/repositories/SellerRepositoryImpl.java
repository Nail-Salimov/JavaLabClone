package server.repositories;

import server.context.Component;
import server.propertysource.PropertySource;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class SellerRepositoryImpl implements SellerRepository, Component {
    private static Connection connection;
    private static SellerRepositoryImpl sellerRepository;

    private final String SEARCH_COUNT = "SELECT count FROM products WHERE name = ?";
    private final String PRODUCT_IS_EXIST = "SELECT 1 FROM products WHERE name = ?";
    private final String UPDATE_PRODUCT = "UPDATE products SET count = ? WHERE name = ?";
    private final String ADD_PRODUCT = "INSERT INTO products (name, count) VALUES (?, ?)";

    private final String DELETE_PRODUCT = "DELETE FROM products WHERE name = ?";

    private SellerRepositoryImpl(PropertySource source) {
        String propertyPath = source.getPropertyPath();
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

    public SellerRepositoryImpl() {
    }

    public static SellerRepositoryImpl getSellerDBLogical(PropertySource source) {
        sellerRepository = (sellerRepository == null) ? new SellerRepositoryImpl(source) : sellerRepository;
        return sellerRepository;
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

    @Override
    public void addUser(Long aLong, String password) {
        throw new IllegalStateException("Not usable");
    }

    @Override
    public String getName() {
        return "SellerRepository";
    }

    public static SellerRepository newFormation() {
        return new SellerRepositoryImpl();
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        SellerRepositoryImpl.connection = connection;
    }
}
