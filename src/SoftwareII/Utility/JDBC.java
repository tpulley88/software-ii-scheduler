package SoftwareII.Utility;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.h2.tools.RunScript;

/**Method that allows user to communicate with database
 */
public class JDBC {

    //Declare variables
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";

    private static Connection connection = null;  // Connection Interface
    private static PreparedStatement preparedStatement;

    public static boolean isDemoMode() {
        return isBlank(System.getenv("SCHEDULER_DB_USER")) ||
                isBlank(System.getenv("SCHEDULER_DB_PASSWORD"));
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /**Establishes connection with SQL database
     */
    public static void makeConnection() {
        try {
              if (isDemoMode()) {
                  Class.forName("org.h2.Driver");
                  connection = DriverManager.getConnection(
                          "jdbc:h2:mem:client_schedule;MODE=MySQL;DB_CLOSE_DELAY=-1;NON_KEYWORDS=START,END,TYPE",
                          "sa",
                          "");
                  RunScript.execute(connection, new InputStreamReader(
                          JDBC.class.getResourceAsStream("/SoftwareII/Utility/demo-schema.sql"),
                          StandardCharsets.UTF_8));
                  System.out.println("Connected to fictional in-memory demo database.");
              } else {
                  Class.forName("com.mysql.cj.jdbc.Driver");
                  String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone=SERVER";
                  connection = DriverManager.getConnection(
                          jdbcUrl,
                          System.getenv("SCHEDULER_DB_USER"),
                          System.getenv("SCHEDULER_DB_PASSWORD"));
                  System.out.println("Connected to configured MySQL database.");
              }
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
            if (connection != null) connection.close();
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
