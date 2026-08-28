package SoftwareII.DAO;

import SoftwareII.Model.Country;
import SoftwareII.Utility.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**CountriesDAO retrieves and manipulates country information from SQL database.
 */
public class CountriesDAO {

    /**Creates a list of all countries from SQL database.
     * @return - returns list of all countries
     * @throws SQLException - Exception
     */
    public static ObservableList<Country> getAllCountries() throws SQLException {

        ObservableList<Country> countryList = FXCollections.observableArrayList();

        String sqlStatement = "SELECT * FROM countries";

        JDBC.setPreparedStatement(sqlStatement, JDBC.getConnection());

        PreparedStatement countryListStatement = JDBC.getPreparedStatement();

        countryListStatement.execute(sqlStatement);

        ResultSet rs = countryListStatement.getResultSet();

        if (rs != null) {
            while (rs.next()) {

                int countryID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");

                Country addCountry = new Country(countryID, countryName);

                countryList.add(addCountry);
            }
        }
        return countryList;
    }
}
