import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Supplier;

public class pgConnector {

    private static final String URL = "jdbc:postgresql://localhost/quiz";
    private static final String USERNAME = "quiz_user";
    private static final String PASSWORD = "quiz_password";
    private static final int MAX_POOL_SIZE = 10;

    private static DataSource getHikariDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(MAX_POOL_SIZE);
        return dataSource;
    }

    private static DataSource getC3P0DataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUser(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaxPoolSize(MAX_POOL_SIZE);
        return dataSource;
    }

    private static void testConnection(Supplier<DataSource> dataSourceSupplier) throws SQLException {
        DataSource dataSource = dataSourceSupplier.get();
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.setMaxRows(1);
            ResultSet resultSet = statement.executeQuery("select * from local_user");
            while(resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        testConnection(pgConnector::getHikariDataSource);
        testConnection(pgConnector::getC3P0DataSource);
    }
}
