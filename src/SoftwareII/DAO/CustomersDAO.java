package SoftwareII.DAO;

import SoftwareII.Model.Appointment;
import SoftwareII.Model.Country;
import SoftwareII.Model.Customer;
import SoftwareII.Model.First_Level_Divisions;
import SoftwareII.Utility.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

/**CustomersDAO retrieves and manipulates customer information from SQL database.
 */
public class CustomersDAO {

    /**Creates a list of all customers form SQL database
     * @return - returns list of customers
     * @throws SQLException - Exception
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {

        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        String sqlStatement = "SELECT * FROM customers";

        JDBC.setPreparedStatement(sqlStatement, JDBC.getConnection());

        PreparedStatement custListStatement = JDBC.getPreparedStatement();

        ResultSet rs = custListStatement.executeQuery();

        if (rs != null) {
            while (rs.next()) {
                int custID = rs.getInt("Customer_ID");
                String custName = rs.getString("Customer_Name");
                String custAddress = rs.getString("Address");
                String custPostalCode = rs.getString("Postal_Code");
                String custPhone = rs.getString("Phone");
                int divID = rs.getInt("Division_ID");

                Customer addCust = new Customer(custID, custName, custAddress, custPostalCode, custPhone, divID);

                customerList.add(addCust);
            }
        }
        return customerList;
    }

    /**Uses Division ID to return customer's country
     * @param divID - division ID
     * @return - returns customer's country
     * @throws SQLException - Exception
     */
    public static String custCountryFromDivID(int divID) throws SQLException {

        ObservableList<First_Level_Divisions> divisionsList = First_Level_DivisionsDAO.getAllDivisions();
        ResourceBundle rBundle = ResourceBundle.getBundle("SoftwareII/Utility/Scheduling", Locale.getDefault());

        int countryID = 0;
        String custCountry = null;


        for (First_Level_Divisions div : divisionsList) {
            if (div.getDivisionID() == divID) {
                countryID = div.getCountryID();
            }
        }

        if (countryID == 1) {
            custCountry = rBundle.getString("unitedStates");
        }
        if (countryID == 2) {
            custCountry = rBundle.getString("unitedKingdom");
        }
        if (countryID == 3) {
            custCountry = "Canada";
        }

        return custCountry;
    }

    /**Uses Division ID to return customer's state to Customer constructor
     * @param divID - division ID
     * @return - returns name of customer's state
     * @throws SQLException - Exception
     */
    public static String custStateFromDivID(int divID) throws SQLException {

        ObservableList<First_Level_Divisions> divisionsList = First_Level_DivisionsDAO.getAllDivisions();

        String custState = null;

        for (First_Level_Divisions div : divisionsList) {
            if (div.getDivisionID() == divID) {
                custState = div.getDivisionName();
            }
        }
            return custState;
    }

    /**Creates a list of division names from SQL database
     * @param selectedCountry - selected Country
     * @return - returns list of Divisions
     * @throws SQLException - Exception
     */
    public static ObservableList<String> getDivisionNames (Object selectedCountry) throws SQLException {

        ObservableList<Country> countryList = CountriesDAO.getAllCountries();
        String selectedCountryString = selectedCountry.toString();
        String selectedCountryConversion = null;

        int countryID = 0;

        for (Country country : countryList) {
            if (country.getCountryName().matches(selectedCountryString)) {
                countryID = country.getCountryID();
            }
        }

        if (countryID == 0) {
            if (selectedCountryString.matches("United States")) {
                selectedCountryConversion = "U.S";
            } else if (selectedCountryString.matches("United Kingdom")) {
                selectedCountryConversion = "UK";
            } else if (selectedCountryString.matches("Canada")) {
                selectedCountryConversion = "Canada";
            }
        }

        if (selectedCountryConversion != null) {
            for (Country country : countryList) {
                if (country.getCountryName().matches(selectedCountryConversion)) {
                    countryID = country.getCountryID();
                }
            }
        }

        ObservableList<String> divisionNames = FXCollections.observableArrayList();
        ObservableList<First_Level_Divisions> divisionList = First_Level_DivisionsDAO.getAllDivisions();

        for (First_Level_Divisions divObject : divisionList) {
            if (countryID == divObject.getCountryID()) {
                divisionNames.add(divObject.getDivisionName());
            }
        }
        return divisionNames;
    }

    /**Creates a list of country names from SQL database
     * @return - returns list of Country Names
     * @throws SQLException - Exception
     */
    public static ObservableList<String> getCountryNames () throws SQLException {
        ObservableList<Country> countryList;
        ObservableList<String> countryNames = FXCollections.observableArrayList();

        countryList = CountriesDAO.getAllCountries();

        for (Country country : countryList) {
            String countryName = country.getCountryName();
            countryNames.add(countryName);
        }

        return countryNames;
    }

