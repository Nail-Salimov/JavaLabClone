package DB;

import org.mindrot.jbcrypt.BCrypt;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class GeneralDBLogical implements GeneralLogical {
    private static Connection connection;
    private static GeneralDBLogical generalLogical;

    private final String SEARCH_CLIENT = "SELECT * FROM clients WHERE id = ? ;";

    private GeneralDBLogical(String propertyPath) {
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

    public static GeneralDBLogical getGeneralDBLogical(String propertyPath) {
        return (generalLogical == null) ? new GeneralDBLogical(propertyPath) : generalLogical;
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
}
