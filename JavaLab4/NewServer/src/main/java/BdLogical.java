import com.fasterxml.jackson.databind.ObjectMapper;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.Properties;

public class BdLogical {
    private static Connection connection;
    private static BdLogical bdLogical;

    private final String SEARCH = "SELECT * FROM serverClients WHERE name = ? ;";
    private final String INSERT_MESSAGE = "INSERT INTO messages(name, message, date ) VALUES (?, ?, ?);";
    private final String SEARCH_HISTORY = "SELECT * FROM messages LIMIT ? OFFSET ?";
    private final String SIZE_HISTORY = "SELECT COUNT(1) FROM messages";

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

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String hash = resultSet.getString("password");
                statement.close();
                return BCrypt.checkpw(password, hash);
            } else {
                statement.close();
                return false;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public MessageHistory messageHistory(int page, int size) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            PreparedStatement statement = connection.prepareStatement(SIZE_HISTORY);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            int count = resultSet.getInt(1);
            if (count >= page * size) {
                statement = connection.prepareStatement(SEARCH_HISTORY);
                statement.setInt(1, size);
                statement.setInt(2, page * (size - 1) + 1);
                resultSet = statement.executeQuery();

                MessageHistory history = new MessageHistory("History", "success", size);
                while (resultSet.next()) {
                    history.add(resultSet.getString("name"), resultSet.getString("message"));
                }
                return history;
            } else {
                MessageHistory historyException = new MessageHistory("History", "failure", 0);
                return  historyException;
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
