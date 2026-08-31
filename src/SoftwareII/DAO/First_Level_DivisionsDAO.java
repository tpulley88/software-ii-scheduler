package SoftwareII.DAO;

import SoftwareII.Model.First_Level_Divisions;
import SoftwareII.Utility.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**First_Level_DivisionsDAO retrieves and manipulates division information from SQL database.
 */
public class First_Level_DivisionsDAO {

    /**Creates a list of all divisions from SQL database
     * @return - returns list of all divisions
     * @throws SQLException - Exception
     */
    public static ObservableList<First_Level_Divisions> getAllDivisions() throws SQLException {

        ObservableList<First_Level_Divisions> divisionList = FXCollections.observableArrayList();

        String sqlStatement = "SELECT * FROM first_level_divisions";

        JDBC.setPreparedStatement(sqlStatement, JDBC.getConnection());

        PreparedStatement divisionListStatement = JDBC.getPreparedStatement();

        ResultSet rs = divisionListStatement.executeQuery();

        if (rs != null) {
            while (rs.next()) {
                int divisionID = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryID = rs.getInt("Country_ID");

                First_Level_Divisions addDivision = new First_Level_Divisions(divisionID, divisionName, countryID);

                divisionList.add(addDivision);
            }
        }
        return divisionList;
    }

}
