package SoftwareII.DAO;

import SoftwareII.Model.User;
import SoftwareII.Utility.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**UsersDAO retrieves and manipulates user information from SQL database.
 */
public class UsersDAO {

    /**Creates a list of all users from SQL database
     * @return - returns list of all users
     * @throws SQLException - Exception
     */
    public static ObservableList<User> getAllUsers() throws SQLException {

        ObservableList<User> userList = FXCollections.observableArrayList();

        String sqlStatement = "SELECT * FROM users";

        JDBC.setPreparedStatement(sqlStatement, JDBC.getConnection());

        PreparedStatement userListStatement = JDBC.getPreparedStatement();

        ResultSet rs = userListStatement.executeQuery();

        if (rs != null) {
            while (rs.next()) {

                int userID = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String userPassword = rs.getString("Password");

                User addUser = new User(userID, userName, userPassword);

                userList.add(addUser);
            }
        }
        return userList;
    }

    /**Takes username and returns user ID
     * @param username - username
     * @return - returns user ID
     * @throws SQLException - Exception
     */
    public static int getUserID(String username) throws SQLException {
        ObservableList<User> allUsers = getAllUsers();

        int convertedUserID = 0;

        for (User user : allUsers) {
            if (user.getUserName().matches(username)) {
                convertedUserID = user.getUserID();
            }
        }
        return convertedUserID;
    }
}
