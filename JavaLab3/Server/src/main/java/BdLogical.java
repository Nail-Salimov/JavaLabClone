import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.Properties;

public class BdLogical {
    private static Connection connection;
    private static BdLogical bdLogical;

    private final String SEARCH = "SELECT 1 FROM serverClients WHERE name = ? and password = ?;";
    private final String INSERT_MESSAGE = "INSERT INTO messages(name, message, date ) VALUES (?, ?, ?);";

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


    public void addMessage(String name, String message) {
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT_MESSAGE);
            statement.setString(1, name);
            statement.setString(2, message);

            Date date = new Date();
            statement.setString(3, date.toString());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException();
            }
            statement.close();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean checkClient(String name, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement(SEARCH);
            statement.setString(1, name);
            statement.setString(2, password);

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
}