    /**Takes division name and converts to division ID number
     * @param division - division name
     * @return - returns division ID
     * @throws SQLException - Exception
     */
    public static int getDivisionID(String division) throws SQLException {

        int divisionID = 0;

        ObservableList<First_Level_Divisions> divList = First_Level_DivisionsDAO.getAllDivisions();

        for (First_Level_Divisions customer : divList) {
            if (customer.getDivisionName().matches(division)){
                divisionID = customer.getDivisionID();
            }
        }
        return divisionID;
    }

    /**Adds new customer to SQL database
     * @param newCustomer - new Customer object
     * @throws SQLException - Exception
     */
    public static void addCustomer(Customer newCustomer) throws SQLException {

        //Set statement
        String insertStatement = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?,?,?,?,?,?,?,?,?)";

        JDBC.setPreparedStatement(insertStatement, JDBC.getConnection());

        PreparedStatement statement = JDBC.getPreparedStatement();

        //Import customer data
        String cusName = newCustomer.getCustName();
        String cusAddress = newCustomer.getCustAddress();
        String cusPostalCode = newCustomer.getCustPostalCode();
        String cusPhone = newCustomer.getCustPhone();
        LocalDateTime createDate = newCustomer.getCreatedDate();
        String createBy = newCustomer.getCreatedBy();
        Timestamp lastUpdate = newCustomer.getUpdatedTime();
        String updateBy = newCustomer.getUpdatedBy();
        int cusDivID = newCustomer.getDivID();

        //Mapping of value keys
        statement.setString(1, cusName);
        statement.setString(2, cusAddress);
        statement.setString(3, cusPostalCode);
        statement.setString(4, cusPhone);
        statement.setString(5, createDate.toString());
        statement.setString(6, createBy);
        statement.setString(7, lastUpdate.toString());
        statement.setString(8, updateBy);
        statement.setInt(9, cusDivID);

        //Execute statement
        statement.execute();
    }

    /**Modifies customer in SQL database
     * @param updatedCustomer - modified Customer object
     * @throws SQLException - Exception
     */
    public static void modifyCustomer(Customer updatedCustomer) throws SQLException {

        //Set statement
        String updateStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";

        JDBC.setPreparedStatement(updateStatement, JDBC.getConnection());

        PreparedStatement statement = JDBC.getPreparedStatement();

        //Import customer data
        String cusName = updatedCustomer.getCustName();
        String cusAddress = updatedCustomer.getCustAddress();
        String cusPostalCode = updatedCustomer.getCustPostalCode();
        String cusPhone = updatedCustomer.getCustPhone();
        Timestamp lastUpdate = updatedCustomer.getUpdatedTime();
        String updateBy = updatedCustomer.getUpdatedBy();
        int cusDivID = updatedCustomer.getDivID();
        int cusID = updatedCustomer.getCustID();

        //Mapping of value keys
        statement.setString(1, cusName);
        statement.setString(2, cusAddress);
        statement.setString(3, cusPostalCode);
        statement.setString(4, cusPhone);
        statement.setString(5, lastUpdate.toString());
        statement.setString(6, updateBy);
        statement.setInt(7, cusDivID);
        statement.setInt(8,cusID);

        //Execute statement
        statement.execute();
    }

    /** Deletes customer from SQL database
     * @param currentCustomer - Customer object to be deleted
     * @throws SQLException - Exception
     */
    public static void deleteCustomer(Customer currentCustomer) throws SQLException {

        //Set statement
        String deleteStatement = "DELETE FROM customers WHERE Customer_ID = ?";

        JDBC.setPreparedStatement(deleteStatement, JDBC.getConnection());

        PreparedStatement statement = JDBC.getPreparedStatement();

        //Import customer data
        int cusID = currentCustomer.getCustID();

        //Mapping of value keys
        statement.setInt(1,cusID);

        //Execute statement
        statement.execute();
    }

    /**Returns Customer object when provided customer ID
     * @param custID - customer ID
     * @return - returns customer
     * @throws SQLException - Exception
     */
    public static Customer uploadCust (int custID) throws SQLException {

        Customer findCust = null;

        ObservableList<Customer> allCust = getAllCustomers();

        for (Customer cust : allCust) {
            if (cust.getCustID() == custID) {
                findCust = cust;
            }
        }
        return findCust;
    }

    public static ObservableList<Appointment> getApptList(Customer customer) throws SQLException {

        ObservableList<Appointment> custAppt = FXCollections.observableArrayList();

        ObservableList<Appointment> getAllAppt = AppointmentsDAO.getAllApptsDTString();

        int custID = customer.getCustID();

        for (Appointment appt : getAllAppt) {
            if (appt.getCustID() == custID) {
                custAppt.add(appt);
            }
        }

        return custAppt;

    }
}
