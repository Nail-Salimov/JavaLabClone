import java.sql.*;

public class BdLogics {
    Connection connection;
    private final String INSERT = "insert into " +
            "image_info2 (nameimage, size) values (?, ?);";
    public BdLogics(String name, String password, String url) {
        try {
            this.connection = DriverManager.getConnection(url, name, password);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }


    public void save(String nameImage, long size) {

        try {
            PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, nameImage);
            statement.setLong(2, size);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
