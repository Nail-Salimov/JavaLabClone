package server.repositories;

import org.mindrot.jbcrypt.BCrypt;
import server.context.Component;
import server.models.User;
import server.propertysource.PropertySource;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import java.util.Properties;

public class GeneralRepositoryImpl implements GeneralRepository, Component {
    private static Connection connection;
    private static GeneralRepositoryImpl generalRepository;

    private final String SEARCH_CLIENT = "SELECT * FROM clients WHERE id = ? ;";

    private GeneralRepositoryImpl(PropertySource source) {
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

    public GeneralRepositoryImpl(){};

    public static GeneralRepositoryImpl getGeneralDBLogical(PropertySource source) {
        return (generalRepository == null) ? new GeneralRepositoryImpl(source) : generalRepository;
    }

    @Override
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

    @Override
    public Optional<User> getUser(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement(SEARCH_CLIENT);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String resultSetRole = resultSet.getString("role");
                Integer userId = resultSet.getInt("id");
                statement.close();
                return Optional.of(new User(userId, resultSetRole));
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void addUser(Long aLong, String password) {
        throw new IllegalStateException("Not usable");
    }

    @Override
    public String getName() {
        return "GeneralRepository";
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        GeneralRepositoryImpl.connection = connection;
    }

    public static GeneralRepository newFormation(){
        return new GeneralRepositoryImpl();
    }
}
