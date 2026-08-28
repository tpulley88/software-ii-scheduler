package SoftwareII.Utility;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**Method that allows user to communicate with database
 */
public class JDBC {

    //Declare variables
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";

    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL

    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = requireEnvironmentVariable("SCHEDULER_DB_USER");
    private static final String password = requireEnvironmentVariable("SCHEDULER_DB_PASSWORD");
    private static Connection connection = null;  // Connection Interface
    private static PreparedStatement preparedStatement;

    private static String requireEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(name + " must be configured before starting the application");
        }
        return value;
    }

    /**Establishes connection with SQL database
     */
    public static void makeConnection() {
        try {
              Class.forName(driver); // Locate Driver
              connection = DriverManager.getConnection(jdbcUrl, userName, password); // reference Connection object
              System.out.println("Connection successful!");
        }
        catch(ClassNotFoundException | SQLException e) {
              System.out.println("Error:" + e.getMessage());
              e.printStackTrace();
        }
    }

    /**Gets pre-established database connection
     * @return - Returns connection
     */
    public static Connection getConnection() {
        return connection;
    }

    /**Closes SQL database connection
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**Sets Prepared Statement to interact with SQL database
     * @param sqlStatement - SQL statement
     * @param conn - connection
     * @throws SQLException - Exception
     */
    public static void setPreparedStatement(String sqlStatement, Connection conn) throws SQLException {
        if (conn != null)
            preparedStatement = conn.prepareStatement(sqlStatement);
        else
            System.out.println("Prepared Statement Creation Failed!");
    }

    /**Gets Prepared Statement to interact with SQL database
     * @return - Returns prepared statement or returns null
     */
    public static PreparedStatement getPreparedStatement()  {
        if (preparedStatement != null)
            return preparedStatement;
        else {
            System.out.println("Null reference to Prepared Statement");
            return null;
        }
    }
}
